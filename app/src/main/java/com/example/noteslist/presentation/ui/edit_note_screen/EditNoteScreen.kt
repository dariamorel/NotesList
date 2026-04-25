package com.example.noteslist.presentation.ui.edit_note_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import com.example.noteslist.domain.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    note: Note,
    viewModel: EditNoteViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf(note.title) }
    var body by remember { mutableStateOf(note.body) }
    var isImportant by remember { mutableStateOf(note.isImportant) }
    var isTitleFilled by remember { mutableStateOf(false) }
    var showTitleError by remember { mutableStateOf(false) }
    var isRead by remember { mutableStateOf(note.isRead) }

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
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = note.getCreateTimeFormatted(),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.is_read),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Checkbox(
                checked = isRead,
                onCheckedChange = { isRead = !isRead },
                colors = CheckboxDefaults.colors(
                    checkedColor = buttonColor,
                    uncheckedColor = MaterialTheme.colorScheme.onSurface .copy(alpha = 0.5f)
                )
            )
        }

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
                    viewModel.editNote(
                        note = note,
                        newTitle = title,
                        newBody = body,
                        newIsRead = isRead,
                        newIsImportant = isImportant
                    )
                    onBack()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                disabledContainerColor = buttonColor
            )
        ) {
            Text(stringResource(R.string.save))
        }
    }
}