package es.jshcd.android.rssreader.ui.viewmodel

import android.content.Intent
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.Locale
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class NewsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NewsState(emptyList()))
    val uiState: StateFlow<NewsState> = _uiState.asStateFlow()

    private val rssDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)

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

        Log.d(TAG, "Updating news from ${lFeedUrls.size} sources")
        
        // Clear current news before starting a full refresh
        _uiState.update { it.copy(newsDtos = emptyList()) }

        lFeedUrls.forEach { url ->
            val request = StringRequest(
                Request.Method.GET,
                url,
                { response ->
                    val newsFromSource = parse(String(response.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8))
                    Log.d(TAG, "Fetched ${newsFromSource.size} items from $url")
                    
                    _uiState.update { state ->
                        // Combine, sort by date descending, and re-assign IDs
                        val combined = (state.newsDtos + newsFromSource)
                            .sortedByDescending { parseDate(it.pubDate) }
                            .mapIndexed { index, item -> item.copy(id = index) }
                        
                        state.copy(newsDtos = combined)
                    }
                },
                { error ->
                    Log.e(TAG, "Error fetching $url: ${error.message}")
                    onRequestError(error)
                }
            )
            lQueue.add(request)
        }
    }

    private fun parseDate(dateStr: String): Long {
        return try {
            rssDateFormat.parse(dateStr.trim())?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    private fun parse(aXml: String): List<NewsDto> {
        val newsList = mutableListOf<NewsDto>()
        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val d = builder.parse(InputSource(StringReader(aXml)))
            val channelTitle = d.getElementsByTagName("title").item(0).textContent
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
                    when (mNode.nodeName) {
                        "title" -> title = mNode.textContent
                        "description" -> description = mNode.textContent
                        "link" -> link = mNode.textContent
                        "content:encoded" -> content = mNode.textContent
                        "pubDate" -> pubDate = mNode.textContent
                        "media:content", "media:thumbnail" -> {
                            val urlAttr = mNode.attributes?.getNamedItem("url")
                            if (urlAttr != null) {
                                imageUrl = urlAttr.textContent
                            } else {
                                // Check nested media:thumbnail
                                val mediaChildren = mNode.childNodes
                                for (k in 0 until mediaChildren.length) {
                                    if (mediaChildren.item(k).nodeName == "media:thumbnail") {
                                        imageUrl = mediaChildren.item(k).attributes?.getNamedItem("url")?.textContent ?: ""
                                    }
                                }
                            }
                        }
                    }
                }
                newsList.add(
                    NewsDto(
                        channelTitle = channelTitle,
                        id = 0, // ID will be set after sorting
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
            Log.e(TAG, "Parsing error: ${e.message}")
        }
        return newsList
    }

    fun shareLink(link: String, onShareIntentReady: (Intent) -> Unit) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, link)
        }
        onShareIntentReady(shareIntent)
    }

    fun updateFocusedImage(focusedIndex: Int) {
        viewModelScope.launch {
            _uiState.emit(uiState.value.copy(focusedNews = focusedIndex))
        }
    }

    companion object {
        private const val TAG = "NewsViewModel"
    }
}
