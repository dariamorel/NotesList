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

        val noteList = NotesRepository.notesList
        val items = mapNotesToItems(noteList)

        val spacing = this.resources.getDimension(R.dimen.recycler_vertical_spacing).toInt()

        val adapter = NotesAdapter(
            listOf(
                ImportantNoteDelegate(),
                NoteStackDelegate(),
                DateHeaderDelegate()
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

    private fun mapNotesToItems(notes: List<Note>): List<NotesItem> {

        val grouped = notes.groupBy { it.createTime.toLocalDate() }

        val result = mutableListOf<NotesItem>()

        grouped.keys.sortedDescending().forEach { date ->
            result.add(DateItem(date))

            val notesForDate = grouped[date]!!

            notesForDate.filter { it.isImportant }.forEach { note ->
                result.add(ImportantNoteItem(note))
            }

            val normalNotes = notesForDate.filter { !it.isImportant }
            if (normalNotes.isNotEmpty()) {
                result.add(NoteStackItem(normalNotes))
            }
        }

        return result
    }
}