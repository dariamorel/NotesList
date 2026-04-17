package com.example.noteslist.presentation.ui.recycler_view_screen

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.Application
import com.example.noteslist.R
import com.example.noteslist.databinding.FragmentRecyclerViewBinding
import com.example.noteslist.domain.Note
import com.example.noteslist.presentation.ui.recycler_view_screen.recycler_view.DateHeaderDelegate
import com.example.noteslist.presentation.ui.recycler_view_screen.recycler_view.ImportantNoteDelegate
import com.example.noteslist.presentation.ui.recycler_view_screen.recycler_view.NoteStackDelegate
import com.example.noteslist.presentation.ui.recycler_view_screen.recycler_view.NotesAdapter
import com.example.noteslist.presentation.ui.recycler_view_screen.recycler_view.items.DateItem
import com.example.noteslist.presentation.ui.recycler_view_screen.recycler_view.items.ImportantNoteItem
import com.example.noteslist.presentation.ui.recycler_view_screen.recycler_view.items.NoteStackItem
import com.example.noteslist.presentation.ui.recycler_view_screen.recycler_view.items.NotesItem
import com.example.noteslist.presentation.view_model.NotesViewModel
import kotlinx.coroutines.launch

class RecyclerViewFragment(): Fragment() {
    private var _binding: FragmentRecyclerViewBinding? = null
    val binding get() = _binding!!
    private lateinit var notesAdapter: NotesAdapter

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

        val viewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]

        val spacing = this.resources.getDimension(R.dimen.recycler_vertical_spacing).toInt()

        notesAdapter = NotesAdapter(
            listOf(
                ImportantNoteDelegate(viewModel) { note ->
                    findNavController().navigate(
                        RecyclerViewFragmentDirections.navigateToEditNoteFragment(note)
                    )
                },
                NoteStackDelegate(viewModel),
                DateHeaderDelegate()
            )
        )

        binding.recyclerView.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.bottom = spacing
                }
            })
        }

        binding.addButton?.setOnClickListener {
            findNavController().navigate(
                RecyclerViewFragmentDirections.Companion.navigateToAddNoteFragment()
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notes.collect { noteList ->
                    notesAdapter.submitItems(mapNotesToItems(noteList))
                }
            }
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