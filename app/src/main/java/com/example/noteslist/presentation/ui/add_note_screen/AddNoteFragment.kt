package com.example.noteslist.presentation.ui.add_note_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteslist.Application
import com.example.noteslist.databinding.FragmentAddNoteBinding
import com.example.noteslist.presentation.view_model.NotesViewModel

class AddNoteFragment: Fragment() {
    private var _binding: FragmentAddNoteBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]

        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        binding.composeView.setContent {
            AddNoteScreen(
                viewModel = viewModel
            ) {
                findNavController().navigate(
                    AddNoteFragmentDirections.Companion.navigateToRecyclerViewFragment()
                )
            }
        }
        return binding.root
    }
}