package com.app.base

sealed class BasePagingState<T>: BaseState() {

    class OnItemsLoad<T>(val items: List<T>) : BasePagingState<T>()
    class ItemsIsEmpty<M>(): BasePagingState<M>()
    class LightLoading<M>(val isLoading: Boolean) : BasePagingState<M>()
    class OnLoadError<M>(val error: String): BasePagingState<M>()

}