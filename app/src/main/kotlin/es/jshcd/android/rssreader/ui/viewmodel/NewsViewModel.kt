package es.jshcd.android.rssreader.ui.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import es.jshcd.android.rssreader.dto.NewsDto
import es.jshcd.android.rssreader.ui.state.NewsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class NewsViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(NewsState(emptyList()))
    val uiState: StateFlow<NewsState> = _uiState.asStateFlow()

    fun updateNews(
        isNetworkAvailable: Boolean,
        lFeedUrls: List<String>,
        lQueue: RequestQueue,
        onNoNetworkAvailable: () -> Unit,
        onRequestError: (VolleyError) -> Unit
    ) {
        if (!isNetworkAvailable) {
            onNoNetworkAvailable()
            return
        }

        _uiState.update { it.copy(newsDtos = emptyList()) }

        lFeedUrls.forEach { url ->
            lQueue.add(
                StringRequest(
                    Request.Method.GET,
                    url,
                    { response ->
                        val news = parse(String(response.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8))
                        _uiState.update { state ->
                            val currentSize = state.newsDtos.size
                            val newsWithUpdatedIds = news.mapIndexed { index, newsDto ->
                                newsDto.copy(id = currentSize + index)
                            }
                            state.copy(newsDtos = state.newsDtos + newsWithUpdatedIds)
                        }
                    },
                    { aError ->
                        onRequestError(aError)
                    }
                )
            )
        }
    }

    private fun parse(aXml: String): List<NewsDto> {
        val stringReader = StringReader(aXml)
        val inputSource = InputSource(stringReader)
        val factory: DocumentBuilderFactory
        val builder: DocumentBuilder
        val newsList = mutableListOf<NewsDto>()
        try {
            factory = DocumentBuilderFactory.newInstance()
            builder = factory.newDocumentBuilder()
            val d = builder.parse(inputSource)
            val nodeList = d.getElementsByTagName("item")

            for (i in 0 until nodeList.length) {
                val nNode = nodeList.item(i)
                val childNodes = nNode.childNodes

                var title = ""
                var description = ""
                var link = ""
                var content = ""
                var imageUrl = ""
                var pubDate = ""

                for (j in 0 until childNodes.length) {
                    val mNode = childNodes.item(j)
                    if (mNode.nodeName.compareTo("title") == 0) {
                        title = mNode.textContent
                    } else if (mNode.nodeName.compareTo("description") == 0) {
                        description = mNode.textContent
                    } else if (mNode.nodeName.compareTo("link") == 0) {
                        link = mNode.textContent
                    } else if (mNode.nodeName.compareTo("content:encoded") == 0) {
                        content = mNode.textContent
                    } else if (mNode.nodeName.compareTo("media:content") == 0) {
                        val mediaContent = mNode.childNodes
                        for (k in 0 until mediaContent.length) {
                            val mediaItem = mediaContent.item(k)
                            if (mediaItem.nodeName.compareTo("media:thumbnail") == 0) {
                                val mediaItemAttributes = mediaItem.attributes
                                val url = mediaItemAttributes.getNamedItem("url")
                                imageUrl = url.textContent
                            }
                        }
                    } else if (mNode.nodeName == "pubDate") {
                        pubDate = mNode.textContent
                    }
                }
                newsList.add(
                    NewsDto(
                        id = i,
                        title = title,
                        description = description,
                        link = link,
                        content = content,
                        imageUrl = imageUrl,
                        pubDate = pubDate
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return newsList
    }

    fun shareLink(
        link: String,
        onShareIntentReady: (Intent) -> Unit
    ) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)
        onShareIntentReady(shareIntent)
    }

    fun updateFocusedImage(focusedIndex: Int) {
        viewModelScope.launch {
            _uiState.emit(uiState.value.copy(focusedNews = focusedIndex))
        }
    }
}
