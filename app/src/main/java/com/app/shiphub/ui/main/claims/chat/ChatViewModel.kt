package com.app.shiphub.ui.main.claims.chat

import androidx.lifecycle.viewModelScope
import com.app.base.BaseViewModel
import com.app.data.use_cases.ClaimsUseCase
import com.app.shiphub.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import timber.log.Timber
import javax.inject.Inject
import androidx.core.net.toUri

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val claimsUseCase: ClaimsUseCase
) : BaseViewModel<ChatUIState>(ChatUIState.Init) {

    private val json = Json { ignoreUnknownKeys = true }
    private var webSocket: WebSocket? = null
    private var currentClaimId: Long = -1
    private var currentUserId: Long = -1

    fun initChat(claimId: Long) {
        if (currentClaimId == claimId) return

        currentClaimId = claimId
        currentUserId = claimsUseCase.getCurrentUserId()

        _state.update { ChatUIState.Content(claimId = claimId, messages = emptyList()) }

        viewModelScope.launch {
            try {
                val history = claimsUseCase.getMessagesByClaim(claimId)
                val messages = history.items.map {
                    ChatMessage(
                        content = it.text,
                        isFromMe = it.senderId == currentUserId
                    )
                }
                updateContent { it.copy(messages = messages) }
                connectWebSocket()
            } catch (e: Exception) {
                if (e !is CancellationException) {
                    Timber.e(e)
                    emitState(ChatUIState.Error(e.localizedMessage ?: "Ошибка загрузки истории чата"))
                }
            }
        }
    }

    fun closeChat() {
        webSocket?.close(1000, "Closing")
        webSocket = null
        currentClaimId = -1
    }

    fun sendMessage(content: String) {
        if (content.isBlank()) return
        val sent = webSocket?.send(
            json.encodeToString(
                ChatWebSocketMessage(
                    type = "chat",
                    claimId = currentClaimId,
                    text = content
                )
            )
        )
        if (sent == true) {
            updateContent { it.copy(messages = it.messages + ChatMessage(content = content, isFromMe = true)) }
        } else {
            viewModelScope.launch {
                emitState(ChatUIState.Error("Не удалось отправить сообщение"))
            }
        }
    }

    private fun connectWebSocket() {
        if (webSocket != null) return

        val request = Request.Builder()
            .url(buildWebSocketUrl())
            .build()

        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Timber.d("WebSocket opened: ${response.code}")
                sendSubscribeRequest()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                viewModelScope.launch {
                    handleIncomingMessage(text)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Timber.e(t, "WebSocket failure")
                viewModelScope.launch {
                    emitState(ChatUIState.Error("Ошибка WebSocket: ${t.localizedMessage ?: "соединение потеряно"}"))
                }
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Timber.d("WebSocket closed: code=$code reason=$reason")
            }
        })
    }

    private fun sendSubscribeRequest() {
        webSocket?.send(
            json.encodeToString(
                ChatWebSocketMessage(
                    type = "subscribe",
                    claimId = currentClaimId
                )
            )
        )
    }

    private suspend fun handleIncomingMessage(payload: String) {
        try {
            val message = json.decodeFromString(ChatWebSocketMessage.serializer(), payload)
            when (message.type) {
                "chat" -> {
                    val content = message.text.orEmpty()
                    if (content.isNotBlank() && message.senderId != currentUserId) {
                        updateContent { it.copy(messages = it.messages + ChatMessage(content = content, isFromMe = false)) }
                    }
                }
                "error" -> emitState(ChatUIState.Error(message.error ?: "Ошибка сервера"))
                else -> Timber.d("WebSocket info: type=${message.type} info=${message.info}")
            }
        } catch (e: Exception) {
            Timber.w(e, "Failed parse WebSocket message")
        }
    }

    private fun buildWebSocketUrl(): String {
        val uri = BuildConfig.BASE_URL.toUri()
        val scheme = when (uri.scheme) {
            "https" -> "wss"
            "http" -> "ws"
            "wss", "ws" -> uri.scheme
            else -> "ws"
        }
        val host = uri.host ?: return "ws://localhost/ws/chat"
        val port = if (uri.port != -1) uri.port else if (scheme == "wss") 443 else 80
        return "$scheme://$host:$port/ws/chat"
    }

    private fun updateContent(update: (ChatUIState.Content) -> ChatUIState.Content) {
        val current = state.value as? ChatUIState.Content ?: return
        _state.update { update(current) }
    }
}
