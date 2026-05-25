package com.app.shiphub.ui.main.create_claim

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.app.base.BaseViewModel
import com.app.data.models.enums.EquipmentType
import com.app.data.models.enums.TestType
import com.app.data.models.requests.ClaimRequest
import com.app.data.models.requests.EquipmentRequest
import com.app.data.use_cases.ClaimsUseCase
import com.app.data.use_cases.UserUseCase
import com.app.shiphub.ShipHubApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class CreateClaimViewModel @Inject constructor(
    private val claimsUseCase: ClaimsUseCase,
    private val userUseCase: UserUseCase
) : BaseViewModel<CreateClaimState>(CreateClaimState.Content()) {

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            val user = userUseCase.getUser()
            val content = state.value as? CreateClaimState.Content ?: CreateClaimState.Content()
            
            val organizationName = user.legalInfo?.organizationName ?: ""
            val clientName = user.username
            val contactPhone = user.legalInfo?.phone ?: ""
            val email = user.email

            emitState(content.copy(
                organizationName = organizationName,
                clientName = clientName,
                contactPhone = contactPhone,
                email = email
            ))
        }
    }

    fun onOrganizationNameChanged(name: String) {
        updateContent { it.copy(organizationName = name) }
    }

    fun onClientNameChanged(name: String) {
        updateContent { it.copy(clientName = name) }
    }

    fun onPhoneChanged(phone: String) {
        updateContent { it.copy(contactPhone = phone) }
    }

    fun onEmailChanged(email: String) {
        updateContent { it.copy(email = email) }
    }

    fun onTestTypeSelected(index: Int) {
        updateContent { it.copy(testTypeIndex = index) }
    }

    fun onCustomTypeChanged(isCustom: Boolean) {
        updateContent { it.copy(isCustomType = isCustom) }
    }

    fun onCustomTestNameChanged(name: String) {
        updateContent { it.copy(customTestName = name) }
    }

    fun onAdditionalInfoChanged(info: String) {
        updateContent { it.copy(additionalInfo = info) }
    }

    fun onEquipmentNameChanged(name: String) {
        updateContent { it.copy(equipmentName = name) }
    }

    fun onEquipmentTypeSelected(index: Int) {
        updateContent { it.copy(equipmentTypeIndex = index) }
    }

    fun onManufacturerChanged(manufacturer: String) {
        updateContent { it.copy(manufacturer = manufacturer) }
    }

    fun onSerialNumberChanged(serial: String) {
        updateContent { it.copy(serialNumber = serial) }
    }

    fun onCountChanged(count: String) {
        updateContent { it.copy(count = count) }
    }

    fun addPhoto(uri: Uri) {
        updateContent { content ->
            if (content.photos.size < 3) {
                content.copy(photos = content.photos + uri)
            } else {
                content
            }
        }
    }

    fun removePhoto(uri: Uri) {
        updateContent { it.copy(photos = it.photos - uri) }
    }

    fun addDocument(uri: Uri) {
        updateContent { it.copy(documents = it.documents + uri) }
    }

    fun removeDocument(uri: Uri) {
        updateContent { it.copy(documents = it.documents - uri) }
    }

    private fun updateContent(update: (CreateClaimState.Content) -> CreateClaimState.Content) {
        val currentContent = state.value as? CreateClaimState.Content ?: CreateClaimState.Content()
        _state.update { update(currentContent) }
    }

    fun sendClaim() {
        val content = state.value as? CreateClaimState.Content ?: return
        
        if (content.isCustomType && content.customTestName.isBlank()) {
            viewModelScope.launch {
                emitState(CreateClaimState.Error("Введите название типа испытаний"))
                emitState(content) // Reset to content state
            }
            return
        }

        if (!content.isCustomType && (content.testTypeIndex == null || content.testTypeIndex == -1)) {
            viewModelScope.launch {
                emitState(CreateClaimState.Error("Выберите тип испытаний или укажите свой"))
                emitState(content) // Reset to content state
            }
            return
        }

        withLoading {
            val testType = if (content.testTypeIndex != null) {
                TestType.entries.getOrNull(content.testTypeIndex)
            } else null

            val equipmentType = if (content.equipmentTypeIndex != null) {
                EquipmentType.entries.getOrNull(content.equipmentTypeIndex)
            } else null

            val equipment = EquipmentRequest(
                equipmentType = equipmentType,
                name = content.equipmentName,
                manufacturer = content.manufacturer,
                serialNumber = content.serialNumber,
                count = content.count.toIntOrNull() ?: 1
            )

            val request = ClaimRequest(
                organizationName = content.organizationName,
                clientName = content.clientName,
                contactPhone = content.contactPhone,
                email = content.email,
                testType = testType,
                equipment = equipment,
                isCustomType = content.isCustomType,
                customTestName = if (content.isCustomType) content.customTestName else null,
                additionalInfo = content.additionalInfo
            )

            try {
                val context = ShipHubApplication.instance.applicationContext
                val jsonRequest = Json.encodeToString(request)
                val claimPart = MultipartBody.Part.createFormData(
                    "claim",
                    null,
                    jsonRequest.toRequestBody("application/json".toMediaTypeOrNull())
                )

                val photoParts = content.photos.mapIndexed { index, uri ->
                    val file = uriToFile(context, uri, "photo${index + 1}")
                    val requestFile = file.asRequestBody(
                        com.app.base.FileUtils.getMimeType(context, uri)?.toMediaTypeOrNull()
                    )
                    MultipartBody.Part.createFormData("photo${index + 1}", file.name, requestFile)
                }

                val docTypes = mutableListOf<String?>()
                val docParts = content.documents.mapIndexed { index, uri ->
                    val file = uriToFile(context, uri, "document${index + 1}")
                    docTypes.add("ACT")
                    val mimeType = com.app.base.FileUtils.getMimeType(context, uri)
                    val requestFile = file.asRequestBody(mimeType?.toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("document${index + 1}", file.name, requestFile)
                }

                claimsUseCase.createClaimWithPhotos(
                    claimPart = claimPart,
                    photoParts = photoParts,
                    documentParts = docParts,
                    docTypes = docTypes
                )
                emitState(CreateClaimState.Success)
            } catch (e: Exception) {
                emitState(CreateClaimState.Error(e.message ?: "Error creating claim"))
                emitState(content) // Reset to content state
            }
        }
    }

    private fun uriToFile(context: android.content.Context, uri: Uri, fileNamePrefix: String): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = com.app.base.FileUtils.getFileName(context, uri) ?: fileNamePrefix
        val file = File(context.cacheDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }
}
