package com.example.akihiro.playground

import flux.ActionCreator

class ItemActionCreator(private val useCase: ItemUseCase): ActionCreator() {

    /**
     * 状態の初期化
     */
    fun initialize() {
        dispatch(ItemAction.Initialize)
    }

    /**
     * 新着記事一覧を取得
     * @param page
     */
    fun fetchNewItems(page: Int) {
        useCase
            .fetchNewItems(page)
            .onSuccessDispatch(ItemAction::FetchNewItems)
            .onErrorDispatch(ItemAction::ErrorFetchNewItems)
            .subscribe()
    }

    /**
     * Queryのキーワードで記事を検索する
     * @param page
     * @param query
     */
    fun fetchItemsForQuery(page: Int, query: String) {
        useCase
            .fetchItemsForQuery(page, query)
            .onSuccessDispatch(ItemAction::FetchItemsForQuery)
            .onErrorDispatch(ItemAction::ErrorFetchItemsForQuery)
            .subscribe()
    }
}