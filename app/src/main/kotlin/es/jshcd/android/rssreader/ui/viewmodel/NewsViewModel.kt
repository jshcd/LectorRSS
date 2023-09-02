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
import kotlinx.coroutines.launch
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import java.io.IOException
import java.io.StringReader
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException


class NewsViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(NewsState(emptyList()))
    val uiState: StateFlow<NewsState> = _uiState.asStateFlow()

    fun updateNews(
        isNetworkAvailable: Boolean,
        lFeedUrl: String,
        lQueue: RequestQueue,
        onNoNetworkAvailable: () -> Unit,
        onRequestError: (VolleyError) -> Unit
    ) {
        if (!isNetworkAvailable) {
            onNoNetworkAvailable()
        }
        lQueue.add(
            StringRequest(
                Request.Method.GET,
                lFeedUrl,
                { response ->
                    parse(response)
                },
                { aError ->
                    onRequestError(aError)
                }
            )
        )
    }

    private fun parse(aXml: String) {
        val stringReader = StringReader(aXml)
        val inputSource = InputSource(stringReader)
        val factory: DocumentBuilderFactory
        val builder: DocumentBuilder
        try {
            factory = DocumentBuilderFactory.newInstance()
            builder = factory.newDocumentBuilder()
            val d = builder.parse(inputSource)
            val nodeList = d.getElementsByTagName("item")
            // System.out.println("The feed has " + nodeList.getLength() +
            // " items");

            val newsList = mutableListOf<NewsDto>()

            for (i in 0 until nodeList.length) {
                val nNode = nodeList.item(i)
                var newsDto: NewsDto
                val childNodes = nNode.childNodes

                var title = ""
                var description = ""
                var link = ""
                var content = ""
                var imageUrl = ""
                var pubDate = ""

                // System.out.println(childNodes.getLength());
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
                        //val attributes = mNode.attributes
                        //val url = attributes.getNamedItem("url")
                        val mediaContent = mNode.childNodes
                        for (k in 0 until mediaContent.length) {
                            val mediaItem = mediaContent.item(k)
                            if (mediaItem.nodeName.compareTo("media:thumbnail") == 0) {
                                val mediaItemAttributes = mediaItem.attributes
                                val url = mediaItemAttributes.getNamedItem("url")
                                imageUrl = url.textContent
                            }
                        }

                        // System.out.println(url.getTextContent());
                    } else if (mNode.nodeName == "pubDate") {
                        pubDate = mNode.textContent
                    }
                }
                newsDto = NewsDto(
                    id = i,
                    title = title,
                    description = description,
                    link = link,
                    content = content,
                    imageUrl = imageUrl,
                    pubDate = pubDate
                )
                newsList.add(newsDto)
            }
            viewModelScope.launch {
                _uiState.emit(uiState.value.copy(newsDtos = newsList))
            }
        } catch (e: SAXException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ParserConfigurationException) {
            e.printStackTrace()
        }
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