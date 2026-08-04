package es.jshcd.android.rssreader.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.jshcd.android.rssreader.R

@Composable
fun ContactScreen(
    onArrowBackClick: () -> Unit,
    onEmailClick: (String) -> Unit
) {
    val email = "sanchezhernandezjavier@gmail.com"
    val contactDescription = stringResource(id = R.string.contact_description, email)
    
    val annotatedString = buildAnnotatedString {
        val startIndex = contactDescription.indexOf(email)
        if (startIndex != -1) {
            append(contactDescription.substring(0, startIndex))
            
            val link = LinkAnnotation.Clickable(
                tag = "URL",
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                ),
                linkInteractionListener = {
                    onEmailClick(email)
                }
            )
            
            withLink(link) {
                append(email)
            }
            
            append(contactDescription.substring(startIndex + email.length))
        } else {
            append(contactDescription)
        }
    }

    Scaffold(
        modifier = Modifier.padding(WindowInsets.statusBars.asPaddingValues()),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onArrowBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.contact_screen_title))
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = annotatedString,
                    style = TextStyle(fontSize = 16.sp)
                )
            }
        }
    )
}
