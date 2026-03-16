package com.example.noteslist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.data.NotesRepository
import com.example.noteslist.databinding.ActivityMainBinding
import com.example.noteslist.domain.Note
import com.example.noteslist.presentation.recyclerView.ImportantNoteDelegate
import com.example.noteslist.domain.recyclerView.ImportantNoteItem
import com.example.noteslist.presentation.recyclerView.NoteStackDelegate
import com.example.noteslist.domain.recyclerView.NoteStackItem
import com.example.noteslist.presentation.recyclerView.NotesAdapter
import com.example.noteslist.domain.recyclerView.NotesItem

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val noteList = NotesRepository.notesList
        val items = mapNotesToItems(noteList)

        val spacing = this.resources.getDimension(R.dimen.recycler_vertical_spacing).toInt()

        val adapter = NotesAdapter(
            listOf(
                ImportantNoteDelegate(),
                NoteStackDelegate()
            )
        )
        adapter.submitItems(items)

        setContentView(binding.root)

        val ctx = this
        binding.recyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(ctx)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: android.graphics.Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.bottom = spacing
                }
            })
        }

    }
}

fun mapNotesToItems(notes: List<Note>): List<NotesItem> {

    val result = mutableListOf<NotesItem>()

    val important = notes.filter { it.isImportant }
    val normal = notes.filter { !it.isImportant }

    important.forEach {
        result.add(ImportantNoteItem(it))
    }

    if (normal.isNotEmpty()) {
        result.add(NoteStackItem(normal))
    }

    return result
}