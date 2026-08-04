package es.jshcd.android.rssreader.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
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
            pushStringAnnotation(tag = "URL", annotation = email)
            withStyle(
                style = SpanStyle(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(email)
            }
            pop()
            append(contactDescription.substring(startIndex + email.length))
        } else {
            append(contactDescription)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onArrowBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
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
                ClickableText(
                    text = annotatedString,
                    style = TextStyle(fontSize = 16.sp),
                    onClick = { offset ->
                        annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                            .firstOrNull()?.let { annotation ->
                                onEmailClick(annotation.item)
                            }
                    }
                )
            }
        }
    )
}
