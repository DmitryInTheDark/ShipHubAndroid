package com.app.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch

abstract class BasePagingFragment<VB: ViewBinding, T, M: BaseHolderModel, VH: BaseViewHolder, S : BaseState, VM : BasePagingViewModel<T, S, M>> : BaseFragment<VB, S, VM>() {

    private lateinit var _adapter: BaseAdapter<M, VH>
    private lateinit var _recyclerView: RecyclerView
    protected val adapter
        get() = _adapter
    protected val recyclerView
        get() = _recyclerView

    protected abstract fun initAdapterAndRecyclerView(): Pair<BaseAdapter<M, VH>, RecyclerView>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapterAndRecyclerView().apply {
            _adapter = first
            _recyclerView = second
        }
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dx <= 0 && dy <= 0) return

                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (lastVisibleItemPosition >= totalItemCount - 3) {
                    viewModel.loadNextPage()
                }
            }
        })
        viewModel.loadFirstPage()
    }

    override fun setupObservers() {
        super.setupObservers()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    viewModel.pagingState.collect { state ->
                        handlePagingState(state)
                    }
                }
            }
        }
    }

    protected abstract fun setLightLoading(isLoading: Boolean)

    protected open fun handlePagingState(state: BasePagingState<*>){
        when(state){
            is BasePagingState.LightLoading -> setLightLoading(state.isLoading)
            is BasePagingState.OnItemsLoad<*> -> {
                @Suppress("UNCHECKED_CAST")
                setupList(state.items as List<M>)
            }
            is BasePagingState.ItemsIsEmpty -> showEmptyPlaceholder()
            is BasePagingState.OnLoadError -> setErrorPlaceholder(state.error)
        }
    }

    protected abstract fun showEmptyPlaceholder()
    protected abstract fun setErrorPlaceholder(error: String)

    protected open fun setupList(items: List<M>){
        adapter.submitList(items)
    }

}