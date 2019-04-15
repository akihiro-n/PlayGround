package com.example.akihiro.playground

import io.reactivex.Single

class ItemRepositoryImpl(private val api: API) : ItemRepository {
    override fun getItems(page: Int, perPage: Int, query: String?): Single<List<Item>> {
        return api.getItems(page, perPage, query)
    }
}