package es.jshcd.android.rssreader.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage

@Composable
fun PhotoScreen(
    focusedImageTitle: String,
    focusedImage: String?,
    onArrowBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onArrowBackClick()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Text(
                        text = focusedImageTitle,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                focusedImage?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "Focused image",
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun PhotoScreenPreview() {
    PhotoScreen(
        focusedImageTitle = "Titular",
        focusedImage = "https://www.abc.es/xlsemanal/wp-content/uploads/sites/5/2023/08/libro-dan-lyons-poder-de-guardar-silecio-callarse-exito.a.jpg",
        onArrowBackClick = { }
    )
}