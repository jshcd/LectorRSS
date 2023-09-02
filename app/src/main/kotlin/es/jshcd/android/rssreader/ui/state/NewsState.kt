package es.jshcd.android.rssreader.ui.state

import es.jshcd.android.rssreader.dto.NewsDto

data class NewsState(
    val newsDtos: List<NewsDto>,
    val focusedImage: String? = null
)
