package com.example.akihiro.playground

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface API {

    /**
     * 記事一覧を取得する
     * @param page
     * @param perPage
     * @param query
     * @return
     */
    @GET("/api/v2/items")
    fun getItems(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("query") query: String?): Single<List<Item>>
}