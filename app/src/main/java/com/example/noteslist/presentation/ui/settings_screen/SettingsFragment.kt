package com.example.noteslist.presentation.ui.settings_screen

import android.content.res.Configuration
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
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.noteslist.R
import com.example.noteslist.presentation.di.PresentationComponentHolder
import com.example.noteslist.presentation.ui.add_note_screen.AddNoteScreen
import com.example.noteslist.presentation.ui.add_note_screen.AddNoteViewModel
import com.example.noteslist.presentation.ui.notes_list_screen.NotesListFragmentDirections
import com.example.noteslist.presentation.ui.notes_list_screen.NotesListViewModel

class SettingsFragment: Fragment() {
    var navControllerDetail: NavController? = null

    private val viewModel by viewModels<SettingsViewModel> {
        PresentationComponentHolder.component.createSettingsViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        navControllerDetail = (requireActivity().supportFragmentManager.findFragmentById(R.id.detail) as? NavHostFragment)?.navController

        return ComposeView(requireContext()).apply {
            setContent {
                Scaffold { innerPadding ->
                    SettingsScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }
}