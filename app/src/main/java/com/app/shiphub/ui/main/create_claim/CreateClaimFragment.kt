package com.app.shiphub.ui.main.create_claim

import android.net.Uri
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import coil.load
import com.app.base.BaseFragment
import com.app.base.FileUtils
import com.app.data.models.enums.EquipmentType
import com.app.data.models.enums.TestType
import com.app.shiphub.R
import com.app.shiphub.databinding.FragmentCreateClaimBinding
import com.app.shiphub.databinding.HolderDocumentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateClaimFragment : BaseFragment<FragmentCreateClaimBinding, CreateClaimState, CreateClaimViewModel>() {

    override val viewModel: CreateClaimViewModel by viewModels()

    private val photoPicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.addPhoto(it) }
    }

    private val documentPicker = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let { viewModel.addDocument(it) }
    }

    override fun initializeBinding() = FragmentCreateClaimBinding.inflate(layoutInflater)

    override fun setupUI() {
        val testTypes = TestType.entries.map { it.displayName }
        val testAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, testTypes)
        binding.actvTestType.setAdapter(testAdapter)

        val equipmentTypes = EquipmentType.entries.map { it.displayName }
        val equipmentAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, equipmentTypes)
        binding.actvEquipmentType.setAdapter(equipmentAdapter)
    }

    override fun setupListeners() = with(binding) {
        etOrgName.doAfterTextChanged { viewModel.onOrganizationNameChanged(it.toString()) }
        etClientName.doAfterTextChanged { viewModel.onClientNameChanged(it.toString()) }
        etPhone.doAfterTextChanged { viewModel.onPhoneChanged(it.toString()) }
        etEmail.doAfterTextChanged { viewModel.onEmailChanged(it.toString()) }
        
        actvTestType.setOnClickListener {
            if (!binding.cbCustomType.isChecked) {
                actvTestType.showDropDown()
            }
        }
        
        actvTestType.setOnItemClickListener { _, _, position, _ ->
            if (!binding.cbCustomType.isChecked) {
                viewModel.onTestTypeSelected(position)
            }
        }

        actvEquipmentType.setOnClickListener {
            actvEquipmentType.showDropDown()
        }

        actvEquipmentType.setOnItemClickListener { _, _, position, _ ->
            viewModel.onEquipmentTypeSelected(position)
        }
        
        cbCustomType.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onCustomTypeChanged(isChecked)
            tilCustomTestName.isVisible = isChecked
            tilTestType.isEnabled = !isChecked
            if (isChecked) {
                actvTestType.setText("")
                viewModel.onTestTypeSelected(-1)
            }
        }
        
        etCustomTestName.doAfterTextChanged { viewModel.onCustomTestNameChanged(it.toString()) }
        etAdditionalInfo.doAfterTextChanged { viewModel.onAdditionalInfoChanged(it.toString()) }
        
        etEquipmentName.doAfterTextChanged { viewModel.onEquipmentNameChanged(it.toString()) }
        etManufacturer.doAfterTextChanged { viewModel.onManufacturerChanged(it.toString()) }
        etSerialNumber.doAfterTextChanged { viewModel.onSerialNumberChanged(it.toString()) }
        etCount.doAfterTextChanged { viewModel.onCountChanged(it.toString()) }
        
        btnAddPhoto.setOnClickListener {
            photoPicker.launch("image/*")
        }
        
        btnAddDocument.setOnClickListener {
            documentPicker.launch(arrayOf(
                "application/pdf",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            ))
        }
        
        btnSend.setOnClickListener {
            viewModel.sendClaim()
        }
    }

    override fun handleState(state: CreateClaimState) {
        when (state) {
            is CreateClaimState.Init -> {}
            is CreateClaimState.Content -> {
                updateUI(state)
            }
            is CreateClaimState.Success -> {
                showToast(getString(R.string.claim_is_create))
                navigateBack()
            }
            is CreateClaimState.Error -> {
                showToast(state.message)
            }
        }
    }

    private fun updateUI(state: CreateClaimState.Content) = with(binding) {
        // Only set text if it's different to avoid cursor reset
        if (etOrgName.text.toString() != state.organizationName) {
            etOrgName.setText(state.organizationName)
        }
        if (etClientName.text.toString() != state.clientName) {
            etClientName.setText(state.clientName)
        }
        if (etPhone.text.toString() != state.contactPhone) {
            etPhone.setText(state.contactPhone)
        }
        if (etEmail.text.toString() != state.email) {
            etEmail.setText(state.email)
        }

        // Test Type
        if (state.isCustomType) {
            actvTestType.setText("")
            tilTestType.isEnabled = false
        } else {
            tilTestType.isEnabled = true
            state.testTypeIndex?.let { index ->
                if (index != -1) {
                    val testTypeName = TestType.entries.getOrNull(index)?.displayName
                    if (actvTestType.text.toString() != testTypeName) {
                        actvTestType.setText(testTypeName, false)
                    }
                }
            }
        }

        // Equipment Type
        state.equipmentTypeIndex?.let { index ->
            val equipmentTypeName = EquipmentType.entries.getOrNull(index)?.displayName
            if (actvEquipmentType.text.toString() != equipmentTypeName) {
                actvEquipmentType.setText(equipmentTypeName, false)
            }
        }

        // Photos
        ivPhoto1.isVisible = state.photos.size > 0
        if (state.photos.size > 0) {
            ivPhoto1.load(state.photos[0])
            ivPhoto1.setOnClickListener { viewModel.removePhoto(state.photos[0]) }
        }
        
        ivPhoto2.isVisible = state.photos.size > 1
        if (state.photos.size > 1) {
            ivPhoto2.load(state.photos[1])
            ivPhoto2.setOnClickListener { viewModel.removePhoto(state.photos[1]) }
        }
        
        ivPhoto3.isVisible = state.photos.size > 2
        if (state.photos.size > 2) {
            ivPhoto3.load(state.photos[2])
            ivPhoto3.setOnClickListener { viewModel.removePhoto(state.photos[2]) }
        }
        
        btnAddPhoto.isEnabled = state.photos.size < 3

        // Documents
        setupDocuments(state.documents)
    }

    private fun setupDocuments(documents: List<Uri>) {
        binding.llDocuments.removeAllViews()
        documents.forEach { uri ->
            val docBinding = HolderDocumentBinding.inflate(layoutInflater, binding.llDocuments, false)
            docBinding.tvDocumentName.text = FileUtils.getFileName(requireContext(), uri) ?: "Document"
            docBinding.ibDownload.isVisible = false
            docBinding.ibShow.setImageResource(android.R.drawable.ic_menu_delete)
            docBinding.ibShow.setOnClickListener {
                viewModel.removeDocument(uri)
            }
            binding.llDocuments.addView(docBinding.root)
        }
    }
}
