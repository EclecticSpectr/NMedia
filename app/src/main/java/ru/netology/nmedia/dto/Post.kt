package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean = false,
    val likes: Int = 999,
    val shares: Int = 999999,
    val views: Int = 0,
//    val video: String? = null
    val video: String? = "https://www.youtube.com/watch?v=WhWc3b3KhnY"
)