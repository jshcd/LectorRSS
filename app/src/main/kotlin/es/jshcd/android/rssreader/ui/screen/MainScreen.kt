package es.jshcd.android.rssreader.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import es.jshcd.android.rssreader.R
import es.jshcd.android.rssreader.dto.NewsDto
import es.jshcd.android.rssreader.ui.ACTION_SETTINGS
import es.jshcd.android.rssreader.ui.ACTION_CONTACT
import es.jshcd.android.rssreader.ui.screen.components.HeadlineRow
import es.jshcd.android.rssreader.ui.theme.RSSReaderTheme

@Composable
fun RSSReaderMain(
    headlines: List<NewsDto>,
    onActionButtonClick: (String) -> Unit,
    onShareButtonClick: (String) -> Unit,
    onHeadlineClick: (String) -> Unit,
    onImageClick: (Int) -> Unit
) {
    val borderPadding = 5.dp

    // A surface container using the 'background' color from the theme
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                actions = {
                    IconButton(
                        onClick = {
                            onActionButtonClick(ACTION_CONTACT)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = stringResource(
                                id = R.string.action_contact
                            )
                        )
                    }
                    IconButton(
                        onClick = {
                            onActionButtonClick(ACTION_SETTINGS)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(
                                id = R.string.action_settings
                            )
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Box {
                LazyColumn(
                    modifier = Modifier.padding(
                        top = borderPadding,
                        bottom = paddingValues.calculateBottomPadding(),
                        start = borderPadding,
                        end = borderPadding
                    )
                ) {
                    items(headlines.size) {
                        HeadlineRow(
                            headline = headlines[it],
                            onShareButtonClick = onShareButtonClick,
                            onHeadlineClick = onHeadlineClick,
                            onImageClick = onImageClick
                        )
                    }
                }
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun GreetingPreview() {
    RSSReaderTheme {
        RSSReaderMain(
            headlines = listOf(
                NewsDto(
                    id = 1,
                    title = "Reacciones, pactos y resultados del 23-J, en directo | Sánchez contesta a Feijóo que no se reunirá con él hasta que el Rey designe candidato a la investidura",
                    description = "EL PAÍS ofrece de forma gratuita la última hora de las reacciones al 23-J. Si quieres apoyar nuestro periodismo",
                    content = "Aquí van mazo de cosas con texto html <p>parrafos</p>, <strong>negritas</strong> y demás...",
                    imageUrl = "https://cloudfront-eu-central-1.images.arcpublishing.com/prisa/PJIEPYAN3NF6HFKRAUQADPH3TY",
                    link = "https://elpais.com/espana/elecciones-generales/2023-07-30/reacciones-pactos-y-resultados-del-23j-en-directo.html",
                    pubDate = "Sat, 26 Aug 2023 03:15:00 GMT"
                ),
                NewsDto(
                    id = 2,
                    title = "Reacciones, pactos y resultados del 23-J, en directo | Sánchez contesta a Feijóo que no se reunirá con él hasta que el Rey designe candidato a la investidura",
                    description = "EL PAÍS ofrece de forma gratuita la última hora de las reacciones al 23-J. Si quieres apoyar nuestro periodismo",
                    content = "Aquí van mazo de cosas con texto html <p>parrafos</p>, <strong>negritas</strong> y demás...",
                    imageUrl = "https://cloudfront-eu-central-1.images.arcpublishing.com/prisa/PJIEPYAN3NF6HFKRAUQADPH3TY",
                    link = "https://elpais.com/espana/elecciones-generales/2023-07-30/reacciones-pactos-y-resultados-del-23j-en-directo.html",
                    pubDate = "Sat, 26 Aug 2023 03:15:00 GMT"
                ),
                NewsDto(
                    id = 3,
                    title = "Reacciones, pactos y resultados del 23-J, en directo | Sánchez contesta a Feijóo que no se reunirá con él hasta que el Rey designe candidato a la investidura",
                    description = "EL PAÍS ofrece de forma gratuita la última hora de las reacciones al 23-J. Si quieres apoyar nuestro periodismo",
                    content = "Aquí van mazo de cosas con texto html <p>parrafos</p>, <strong>negritas</strong> y demás...",
                    imageUrl = "https://cloudfront-eu-central-1.images.arcpublishing.com/prisa/PJIEPYAN3NF6HFKRAUQADPH3TY",
                    link = "https://elpais.com/espana/elecciones-generales/2023-07-30/reacciones-pactos-y-resultados-del-23j-en-directo.html",
                    pubDate = "Sat, 26 Aug 2023 03:15:00 GMT"
                ),
                NewsDto(
                    id = 4,
                    title = "Reacciones, pactos y resultados del 23-J, en directo | Sánchez contesta a Feijóo que no se reunirá con él hasta que el Rey designe candidato a la investidura",
                    description = "EL PAÍS ofrece de forma gratuita la última hora de las reacciones al 23-J. Si quieres apoyar nuestro periodismo",
                    content = "Aquí van mazo de cosas con texto html <p>parrafos</p>, <strong>negritas</strong> y demás...",
                    imageUrl = "https://cloudfront-eu-central-1.images.arcpublishing.com/prisa/PJIEPYAN3NF6HFKRAUQADPH3TY",
                    link = "https://elpais.com/espana/elecciones-generales/2023-07-30/reacciones-pactos-y-resultados-del-23j-en-directo.html",
                    pubDate = "Sat, 26 Aug 2023 03:15:00 GMT"
                ),
                NewsDto(
                    id = 5,
                    title = "Reacciones, pactos y resultados del 23-J, en directo | Sánchez contesta a Feijóo que no se reunirá con él hasta que el Rey designe candidato a la investidura",
                    description = "EL PAÍS ofrece de forma gratuita la última hora de las reacciones al 23-J. Si quieres apoyar nuestro periodismo",
                    content = "Aquí van mazo de cosas con texto html <p>parrafos</p>, <strong>negritas</strong> y demás...",
                    imageUrl = "https://cloudfront-eu-central-1.images.arcpublishing.com/prisa/PJIEPYAN3NF6HFKRAUQADPH3TY",
                    link = "https://elpais.com/espana/elecciones-generales/2023-07-30/reacciones-pactos-y-resultados-del-23j-en-directo.html",
                    pubDate = "Sat, 26 Aug 2023 03:15:00 GMT"
                ),
                NewsDto(
                    id = 6,
                    title = "Reacciones, pactos y resultados del 23-J, en directo | Sánchez contesta a Feijóo que no se reunirá con él hasta que el Rey designe candidato a la investidura",
                    description = "EL PAÍS ofrece de forma gratuita la última hora de las reacciones al 23-J. Si quieres apoyar nuestro periodismo",
                    content = "Aquí van mazo de cosas con texto html <p>parrafos</p>, <strong>negritas</strong> y demás...",
                    imageUrl = "https://cloudfront-eu-central-1.images.arcpublishing.com/prisa/PJIEPYAN3NF6HFKRAUQADPH3TY",
                    link = "https://elpais.com/espana/elecciones-generales/2023-07-30/reacciones-pactos-y-resultados-del-23j-en-directo.html",
                    pubDate = "Sat, 26 Aug 2023 03:15:00 GMT"
                ),
                NewsDto(
                    id = 7,
                    title = "Reacciones, pactos y resultados del 23-J, en directo | Sánchez contesta a Feijóo que no se reunirá con él hasta que el Rey designe candidato a la investidura",
                    description = "EL PAÍS ofrece de forma gratuita la última hora de las reacciones al 23-J. Si quieres apoyar nuestro periodismo",
                    content = "Aquí van mazo de cosas con texto html <p>parrafos</p>, <strong>negritas</strong> y demás...",
                    imageUrl = "https://cloudfront-eu-central-1.images.arcpublishing.com/prisa/PJIEPYAN3NF6HFKRAUQADPH3TY",
                    link = "https://elpais.com/espana/elecciones-generales/2023-07-30/reacciones-pactos-y-resultados-del-23j-en-directo.html",
                    pubDate = "Sat, 26 Aug 2023 03:15:00 GMT"
                ),
                NewsDto(
                    id = 8,
                    title = "Reacciones, pactos y resultados del 23-J, en directo | Sánchez contesta a Feijóo que no se reunirá con él hasta que el Rey designe candidato a la investidura",
                    description = "EL PAÍS ofrece de forma gratuita la última hora de las reacciones al 23-J. Si quieres apoyar nuestro periodismo",
                    content = "Aquí van mazo de cosas con texto html <p>parrafos</p>, <strong>negritas</strong> y demás...",
                    imageUrl = "https://cloudfront-eu-central-1.images.arcpublishing.com/prisa/PJIEPYAN3NF6HFKRAUQADPH3TY",
                    link = "https://elpais.com/espana/elecciones-generales/2023-07-30/reacciones-pactos-y-resultados-del-23j-en-directo.html",
                    pubDate = "Sat, 26 Aug 2023 03:15:00 GMT"
                )
            ),
            onActionButtonClick = { _ -> },
            onShareButtonClick = { _ -> },
            onHeadlineClick = { _ -> },
            onImageClick = { _ -> }
        )
    }
}

@PreviewLightDark
@Composable
private fun GreetingWithFocusedImagePreview() {
    RSSReaderTheme {
        RSSReaderMain(
            headlines = listOf(
                NewsDto(
                    id = 1,
                    title = "Reacciones, pactos y resultados del 23-J, en directo | Sánchez contesta a Feijóo que no se reunirá con él hasta que el Rey designe candidato a la investidura",
                    description = "EL PAÍS ofrece de forma gratuita la última hora de las reacciones al 23-J. Si quieres apoyar nuestro periodismo",
                    content = "Aquí van mazo de cosas con texto html <p>parrafos</p>, <strong>negritas</strong> y demás...",
                    imageUrl = "https://www.abc.es/xlsemanal/wp-content/uploads/sites/5/2023/08/libro-dan-lyons-poder-de-guardar-silecio-callarse-exito.a.jpg",
                    link = "https://elpais.com/espana/elecciones-generales/2023-07-30/reacciones-pactos-y-resultados-del-23j-en-directo.html",
                    pubDate = "Sat, 26 Aug 2023 03:15:00 GMT"
                ),
                NewsDto(
                    id = 2,
                    title = "Reacciones, pactos y resultados del 23-J, en directo | Sánchez contesta a Feijóo que no se reunirá con él hasta que el Rey designe candidato a la investidura",
                    description = "EL PAÍS ofrece de forma gratuita la última hora de las reacciones al 23-J. Si quieres apoyar nuestro periodismo",
                    content = "Aquí van mazo de cosas con texto html <p>parrafos</p>, <strong>negritas</strong> y demás...",
                    imageUrl = "https://cloudfront-eu-central-1.images.arcpublishing.com/prisa/PJIEPYAN3NF6HFKRAUQADPH3TY",
                    link = "https://elpais.com/espana/elecciones-generales/2023-07-30/reacciones-pactos-y-resultados-del-23j-en-directo.html",
                    pubDate = "Sat, 26 Aug 2023 03:15:00 GMT"
                ),
                NewsDto(
                    id = 3,
                    title = "Reacciones, pactos y resultados del 23-J, en directo | Sánchez contesta a Feijóo que no se reunirá con él hasta que el Rey designe candidato a la investidura",
                    description = "EL PAÍS ofrece de forma gratuita la última hora de las reacciones al 23-J. Si quieres apoyar nuestro periodismo",
                    content = "Aquí van mazo de cosas con texto html <p>parrafos</p>, <strong>negritas</strong> y demás...",
                    imageUrl = "https://cloudfront-eu-central-1.images.arcpublishing.com/prisa/PJIEPYAN3NF6HFKRAUQADPH3TY",
                    link = "https://elpais.com/espana/elecciones-generales/2023-07-30/reacciones-pactos-y-resultados-del-23j-en-directo.html",
                    pubDate = "Sat, 26 Aug 2023 03:15:00 GMT"
                ),
                NewsDto(
                    id = 4,
                    title = "Reacciones, pactos y resultados del 23-J, en directo | Sánchez contesta a Feijóo que no se reunirá con él hasta que el Rey designe candidato a la investidura",
                    description = "EL PAÍS ofrece de forma gratuita la última hora de las reacciones al 23-J. Si quieres apoyar nuestro periodismo",
                    content = "Aquí van mazo de cosas con texto html <p>parrafos</p>, <strong>negritas</strong> y demás...",
                    imageUrl = "https://cloudfront-eu-central-1.images.arcpublishing.com/prisa/PJIEPYAN3NF6HFKRAUQADPH3TY",
                    link = "https://elpais.com/espana/elecciones-generales/2023-07-30/reacciones-pactos-y-resultados-del-23j-en-directo.html",
                    pubDate = "Sat, 26 Aug 2023 03:15:00 GMT"
                ),
                NewsDto(
                    id = 5,
                    title = "Reacciones, pactos y resultados del 23-J, en directo | Sánchez contesta a Feijóo que no se reunirá con él hasta que el Rey designe candidato a la investidura",
                    description = "EL PAÍS ofrece de forma gratuita la última hora de las reacciones al 23-J. Si quieres apoyar nuestro periodismo",
                    content = "Aquí van mazo de cosas con texto html <p>parrafos</p>, <strong>negritas</strong> y demás...",
                    imageUrl = "https://cloudfront-eu-central-1.images.arcpublishing.com/prisa/PJIEPYAN3NF6HFKRAUQADPH3TY",
                    link = "https://elpais.com/espana/elecciones-generales/2023-07-30/reacciones-pactos-y-resultados-del-23j-en-directo.html",
                    pubDate = "Sat, 26 Aug 2023 03:15:00 GMT"
                ),
                NewsDto(
                    id = 6,
                    title = "Reacciones, pactos y resultados del 23-J, en directo | Sánchez contesta a Feijóo que no se reunirá con él hasta que el Rey designe candidato a la investidura",
                    description = "EL PAÍS ofrece de forma gratuita la última hora de las reacciones al 23-J. Si quieres apoyar nuestro periodismo",
                    content = "Aquí van mazo de cosas con texto html <p>parrafos</p>, <strong>negritas</strong> y demás...",
                    imageUrl = "https://cloudfront-eu-central-1.images.arcpublishing.com/prisa/PJIEPYAN3NF6HFKRAUQADPH3TY",
                    link = "https://elpais.com/espana/elecciones-generales/2023-07-30/reacciones-pactos-y-resultados-del-23j-en-directo.html",
                    pubDate = "Sat, 26 Aug 2023 03:15:00 GMT"
                ),
                NewsDto(
                    id = 7,
                    title = "Reacciones, pactos y resultados del 23-J, en directo | Sánchez contesta a Feijóo que no se reunirá con él hasta que el Rey designe candidato a la investidura",
                    description = "EL PAÍS ofrece de forma gratuita la última hora de las reacciones al 23-J. Si quieres apoyar nuestro periodismo",
                    content = "Aquí van mazo de cosas con texto html <p>parrafos</p>, <strong>negritas</strong> y demás...",
                    imageUrl = "https://cloudfront-eu-central-1.images.arcpublishing.com/prisa/PJIEPYAN3NF6HFKRAUQADPH3TY",
                    link = "https://elpais.com/espana/elecciones-generales/2023-07-30/reacciones-pactos-y-resultados-del-23j-en-directo.html",
                    pubDate = "Sat, 26 Aug 2023 03:15:00 GMT"
                ),
                NewsDto(
                    id = 8,
                    title = "Reacciones, pactos y resultados del 23-J, en directo | Sánchez contesta a Feijóo que no se reunirá con él hasta que el Rey designe candidato a la investidura",
                    description = "EL PAÍS ofrece de forma gratuita la última hora de las reacciones al 23-J. Si quieres apoyar nuestro periodismo",
                    content = "Aquí van mazo de cosas con texto html <p>parrafos</p>, <strong>negritas</strong> y demás...",
                    imageUrl = "https://cloudfront-eu-central-1.images.arcpublishing.com/prisa/PJIEPYAN3NF6HFKRAUQADPH3TY",
                    link = "https://elpais.com/espana/elecciones-generales/2023-07-30/reacciones-pactos-y-resultados-del-23j-en-directo.html",
                    pubDate = "Sat, 26 Aug 2023 03:15:00 GMT"
                )
            ),
            onActionButtonClick = { _ -> },
            onShareButtonClick = { _ -> },
            onHeadlineClick = { _ -> },
            onImageClick = { _ -> }
        )
    }
}