package com.example.noteslist.presentation.ui.notes_list_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.noteslist.presentation.ui.add_note_screen.AddNoteFragmentDirections
import com.example.noteslist.presentation.ui.add_note_screen.AddNoteScreen
import com.example.noteslist.presentation.ui.add_note_screen.AddNoteViewModel

class EmptyFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
            }
        }
    }
}