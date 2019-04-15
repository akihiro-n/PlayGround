package com.example.akihiro.playground

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ItemUseCaseImpl(private val repository: ItemRepository) : ItemUseCase {

    companion object {
        private const val PER_PAGE = 20
    }

    /**
     * 最新の記事一覧を取得する
     * @param page
     * @return
     */
    override fun fetchNewItems(page: Int): Single<List<Item>> {
        return repository
            .getItems(page, PER_PAGE, null)
            .subscribeOn(Schedulers.io())
    }

    /**
     * queryの文字列で記事を検索し一覧で取得
     * @param page
     * @param query
     * @return
     */
    override fun fetchItemsForQuery(page: Int, query: String): Single<List<Item>> {
        return repository
            .getItems(page, PER_PAGE, query)
            .subscribeOn(Schedulers.io())
    }
}