package com.example.akihiro.playground

import flux.Action
import flux.ErrorAction

sealed class ItemAction: Action {
    object Initialize: ItemAction()
    class FetchNewItems(val item: List<Item>): ItemAction()
    class FetchItemsForQuery(val item: List<Item>): ItemAction()
    class ErrorFetchNewItems(override val throwable: Throwable): ErrorAction
    class ErrorFetchItemsForQuery(override val throwable: Throwable): ErrorAction
}