package com.example.noteslist.presentation.ui.add_note_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteslist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    viewModel: AddNoteViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }.apply { value = viewModel.title }
    var body by remember { mutableStateOf("") }.apply { value = viewModel.body }
    val isImportant = viewModel.isImportant
    val isTitleFilled = viewModel.isTitleFilled
    val showTitleError = viewModel.showTitleError

    val starColor = colorResource(R.color.star_color)
    val buttonColor = colorResource(R.color.title_rect_color)

    BackHandler(enabled = true) {
        onBack()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .clickable { viewModel.toggleImportance() }
                    .padding(4.dp)
            ) {
                val icon = if (!isImportant) "☆" else "★"
                Text(
                    text = icon,
                    color = starColor,
                    fontSize = 30.sp
                )
            }
        }

        item {
            OutlinedTextField(
                value = title,
                onValueChange = { newTitle ->
                    title = newTitle
                    viewModel.onTitleChanged(newTitle)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text(stringResource(R.string.title)) },
                isError = showTitleError,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
        }

        if (showTitleError) {
            item {
                Text(
                    text = stringResource(R.string.neet_to_fill),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        item {
            OutlinedTextField(
                value = body,
                onValueChange = { newBody ->
                    body = newBody
                    viewModel.onBodyChanged(newBody)
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.note_text)) },
                minLines = 4,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )
        }

        item {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (!isTitleFilled) {
                        viewModel.onShowTitleErrorChanged(true)
                    } else {
                        viewModel.addNewNote(title, body, isImportant)
                        onBack()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    disabledContainerColor = buttonColor
                )
            ) {
                Text(stringResource(R.string.add))
            }
        }
    }
}