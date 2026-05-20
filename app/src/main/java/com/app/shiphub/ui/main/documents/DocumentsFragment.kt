package com.app.shiphub.ui.main.documents

import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.app.base.BaseFragment
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
class DocumentsFragment: BaseFragment<FragmentDocumentsBinding, DocumentsState, DocumentsViewModel>(), ClaimIdsAdapter.ClaimIdsCallback {

    override val viewModel: DocumentsViewModel by viewModels()
    private val adapter by lazy { ClaimIdsAdapter(this) }
    private var isExpanded = false

    override fun initializeBinding() = FragmentDocumentsBinding.inflate(layoutInflater)

    override fun setupListeners() = with(binding){
        cvClaims.setOnClickListener {
            isExpanded = !isExpanded
            val image = AppCompatResources.getDrawable(
                requireContext(),
                if (isExpanded) R.drawable.arrow_down
                else R.drawable.arrow_left
            )
            ivExpand.setImageDrawable(image)
            rvClaimsIds.isVisible = isExpanded
        }
    }

    override fun setupUI() = with(binding){
        rvClaimsIds.adapter = adapter
    }

    private fun setupClaimsAndDocuments(claimsIds: List<Long>, documents: List<Document>) {
        adapter.submitList(claimsIds.map { ClaimIdHolderModel(it) })
        binding.tvDocumentsAndClaimsNotFound.isVisible = false
        setupDocuments(documents)
    }

    private fun setupDocumentSection(documents: List<Document>, ll: LinearLayout){
        ll.removeAllViews()
        ll.apply {
            isVisible = documents.isNotEmpty()
            if (documents.isNotEmpty()){
                documents.forEach { document ->
                    addView(HolderDocumentBinding.inflate(layoutInflater, this, false).apply {
                        tvDocumentName.text = document.name
                        ibDownload.setOnClickListener { Timber.i("Download ${document.id}") }
                        ibShow.setOnClickListener { Timber.i("Show ${document.id}") }
                    }.root)
                }
            }
        }
    }

    private fun setupDocuments(documents: List<Document>) = with(binding){
        val contracts = documents.filter { it.type == DocumentType.AGREEMENT }
        val acts = documents.filter { it.type == DocumentType.ACT }
        val checks = documents.filter { it.type == DocumentType.CHECK }
        tvContracts.isVisible = contracts.isNotEmpty()
        vContracts.isVisible = contracts.isNotEmpty()
        setupDocumentSection(contracts, llContracts)
        tvActs.isVisible = acts.isNotEmpty()
        vActs.isVisible = acts.isNotEmpty()
        setupDocumentSection(acts, llActs)
        tvChecks.isVisible = checks.isNotEmpty()
        vChecks.isVisible = checks.isNotEmpty()
        setupDocumentSection(checks, llChecks)
        tvDocumentsNotFound.isVisible = contracts.isEmpty() && acts.isEmpty() && checks.isEmpty()
    }

    override fun handleState(state: DocumentsState) {
        when(state){
            is DocumentsState.InitScreen -> {}
            is DocumentsState.SetupDocumentsAndClaims -> setupClaimsAndDocuments(state.claimsIds, state.currentDocuments)
            is DocumentsState.SetupDocumentByCurrentClaim -> setupDocuments(state.documents)
            is DocumentsState.SetupEmptyState -> {
                setupDocuments(emptyList())
                binding.tvDocumentsAndClaimsNotFound.isVisible = true
            }
        }
    }

    override fun onClick(claimId: Long) {
        viewModel.getDocuments(claimId)
    }
}