package es.jshcd.android.rssreader.ui.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import es.jshcd.android.rssreader.R
import es.jshcd.android.rssreader.dto.NewsDto

@Composable
fun HeadlineRow(
    headline: NewsDto,
    onShareButtonClick: (String) -> Unit,
    onHeadlineClick: (String) -> Unit,
    onImageClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1.0f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (headline.imageUrl.isNullOrEmpty()) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_image_24),
                        contentDescription = "Default image"
                    )
                } else {
                    AsyncImage(
                        modifier = Modifier.clickable(
                            onClick = { onImageClick(headline.imageUrl) }
                        ),
                        model = headline.imageUrl,
                        contentDescription = "Image referring to the news",
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(2.5f)
                    .padding(8.dp)
                    .clickable(
                        onClick = { onHeadlineClick(headline.link) }
                    )
            ) {
                Text(
                    text = headline.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = headline.description,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = headline.pubDate
            )
            if (headline.link.isNotEmpty()) {
                IconButton(onClick = { onShareButtonClick(headline.link) }) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "Share"
                    )
                }
            }
        }
        Divider(color = Color.Gray, thickness = 1.dp)
    }
}

@Preview
@Composable
private fun HeadlineRowPreview() {
    val headline = NewsDto(
        id = 1,
        title = "Reacciones, pactos y resultados del 23-J, en directo | Sánchez contesta a Feijóo que no se reunirá con él hasta que el Rey designe candidato a la investidura",
        description = "EL PAÍS ofrece de forma gratuita la última hora de las reacciones al 23-J. Si quieres apoyar nuestro periodismo",
        content = "Aquí van mazo de cosas con texto html <p>parrafos</p>, <strong>negritas</strong> y demás...",
        imageUrl = "https://cloudfront-eu-central-1.images.arcpublishing.com/prisa/PJIEPYAN3NF6HFKRAUQADPH3TY",
        link = "https://elpais.com/espana/elecciones-generales/2023-07-30/reacciones-pactos-y-resultados-del-23j-en-directo.html",
        pubDate = "Sat, 26 Aug 2023 03:15:00 GMT"
    )
    HeadlineRow(
        headline = headline,
        onShareButtonClick = {_-> },
        onHeadlineClick = { _-> },
        onImageClick = { _-> }
    )
}