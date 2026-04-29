package com.example.noteslist.presentation.ui.settings_screen

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.noteslist.R
import com.example.noteslist.presentation.ui.add_note_screen.AddNoteViewModel
import com.example.noteslist.presentation.ui.notes_list_screen.NotesListFragmentDirections

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    var stackSpacing by remember { mutableStateOf("") }.apply { value = viewModel.stackSpacing }
    var stackMaxVisible by remember { mutableStateOf("") }.apply { value = viewModel.stackMaxVisible }

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
                modifier = Modifier.size(48.dp)
                    .clickable {
                        onBack()
                    }
                    .padding(4.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    tint = Color.Black,
                    modifier = Modifier,
                    contentDescription = "back arrow"
                )
            }
        }

        item {
            OutlinedTextField(
                value = stackSpacing,
                onValueChange = { newStackSpacing ->
                    stackSpacing = newStackSpacing
                    viewModel.onStackSpacingChanged(newStackSpacing)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Расстояние между стэками заметок") },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
        }

        item {
            OutlinedTextField(
                value = stackMaxVisible,
                onValueChange = { newStackMaxVisible ->
                    stackMaxVisible = newStackMaxVisible
                    viewModel.onStackMaxVisibleChanged(newStackMaxVisible)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Максимальное видимое количество заметок в стэке") },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
        }

        item {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.saveSettings()
                    onBack()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    disabledContainerColor = buttonColor
                )
            ) {
                Text("Применить параметры")
            }
        }
    }
}