package com.example.noteslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.noteslist.data.NotesRepository
import com.example.noteslist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val note = NotesRepository.notesList.first()
        binding.noteView.setNote(note)
    }
}