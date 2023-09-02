package es.jshcd.android.rssreader

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.volley.toolbox.Volley
import es.jshcd.android.rssreader.ui.ACTION_SETTINGS
import es.jshcd.android.rssreader.ui.ROUTE_MAIN
import es.jshcd.android.rssreader.ui.ROUTE_SETTINGS
import es.jshcd.android.rssreader.ui.screen.RSSReaderMain
import es.jshcd.android.rssreader.ui.screen.SettingsScreen
import es.jshcd.android.rssreader.ui.theme.RSSReaderTheme
import es.jshcd.android.rssreader.ui.viewmodel.NewsViewModel
import es.jshcd.android.rssreader.ui.viewmodel.SettingsViewModel


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val newsViewModel = NewsViewModel()
        val settingsViewModel = SettingsViewModel()

        val requestQueue = Volley.newRequestQueue(applicationContext)

        setContent {
            val newsUiState = newsViewModel.uiState.collectAsState()
            val settingsUiState = settingsViewModel.uiState.collectAsState()
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = ROUTE_MAIN
            ) {
                composable(ROUTE_SETTINGS) {
                    RSSReaderTheme {
                        SettingsScreen(
                            state = settingsUiState.value,
                            onValueChange = {
                                settingsViewModel.updateSetting(it)
                            },
                            onArrowBackClick = { navController.navigateUp() }
                        )
                    }
                }
                composable(ROUTE_MAIN) {

                    newsViewModel.updateNews(
                        isNetworkAvailable = isNetworkAvailable(),
                        lFeedUrl = settingsUiState.value.dataSource,
                        lQueue = requestQueue,
                        onNoNetworkAvailable = {
                            Toast.makeText(
                                applicationContext, applicationContext.resources.getString(R.string.no_network_connection),
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        onRequestError = {
                            Toast.makeText(applicationContext, getString(R.string.request_failed, it.networkResponse.toString()), Toast.LENGTH_LONG).show()
                        }
                    )

                    RSSReaderTheme {
                        RSSReaderMain(
                            headlines = newsUiState.value.newsDtos,
                            focusedImage = newsUiState.value.focusedImage,
                            onActionButtonClick = {
                                when(it) {
                                    ACTION_SETTINGS -> navController.navigate(ROUTE_SETTINGS)
                                }
                            },
                            onShareButtonClick = { link ->
                                newsViewModel.shareLink(
                                    link = link,
                                    onShareIntentReady = { shareIntent ->
                                        applicationContext.startActivity(
                                            Intent.createChooser(shareIntent, getString(R.string.share_link_using)).addFlags(
                                                Intent.FLAG_ACTIVITY_NEW_TASK
                                            )
                                        )
                                    }
                                )
                            },
                            onHeadlineClick = { link ->
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                startActivity(browserIntent)
                            },
                            onImageClick = { imageUri ->
                                newsViewModel.updateFocusedImage(imageUri)
                            },
                            onCloseImageClick = {
                                newsViewModel.updateFocusedImage(null)
                            }
                        )
                    }
                }
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager: ConnectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}