package com.example.noteslist.presentation.ui.notes_list_screen

import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.R
import com.example.noteslist.databinding.FragmentNotesListBinding
import com.example.noteslist.domain.Note
import com.example.noteslist.presentation.di.PresentationComponent
import com.example.noteslist.presentation.di.PresentationComponentHolder
import com.example.noteslist.presentation.ui.add_note_screen.AddNoteFragmentDirections
import com.example.noteslist.presentation.ui.edit_note_screen.EditNoteFragmentDirections
import com.example.noteslist.presentation.ui.notes_list_screen.recycler_view.DateHeaderDelegate
import com.example.noteslist.presentation.ui.notes_list_screen.recycler_view.ImportantNoteDelegate
import com.example.noteslist.presentation.ui.notes_list_screen.recycler_view.NoteStackDelegate
import com.example.noteslist.presentation.ui.notes_list_screen.recycler_view.NotesAdapter
import com.example.noteslist.presentation.ui.notes_list_screen.recycler_view.items.DateItem
import com.example.noteslist.presentation.ui.notes_list_screen.recycler_view.items.ImportantNoteItem
import com.example.noteslist.presentation.ui.notes_list_screen.recycler_view.items.NoteStackItem
import com.example.noteslist.presentation.ui.notes_list_screen.recycler_view.items.NotesItem
import kotlinx.coroutines.launch

class NotesListFragment(): Fragment() {
    private var _binding: FragmentNotesListBinding? = null
    val binding get() = _binding!!
    private lateinit var notesAdapter: NotesAdapter

    private val viewModel by viewModels<NotesListViewModel> {
        PresentationComponentHolder.component.createNotesListViewModelFactory()
    }
    private var navControllerDetail: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        navControllerDetail = (requireActivity().supportFragmentManager.findFragmentById(R.id.detail) as? NavHostFragment)?.navController
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spacing = this.resources.getDimension(R.dimen.recycler_vertical_spacing).toInt()

        val onImportanceChanged = { note: Note, isImportant: Boolean ->
            viewModel.changeImportance(note, isImportant)
        }

        val onReadChanged = { note: Note, isRead: Boolean ->
            viewModel.changeRead(note, isRead)
        }

        val onNoteClick = { note: Note ->
            when (resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    when (navControllerDetail?.currentDestination?.id) {
                        R.id.emptyFragment -> {
                            navControllerDetail?.navigate(
                                EmptyFragmentDirections.navigateToEditNoteFragment(note)
                            )
                        }
                        R.id.addNoteFragmentDetail -> {
                            navControllerDetail?.navigate(
                                AddNoteFragmentDirections.navigateToEditNoteFragmentDetail(note)
                            )
                        }

                        R.id.editNoteFragmentDetail -> {
                            navControllerDetail?.navigate(
                                EditNoteFragmentDirections.navigateToEditNoteFragmentDetail(note)
                            )
                        }
                    }
                }
                else -> {
                    findNavController().navigate(
                        NotesListFragmentDirections.navigateToEditNoteFragment(note)
                    )
                }
            }
        }

        notesAdapter = NotesAdapter(
            listOf(
                ImportantNoteDelegate(
                    onImportanceChanged = onImportanceChanged,
                    onReadChanged = onReadChanged,
                    onClick = onNoteClick
                ),
                NoteStackDelegate(
                    onImportanceChanged = onImportanceChanged,
                    onReadChanged = onReadChanged,
                    onNoteClick = onNoteClick
                ),
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



        binding.addButton.setOnClickListener {
            when (resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    when (navControllerDetail?.currentDestination?.id) {
                        R.id.emptyFragment -> {
                            navControllerDetail?.navigate(
                                EmptyFragmentDirections.navigateToAddNoteFragment()
                            )
                        }

                        R.id.addNoteFragmentDetail -> {
                        }

                        R.id.editNoteFragmentDetail -> {
                            navControllerDetail?.navigate(
                                EditNoteFragmentDirections.navigateToAddNoteFragmentDetail()
                            )
                        }
                    }
                }
                else -> {
                    findNavController().navigate(
                        NotesListFragmentDirections.Companion.navigateToAddNoteFragment()
                    )
                }
            }
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