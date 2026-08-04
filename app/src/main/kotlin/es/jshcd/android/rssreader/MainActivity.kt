package es.jshcd.android.rssreader

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.volley.toolbox.Volley
import es.jshcd.android.rssreader.ui.ACTION_SETTINGS
import es.jshcd.android.rssreader.ui.ACTION_CONTACT
import es.jshcd.android.rssreader.ui.ROUTE_MAIN
import es.jshcd.android.rssreader.ui.ROUTE_PHOTO
import es.jshcd.android.rssreader.ui.ROUTE_SETTINGS
import es.jshcd.android.rssreader.ui.ROUTE_CONTACT
import es.jshcd.android.rssreader.ui.screen.PhotoScreen
import es.jshcd.android.rssreader.ui.screen.RSSReaderMain
import es.jshcd.android.rssreader.ui.screen.SettingsScreen
import es.jshcd.android.rssreader.ui.screen.ContactScreen
import es.jshcd.android.rssreader.ui.theme.RSSReaderTheme
import es.jshcd.android.rssreader.ui.viewmodel.NewsViewModel
import es.jshcd.android.rssreader.ui.viewmodel.SettingsViewModel
import androidx.core.net.toUri


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val newsViewModel = NewsViewModel()
        val settingsViewModel = SettingsViewModel(application)

        val requestQueue = Volley.newRequestQueue(applicationContext)

        setContent {
            val newsUiState = newsViewModel.uiState.collectAsState()
            val settingsUiState = settingsViewModel.uiState.collectAsState()
            val navController = rememberNavController()

            RSSReaderTheme {
                NavHost(
                    navController = navController,
                    startDestination = ROUTE_MAIN
                ) {
                    composable(ROUTE_MAIN) {
                        androidx.compose.runtime.LaunchedEffect(settingsUiState.value.dataSources) {
                            newsViewModel.updateNews(
                                isNetworkAvailable = isNetworkAvailable(),
                                lFeedUrls = settingsUiState.value.dataSources,
                                lQueue = requestQueue,
                                onNoNetworkAvailable = {
                                    Toast.makeText(
                                        applicationContext,
                                        applicationContext.resources.getString(R.string.no_network_connection),
                                        Toast.LENGTH_LONG
                                    ).show()
                                },
                                onRequestError = {
                                    Toast.makeText(
                                        applicationContext,
                                        getString(
                                            R.string.request_failed,
                                            it.networkResponse.toString()
                                        ),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                        }
                        RSSReaderMain(
                            headlines = newsUiState.value.newsDtos,
                            onActionButtonClick = {
                                when (it) {
                                    ACTION_SETTINGS -> navController.navigate(ROUTE_SETTINGS)
                                    ACTION_CONTACT -> navController.navigate(ROUTE_CONTACT)
                                }
                            },
                            onShareButtonClick = { link ->
                                newsViewModel.shareLink(
                                    link = link,
                                    onShareIntentReady = { shareIntent ->
                                        applicationContext.startActivity(
                                            Intent.createChooser(
                                                shareIntent,
                                                getString(R.string.share_link_using)
                                            ).addFlags(
                                                Intent.FLAG_ACTIVITY_NEW_TASK
                                            )
                                        )
                                    }
                                )
                            },
                            onHeadlineClick = { link ->
                                val browserIntent = Intent(Intent.ACTION_VIEW, link.toUri())
                                startActivity(browserIntent)
                            },
                            onImageClick = {
                                newsViewModel.updateFocusedImage(it)
                                navController.navigate(ROUTE_PHOTO)
                            }
                        )
                    }
                    composable(ROUTE_PHOTO) {
                        val focusedIndex = newsUiState.value.focusedNews
                        if (focusedIndex >= 0) {
                            PhotoScreen(
                                focusedImageTitle = newsUiState.value.newsDtos[focusedIndex].title,
                                focusedImage = newsUiState.value.newsDtos[focusedIndex].imageUrl,
                                onArrowBackClick = {
                                    newsViewModel.updateFocusedImage(-1)
                                    navController.navigateUp()
                                }
                            )
                        }
                    }
                    composable(ROUTE_SETTINGS) {
                        SettingsScreen(
                            state = settingsUiState.value,
                            onAddClick = {
                                settingsViewModel.addDataSource(it)
                            },
                            onDeleteClick = {
                                settingsViewModel.removeDataSource(it)
                            },
                            onArrowBackClick = {
                                navController.navigateUp()
                            }
                        )
                    }
                    composable(ROUTE_CONTACT) {
                        ContactScreen(
                            onArrowBackClick = {
                                navController.navigateUp()
                            },
                            onEmailClick = { email ->
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:$email")
                                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_email_subject))
                                }
                                try {
                                    startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(this@MainActivity, "No email app found", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.activeNetwork
        } else {
            connectivityManager.allNetworks.firstOrNull {
                @Suppress("DEPRECATION")
                connectivityManager.getNetworkInfo(it)?.isConnected == true
            }
        } ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
