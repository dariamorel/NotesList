package com.example.noteslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.noteslist.data.NotesRepository
import com.example.noteslist.databinding.ActivityMainBinding
import com.example.noteslist.presentation.NoteView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val noteList = NotesRepository.notesList
        setContentView(binding.root)
        binding.stack.submitNotes(noteList)
    }
}