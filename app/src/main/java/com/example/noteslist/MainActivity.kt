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
        setContentView(binding.root)

        val note = NotesRepository.notesList[1]
        val noteView = binding.noteView
        noteView.setNote(note)
        noteView.setOnChangeListener(object : NoteView.OnChangeListener {
            override fun onImportanceChanged(isImportant: Boolean) {
            }

            override fun onReadChanged(isRead: Boolean) {
            }

        })
    }
}