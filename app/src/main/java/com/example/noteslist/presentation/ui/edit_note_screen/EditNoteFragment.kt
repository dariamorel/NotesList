package com.example.noteslist.presentation.ui.edit_note_screen

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
import androidx.navigation.fragment.navArgs

class EditNoteFragment: Fragment() {
    private val args: EditNoteFragmentArgs by navArgs()

    private val viewModel by viewModels<EditNoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return ComposeView(requireContext()).apply {
            setContent {
                Scaffold { innerPadding ->
                    EditNoteScreen(
                        note = args.note,
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        findNavController().navigate(
                            EditNoteFragmentDirections.navigateToRecyclerViewFragment()
                        )
                    }
                }
            }
        }
    }
}