package com.example.akihiro.playground

data class Item (
    val title: String = "",
    val body: String = "",
    val likesCount: Int = 0,
    val cratedAt: String = "",
    val updatedAt: String = "",
    val user: User = User()
) {
    val likesCountText: String
        get() = "likes $likesCount"
}