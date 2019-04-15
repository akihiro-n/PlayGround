package com.example.akihiro.playground

import io.reactivex.Single

interface ItemUseCase {

    /**
     * 最新の記事一覧を取得する
     * @param page
     * @return
     */
    fun fetchNewItems(page: Int): Single<List<Item>>

    /**
     * queryの文字列で記事を検索し一覧で取得
     * @param page
     * @param query
     * @return
     */
    fun fetchItemsForQuery(page: Int, query: String): Single<List<Item>>
}