package com.app.shiphub.ui.main.claims.claim

import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import com.app.shiphub.ui.main.claims.claim.adapter.StatusAdapter
import com.app.shiphub.ui.main.claims.claim.adapter.StatusHolderModel
import com.app.shiphub.databinding.DialogSelectStatusBinding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.app.base.BaseFragment
import com.app.data.models.domain.Claim
import com.app.data.models.domain.User
import com.app.data.models.enums.ClaimStatus
import com.app.data.models.enums.UserType
import com.app.shiphub.BuildConfig
import com.app.shiphub.R
import com.app.shiphub.databinding.FragmentClaimBinding
import com.app.shiphub.databinding.HolderHomeNotificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClaimFragment: BaseFragment<FragmentClaimBinding, ClaimUIState, ClaimViewModel>() {

    override val viewModel: ClaimViewModel by viewModels()

    private val currentUser: User
        get() = viewModel.getUser()
    private val isManager: Boolean
        get() = currentUser.type == UserType.MANAGER
    val allSteps
        get() = listOf(
            binding.tvClaimCreated,
            binding.tvManagerApproved,
            binding.tvInWork,
            binding.tvTestsCompleted,
            binding.tvDocumentsDelivered
        )

    private val args: ClaimFragmentArgs by navArgs()

    override fun initializeBinding() = FragmentClaimBinding.inflate(layoutInflater)

    override fun setupListeners() {
        binding.ibBack.setOnClickListener {
            navigateBack()
        }
        binding.bChat.setOnClickListener {
            navigate(ClaimFragmentDirections.actionClaimFragmentToChatFragment(args.claimId))
        }
        binding.bChangeStatus.setOnClickListener {
            showStatusSelectionDialog()
        }
    }

    private var currentClaim: Claim? = null

    private fun showStatusSelectionDialog() {
        val claim = currentClaim ?: return
        val availableStatuses = getAvailableStatuses(claim.status)

        if (availableStatuses.isEmpty()) {
            showToast("Нет доступных статусов для изменения")
            return
        }

        val dialogBinding = DialogSelectStatusBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .setView(dialogBinding.root)
            .create()

        val adapter = StatusAdapter { newStatus ->
            viewModel.updateStatus(claim.id, newStatus)
            dialog.dismiss()
        }

        dialogBinding.rvStatuses.adapter = adapter
        dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }

        val models = availableStatuses.map {
            StatusHolderModel(it, getColorByStatus(it))
        }
        adapter.submitList(models)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    private fun getAvailableStatuses(status: ClaimStatus): List<ClaimStatus> {
        return when (status) {
            ClaimStatus.CREATED -> listOf(
                ClaimStatus.APPROVED,
                ClaimStatus.IN_PROGRESS,
                ClaimStatus.TESTS_COMPLETED,
                ClaimStatus.DOCUMENTS_DELIVERED,
                ClaimStatus.ENDED
            )
            ClaimStatus.APPROVED -> listOf(
                ClaimStatus.IN_PROGRESS,
                ClaimStatus.TESTS_COMPLETED,
                ClaimStatus.DOCUMENTS_DELIVERED,
                ClaimStatus.ENDED
            )
            ClaimStatus.IN_PROGRESS -> listOf(
                ClaimStatus.TESTS_COMPLETED,
                ClaimStatus.DOCUMENTS_DELIVERED
            )
            ClaimStatus.TESTS_COMPLETED -> listOf(
                ClaimStatus.IN_PROGRESS,
                ClaimStatus.DOCUMENTS_DELIVERED
            )
            ClaimStatus.DOCUMENTS_DELIVERED -> listOf(
                ClaimStatus.ENDED
            )
            ClaimStatus.ENDED -> emptyList()
        }
    }

    override fun setupUI() = with(binding){
        viewModel.getClaim(args.claimId)
        bChat.text = if (isManager) getString(R.string.chat_with_client)
        else getString(R.string.chat_with_manager)
        bChangeStatus.isVisible = isManager
    }

    private fun setupClaimInfo(claim: Claim, notifications: List<String>) = with(binding){
        currentClaim = claim
        tvClaimId.text = getString(R.string.claim_number, claim.id)
        tvStatus.apply {
            text = claim.status.displayName
            val color = getColorByStatus(claim.status)
            setTextColor(getColor(requireContext(), color))
            cvStatus.setCardBackgroundColor(getColor(requireContext(), color))
        }
        val activeTextView = getActiveTypeTextView(claim.status)
        allSteps.forEach { tv ->
            if (tv == activeTextView) {
                tv.textSize = 18f
                tv.setTypeface(null, Typeface.BOLD)
            } else {
                tv.textSize = 14f
                tv.setTypeface(null, Typeface.NORMAL)
            }
        }
        tvEquipmentType.text = getString(R.string.equipment_type, claim.equipment.equipmentType.displayName)
        tvEquipmentModel.text = getString(R.string.equipment_name, claim.equipment.name)
        tvSerialNumber.text = getString(R.string.serial_number, claim.equipment.serialNumber)
        tvServiceType.text = getString(R.string.service_type, claim.testType.displayName)
        llNotifications.removeAllViews()
        if (notifications.isEmpty()) {
            val notifBinding = HolderHomeNotificationBinding.inflate(layoutInflater).apply {
                tvNotifText.text = getString(R.string.notifications_is_empty)
            }
            llNotifications.addView(notifBinding.root)
        }
        setupPhotos(claim.equipment.imageIds)
    }

    private fun setupPhotos(photoIds: List<Long>?) = with(binding) {
        val photoImageViews = listOf(ivPhoto1, ivPhoto2, ivPhoto3)

        if (photoIds.isNullOrEmpty()) {
            photosContainer.isVisible = false
            return@with
        }

        photosContainer.isVisible = true
        photoIds.forEachIndexed { index, photoId ->
            if (index < photoImageViews.size) {
                val url = "${BuildConfig.BASE_URL}photos/$photoId"
                photoImageViews[index].loadPhoto(url)
            }
        }

        // Hide unused photo views
        for (i in photoIds.size until photoImageViews.size) {
            photoImageViews[i].isVisible = false
        }
    }

    private fun ImageView.loadPhoto(url: String) {
        this.load(url) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_background)
        }
    }

    private fun getActiveTypeTextView(status: ClaimStatus): TextView {
        with(binding){
            return when(status){
                ClaimStatus.CREATED -> tvClaimCreated
                ClaimStatus.APPROVED -> tvManagerApproved
                ClaimStatus.IN_PROGRESS -> tvInWork
                ClaimStatus.TESTS_COMPLETED -> tvTestsCompleted
                ClaimStatus.DOCUMENTS_DELIVERED, ClaimStatus.ENDED -> tvDocumentsDelivered
            }
        }
    }

    private fun getColorByStatus(status: ClaimStatus): Int{
        return when(status){
            ClaimStatus.CREATED -> R.color.blue
            ClaimStatus.APPROVED -> R.color.green
            ClaimStatus.IN_PROGRESS -> R.color.orange
            ClaimStatus.TESTS_COMPLETED -> R.color.turquoise
            ClaimStatus.DOCUMENTS_DELIVERED -> R.color.purple
            ClaimStatus.ENDED -> R.color.green
        }
    }

    override fun handleState(state: ClaimUIState) {
        when(state){
            is ClaimUIState.Init -> {}
            is ClaimUIState.InitScreen -> setupClaimInfo(state.claim, state.notifications)
        }
    }
}
