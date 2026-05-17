package com.app.base

import com.app.base.models.BaseListResponse
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BasePagingViewModel<T, S : BaseState, M : BaseHolderModel>(initialState: S)
    :  BaseViewModel<S>(initialState) {

    protected var currentPage = 0
    protected var totalCount = 0
    protected val currentItems = mutableListOf<T>()
    protected var isLoading = false

    protected val _pagingState = MutableStateFlow<BasePagingState<M>>(BasePagingState.LightLoading(true))
    val pagingState = _pagingState

    protected suspend fun emitPagingState(state: BasePagingState<M>){
        _pagingState.emit(state)
    }

    protected abstract suspend fun getPage(page: Int): BaseListResponse<T>

    protected open fun loadPage(page: Int = currentPage) = withLoading({
        emitPagingState(BasePagingState.LightLoading(it))
    }) {
        isLoading = true
        try {
            getPage(page).apply {
                totalCount = count
                currentItems.addAll(items)
            }
            if (currentItems.isEmpty()){
                if (page == 0){
                    emitPagingState(BasePagingState.ItemsIsEmpty())
                }
            }else{
                emitPagingState(BasePagingState.OnItemsLoad(currentItems.map { map(it) }))
            }
        }catch (e: Exception){
            emitSimpleState(SimpleStates.Error(e.handleError()))
        }finally {
            isLoading = false
        }
    }

    open fun loadFirstPage() {
        if (isLoading) return
        currentPage = 0
        currentItems.clear()
        loadPage()
    }

    open fun loadNextPage(){
        if (haveMore() && !isLoading){
            loadPage(++currentPage)
        }
    }

    protected abstract fun map(domain: T): M

    protected open fun haveMore(): Boolean {
        return currentItems.size < totalCount
    }

}