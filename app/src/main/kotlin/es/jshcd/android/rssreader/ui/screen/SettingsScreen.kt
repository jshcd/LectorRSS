package es.jshcd.android.rssreader.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.jshcd.android.rssreader.R
import es.jshcd.android.rssreader.ui.state.SettingsState

val borderPadding = 5.dp

@Composable
fun SettingsScreen(
    state: SettingsState,
    onValueChange: (String) -> Unit,
    onArrowBackClick: () -> Unit
) {
    val text = remember { mutableStateOf(state.dataSource) }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                     IconButton(
                         onClick = {
                             onArrowBackClick()
                             onValueChange(text.value)
                         }
                     ) {
                         Icon(
                             imageVector = Icons.Default.ArrowBack,
                             contentDescription = "Back"
                         )
                     }
                },
                title = {
                    Text(text = stringResource(id = R.string.settings_screen_title))
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(
                    top = borderPadding,
                    bottom = paddingValues.calculateBottomPadding(),
                    start = borderPadding,
                    end = borderPadding
                )
            ) {
                OutlinedTextField(
                    value = text.value,
                    onValueChange = {
                        text.value = it
                    },
                    maxLines = 1,
                    label = {
                        Text(stringResource(id = R.string.pref_title_data_source))
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.pref_title_data_source))
                    }
                )
            }
        }
    )
}