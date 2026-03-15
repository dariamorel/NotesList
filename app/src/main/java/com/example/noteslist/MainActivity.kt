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
        binding.note1.setNote(noteList[0])
        binding.note2.setNote(noteList[1])
        binding.note3.setNote(noteList[2])
        binding.note1.setOnChangeListener(Listener)
        binding.note2.setOnChangeListener(Listener)
        binding.note3.setOnChangeListener(Listener)

        setContentView(binding.root)
    }

    object Listener : NoteView.OnChangeListener {
        override fun onImportanceChanged(isImportant: Boolean) {
        }

        override fun onReadChanged(isRead: Boolean) {
        }
    }
}