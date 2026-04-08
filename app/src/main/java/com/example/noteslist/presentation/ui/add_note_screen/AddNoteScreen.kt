package com.example.noteslist.presentation.ui.add_note_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.noteslist.presentation.view_model.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    viewModel: NotesViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var isImportant by remember { mutableStateOf(false) }
    var isTitleFilled by remember { mutableStateOf(false) }
    var showTitleError by remember { mutableStateOf(false) }

    val starColor = colorResource(R.color.star_color)
    val buttonColor = colorResource(R.color.title_rect_color)

    BackHandler(enabled = true) {
        onBack()
    }

    LaunchedEffect(title) {
        if (title.isNotBlank()) {
            isTitleFilled = true
            showTitleError
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Start)
                .clickable { isImportant = !isImportant }
                .padding(4.dp)
        ) {
            val icon = if (!isImportant) "☆" else "★"
            Text(
                text = icon,
                color = starColor,
                fontSize = 30.sp
            )
        }

        OutlinedTextField(
            value = title,
            onValueChange = { newTitle ->
                title = newTitle
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

        if (showTitleError) {
            Text(
                text = stringResource(R.string.neet_to_fill),
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = body,
            onValueChange = { newBody ->
                body = newBody },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.note_text)) },
            minLines = 4,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (!isTitleFilled) {
                    showTitleError = true
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