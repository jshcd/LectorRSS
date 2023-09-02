package es.jshcd.android.rssreader.ui.state

import es.jshcd.android.rssreader.dto.NewsDto

data class NewsState(
    val newsDtos: List<NewsDto>,
    val focusedNews: Int = -1
)
