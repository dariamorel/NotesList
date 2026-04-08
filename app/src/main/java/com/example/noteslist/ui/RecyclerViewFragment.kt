package com.example.noteslist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.R
import com.example.noteslist.data.NotesRepository
import com.example.noteslist.databinding.FragmentRecyclerViewBinding
import com.example.noteslist.domain.Note
import com.example.noteslist.presentation.recycler_view.DateHeaderDelegate
import com.example.noteslist.presentation.recycler_view.ImportantNoteDelegate
import com.example.noteslist.presentation.recycler_view.NoteStackDelegate
import com.example.noteslist.presentation.recycler_view.NotesAdapter
import com.example.noteslist.presentation.recycler_view.items.DateItem
import com.example.noteslist.presentation.recycler_view.items.ImportantNoteItem
import com.example.noteslist.presentation.recycler_view.items.NoteStackItem
import com.example.noteslist.presentation.recycler_view.items.NotesItem
import kotlin.collections.filter

class RecyclerViewFragment(): Fragment() {
    private var _binding: FragmentRecyclerViewBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.addButton.setOnClickListener {
        }

        binding.recyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context)
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

        binding.addButton.setOnClickListener {
            findNavController().navigate(
                RecyclerViewFragmentDirections.navigateToAddNoteFragment()
            )
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