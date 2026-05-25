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

    private fun setupClaimsAndDocuments(claimsIds: List<Long>, documents: List<Document>)  = with(binding){
        adapter.submitList(claimsIds.map { ClaimIdHolderModel(it) })
        setupDocuments(documents)
        binding.tvDocumentsAndClaimsNotFound.isVisible = claimsIds.isEmpty() && documents.isEmpty()
    }

    private fun setupDocumentSection(documents: List<Document>, ll: LinearLayout){
        ll.removeAllViews()
        ll.isVisible = documents.isNotEmpty()
        documents.forEach { document ->
            ll.addView(HolderDocumentBinding.inflate(layoutInflater, ll, false).apply {
                tvDocumentName.text = document.name
                ibDownload.setOnClickListener { Timber.i("Download ${document.id}") }
                ibShow.setOnClickListener { Timber.i("Show ${document.id}") }
            }.root)
        }
    }

    private fun setupDocuments(documents: List<Document>) = with(binding){
        val groups = documents.groupBy { it.type }
        val contracts = groups[DocumentType.AGREEMENT].orEmpty()
        val acts = groups[DocumentType.ACT].orEmpty()
        val checks = groups[DocumentType.CHECK].orEmpty()
        val info = groups[DocumentType.INFO].orEmpty()
        tvContracts.isVisible = contracts.isNotEmpty()
        vContracts.isVisible = contracts.isNotEmpty()
        setupDocumentSection(contracts, llContracts)
        tvActs.isVisible = acts.isNotEmpty()
        vActs.isVisible = acts.isNotEmpty()
        setupDocumentSection(acts, llActs)
        tvChecks.isVisible = checks.isNotEmpty()
        vChecks.isVisible = checks.isNotEmpty()
        setupDocumentSection(checks, llChecks)
        tvInfo.isVisible = info.isNotEmpty()
        vInfo.isVisible = info.isNotEmpty()
        setupDocumentSection(info, llInfo)
        binding.tvDocumentsNotFound.isVisible = documents.isEmpty()
    }

    override fun handleState(state: DocumentsState) {
        when(state){
            is DocumentsState.InitScreen -> {}
            is DocumentsState.SetupDocumentsAndClaims -> {
                if (state.claimsIds.isNotEmpty()) binding.tvCurrentClaim.text = getString(R.string.claim_of_number, viewModel.getCurrentClaimId())
                else binding.tvCurrentClaim.text = getString(R.string.claim_not_found)
                setupClaimsAndDocuments(state.claimsIds, state.currentDocuments)
            }
            is DocumentsState.SetupDocumentByCurrentClaim -> {
                viewModel.getCurrentClaimId().let {
                    if (it == null) binding.tvCurrentClaim.text = getString(R.string.claim_not_found)
                    else binding.tvCurrentClaim.text = getString(R.string.claim_of_number, it)
                }
                setupDocuments(state.documents)
            }
        }
    }

    override fun onClick(claimId: Long) {
        viewModel.getDocuments(claimId)
    }
}