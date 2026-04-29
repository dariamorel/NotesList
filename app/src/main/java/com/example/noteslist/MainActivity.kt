package com.example.noteslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.noteslist.databinding.ActivityMainBinding
import com.example.noteslist.databinding.FragmentNotesListBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}