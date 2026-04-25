package com.example.noteslist.presentation.ui.add_note_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.noteslist.presentation.view_model.NotesViewModel

class AddNoteFragment: Fragment() {

    private val viewModel by activityViewModels<NotesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                Scaffold { innerPadding ->
                    AddNoteScreen(
                        viewModel =  viewModel,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        findNavController().navigate(
                            AddNoteFragmentDirections.navigateToRecyclerViewFragment()
                        )
                    }
                }
            }
        }
    }
}