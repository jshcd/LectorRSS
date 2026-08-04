package es.jshcd.android.rssreader.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import es.jshcd.android.rssreader.R
import es.jshcd.android.rssreader.ui.state.SettingsState
import es.jshcd.android.rssreader.ui.theme.RSSReaderTheme

val borderPadding = 5.dp

@Composable
fun SettingsScreen(
    state: SettingsState,
    onAddClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    onArrowBackClick: () -> Unit
) {
    val text = remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    val itemToDelete = remember { mutableStateOf("") }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(stringResource(R.string.confirm_delete_title)) },
            text = { Text(stringResource(R.string.confirm_delete_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClick(itemToDelete.value)
                        showDialog.value = false
                    }
                ) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }

    Scaffold(
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
                    Text(text = stringResource(id = R.string.settings_screen_title))
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(borderPadding)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = text.value,
                        onValueChange = { text.value = it },
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        label = { Text(stringResource(id = R.string.pref_title_data_source)) },
                        placeholder = { Text(text = stringResource(id = R.string.pref_title_data_source)) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (text.value.isNotBlank()) {
                                onAddClick(text.value)
                                text.value = ""
                            }
                        }
                    ) {
                        Text(stringResource(R.string.add))
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                LazyColumn {
                    items(state.dataSources) { source ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = source,
                                modifier = Modifier.weight(1f),
                                maxLines = 1
                            )
                            IconButton(
                                onClick = {
                                    itemToDelete.value = source
                                    showDialog.value = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
@PreviewLightDark
private fun SettingsScreenPreview() {
    RSSReaderTheme {
        SettingsScreen(
            state = SettingsState(),
            onAddClick = {},
            onDeleteClick = {},
            onArrowBackClick = {}
        )
    }
}
