package com.example.noteslist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.data.NotesRepository
import com.example.noteslist.databinding.ActivityMainBinding
import com.example.noteslist.domain.Note
import com.example.noteslist.presentation.recycler_view.items.DateItem
import com.example.noteslist.presentation.recycler_view.ImportantNoteDelegate
import com.example.noteslist.presentation.recycler_view.items.ImportantNoteItem
import com.example.noteslist.presentation.recycler_view.NoteStackDelegate
import com.example.noteslist.presentation.recycler_view.items.NoteStackItem
import com.example.noteslist.presentation.recycler_view.NotesAdapter
import com.example.noteslist.presentation.recycler_view.items.NotesItem
import com.example.noteslist.presentation.recycler_view.DateHeaderDelegate

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}