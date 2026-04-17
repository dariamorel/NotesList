package com.example.noteslist.presentation.ui.edit_note_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteslist.Application
import com.example.noteslist.databinding.FragmentEditNoteBinding
import com.example.noteslist.presentation.view_model.NotesViewModel

class EditNoteFragment: Fragment() {
    private var _binding: FragmentEditNoteBinding? = null
    val binding get() = _binding!!
    private val args: EditNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]

        val note = args.note

        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        binding.composeView.setContent {
            EditNoteScreen(
                note = note,
                viewModel = viewModel
            ) {
                findNavController().navigate(
                    EditNoteFragmentDirections.navigateToRecyclerViewFragment()
                )
            }
        }
        return binding.root
    }
}