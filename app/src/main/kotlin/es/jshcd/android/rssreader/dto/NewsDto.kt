package es.jshcd.android.rssreader.dto

data class NewsDto(
    val id: Int,
    val title: String,
    val description: String,
    val content: String,
    val imageUrl: String?,
    val link: String,
    val pubDate: String
)
