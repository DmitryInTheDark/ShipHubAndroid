package com.app.shiphub.ui.main.documents

import android.net.Uri
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.app.base.BaseFragment
import com.app.base.FileUtils
import com.app.data.models.domain.Document
import com.app.data.models.enums.DocumentType
import com.app.shiphub.R
import com.app.shiphub.databinding.FragmentDocumentsBinding
import com.app.shiphub.databinding.HolderDocumentBinding
import com.app.shiphub.ui.main.documents.adapter.ClaimIdHolderModel
import com.app.shiphub.ui.main.documents.adapter.ClaimIdsAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DocumentsFragment : BaseFragment<FragmentDocumentsBinding, DocumentsState, DocumentsViewModel>(),
    ClaimIdsAdapter.ClaimIdsCallback {

    override val viewModel: DocumentsViewModel by viewModels()
    private val adapter by lazy { ClaimIdsAdapter(this) }
    private var isExpanded = false

    private val documentPicker = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let { handleDocumentPicked(it) }
    }

    private var selectedDocumentType: DocumentType? = null

    override fun initializeBinding() = FragmentDocumentsBinding.inflate(layoutInflater)

    override fun setupListeners() = with(binding) {
        cvClaims.setOnClickListener {
            isExpanded = !isExpanded
            val image = AppCompatResources.getDrawable(
                requireContext(),
                if (isExpanded) R.drawable.arrow_down else R.drawable.arrow_left
            )
            ivExpand.setImageDrawable(image)
            rvClaimsIds.isVisible = isExpanded
        }

        // Кнопки выбора типа документа
        btnAddAgreement.setOnClickListener {
            selectedDocumentType = DocumentType.AGREEMENT
            documentPicker.launch(arrayOf("application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
        }

        btnAddAct.setOnClickListener {
            selectedDocumentType = DocumentType.ACT
            documentPicker.launch(arrayOf("application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
        }

        btnAddCheck.setOnClickListener {
            selectedDocumentType = DocumentType.CHECK
            documentPicker.launch(arrayOf("application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
        }

        btnAddInfo.setOnClickListener {
            selectedDocumentType = DocumentType.INFO
            documentPicker.launch(arrayOf("application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
        }

        // Кнопка загрузки на сервер
        btnUploadDocuments.setOnClickListener {
            viewModel.uploadPendingDocuments()
        }
    }

    private fun handleDocumentPicked(uri: Uri) {
        selectedDocumentType?.let { type ->
            viewModel.addDocument(uri, type)
            selectedDocumentType = null
        }
    }

    override fun setupUI() = with(binding) {
        rvClaimsIds.adapter = adapter
    }

    private fun setupClaimsAndDocuments(
        claimsIds: List<Long>,
        documents: List<Document>,
        pending: Map<DocumentType, List<Uri>> = emptyMap()
    ) = with(binding) {
        adapter.submitList(claimsIds.map { ClaimIdHolderModel(it) })
        setupDocuments(documents, pending)
        binding.tvDocumentsAndClaimsNotFound.isVisible = claimsIds.isEmpty() && documents.isEmpty()
    }

    private fun setupDocumentSection(
        documents: List<Document>,
        pendingUris: List<Uri>,
        ll: LinearLayout,
        type: DocumentType
    ) {
        ll.removeAllViews()
        ll.isVisible = documents.isNotEmpty() || pendingUris.isNotEmpty()

        // Уже загруженные документы
        documents.forEach { document ->
            addDocumentView(ll, document.name, isPending = false, documentId = document.id)
        }

        // Ожидающие загрузки
        pendingUris.forEach { uri ->
            val fileName = FileUtils.getFileName(requireContext(), uri) ?: "Документ"
            addDocumentView(ll, fileName, isPending = true, uri = uri, type = type)
        }
    }

    private fun addDocumentView(
        ll: LinearLayout,
        name: String,
        isPending: Boolean,
        documentId: Long? = null,
        uri: Uri? = null,
        type: DocumentType? = null
    ) {
        val holder = HolderDocumentBinding.inflate(layoutInflater, ll, false).apply {
            tvDocumentName.text = name

            if (isPending) {
                ibDownload.isVisible = false
                ibShow.setImageResource(android.R.drawable.ic_menu_delete)
                ibShow.setOnClickListener {
                    uri?.let { viewModel.removePendingDocument(it, type!!) }
                }
            } else {
                ibDownload.setOnClickListener { Timber.i("Download $documentId") }
                ibShow.setOnClickListener { Timber.i("Show $documentId") }
            }
        }
        ll.addView(holder.root)
    }

    private fun setupDocuments(
        documents: List<Document>,
        pending: Map<DocumentType, List<Uri>> = emptyMap()
    ) = with(binding) {
        val groups = documents.groupBy { it.type }

        setupDocumentSection(
            groups[DocumentType.AGREEMENT].orEmpty(),
            pending[DocumentType.AGREEMENT].orEmpty(),
            llContracts,
            DocumentType.AGREEMENT
        )
        setupDocumentSection(
            groups[DocumentType.ACT].orEmpty(),
            pending[DocumentType.ACT].orEmpty(),
            llActs,
            DocumentType.ACT
        )
        setupDocumentSection(
            groups[DocumentType.CHECK].orEmpty(),
            pending[DocumentType.CHECK].orEmpty(),
            llChecks,
            DocumentType.CHECK
        )
        setupDocumentSection(
            groups[DocumentType.INFO].orEmpty(),
            pending[DocumentType.INFO].orEmpty(),
            llInfo,
            DocumentType.INFO
        )

//        // Видимость заголовков
//        tvContracts.isVisible = groups[DocumentType.AGREEMENT].orEmpty().isNotEmpty() ||
//                pending[DocumentType.AGREEMENT].orEmpty().isNotEmpty()
//        vContracts.isVisible = tvContracts.isVisible
//
//        // Аналогично для остальных секций (активируй по необходимости)
//        tvActs.isVisible = groups[DocumentType.ACT].orEmpty().isNotEmpty() ||
//                pending[DocumentType.ACT].orEmpty().isNotEmpty()
//        vActs.isVisible = tvActs.isVisible
//
//        tvChecks.isVisible = groups[DocumentType.CHECK].orEmpty().isNotEmpty() ||
//                pending[DocumentType.CHECK].orEmpty().isNotEmpty()
//        vChecks.isVisible = tvChecks.isVisible
//
//        tvInfo.isVisible = groups[DocumentType.INFO].orEmpty().isNotEmpty() ||
//                pending[DocumentType.INFO].orEmpty().isNotEmpty()
//        vInfo.isVisible = tvInfo.isVisible
    }

    override fun handleState(state: DocumentsState) {
        when (state) {
            is DocumentsState.InitScreen -> {}

            is DocumentsState.SetupDocumentsAndClaims -> {
                if (state.claimsIds.isNotEmpty()) {
                    binding.tvCurrentClaim.text = getString(R.string.claim_of_number, viewModel.getCurrentClaimId())
                } else {
                    binding.tvCurrentClaim.text = getString(R.string.claim_not_found)
                }
                setupClaimsAndDocuments(
                    claimsIds = state.claimsIds,
                    documents = state.currentDocuments,
                    pending = state.pendingDocuments   // ← Исправлено
                )
            }

            is DocumentsState.SetupDocumentByCurrentClaim -> {
                viewModel.getCurrentClaimId().let {
                    binding.tvCurrentClaim.text = if (it == null)
                        getString(R.string.claim_not_found)
                    else
                        getString(R.string.claim_of_number, it)
                }
                setupDocuments(state.documents, state.pendingDocuments)
            }
        }
    }

    override fun onClick(claimId: Long) {
        viewModel.getDocuments(claimId)
    }
}