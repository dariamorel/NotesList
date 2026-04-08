package com.example.noteslist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import com.example.noteslist.databinding.FragmentAddNoteBinding
import com.example.noteslist.databinding.FragmentRecyclerViewBinding
import com.example.noteslist.presentation.AddNoteScreen

class AddNoteFragment: Fragment() {
    private var _binding: FragmentAddNoteBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        binding.composeView.setContent {
            AddNoteScreen()
        }
        return binding.root
    }
}