package com.app.shiphub.ui.main.claims.chat

import android.view.Gravity
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.base.BaseAdapter
import com.app.base.BaseDiffUtilCallback
import com.app.base.BaseFragment
import com.app.base.BaseViewHolder
import com.app.shiphub.R
import com.app.shiphub.databinding.FragmentChatBinding
import com.app.shiphub.databinding.HolderChatMessageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding, ChatUIState, ChatViewModel>() {

    override val viewModel: ChatViewModel by viewModels()
    private val args: ChatFragmentArgs by navArgs()

    private val adapter = object : BaseAdapter<ChatMessage, BaseViewHolder>(BaseDiffUtilCallback()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            return BaseViewHolder(HolderChatMessageBinding.inflate(layoutInflater, parent, false))
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            val item = getItem(position)
            val binding = HolderChatMessageBinding.bind(holder.itemView)
            binding.tvMessageContent.text = item.content
            val context = binding.root.context
            
            val params = binding.cvMessage.layoutParams as android.widget.LinearLayout.LayoutParams
            if (item.isFromMe) {
                params.gravity = Gravity.END
                binding.cvMessage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.brown))
                binding.tvMessageContent.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                params.gravity = Gravity.START
                binding.cvMessage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
                binding.tvMessageContent.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
            binding.cvMessage.layoutParams = params
        }
    }

    override fun initializeBinding() = FragmentChatBinding.inflate(layoutInflater)

    override fun setupUI() {
        binding.rvMessages.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true
        }
        binding.rvMessages.adapter = adapter
        binding.tvTitle.text = getString(R.string.chat_for_claim, args.claimId)
        viewModel.initChat(args.claimId)
    }

    override fun setupListeners() {
        binding.ibBack.setOnClickListener { navigateBack() }
        binding.ibSend.setOnClickListener {
            val text = binding.etMessage.text.toString()
            if (text.isNotBlank()) {
                viewModel.sendMessage(text)
                binding.etMessage.setText("")
            }
        }
    }

    override fun handleState(state: ChatUIState) {
        when (state) {
            is ChatUIState.Content -> {
                adapter.submitList(state.messages)
                if (state.messages.isNotEmpty()) {
                    binding.rvMessages.post {
                        binding.rvMessages.smoothScrollToPosition(state.messages.size - 1)
                    }
                }
            }
            is ChatUIState.Error -> showToast(state.message)
            else -> {}
        }
    }

    override fun onDestroyView() {
        viewModel.closeChat()
        super.onDestroyView()
    }
}
