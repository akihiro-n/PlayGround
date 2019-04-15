package com.example.akihiro.playground

import io.reactivex.Single

interface ItemRepository {
    fun getItems(page: Int, perPage: Int, query: String?): Single<List<Item>>
}