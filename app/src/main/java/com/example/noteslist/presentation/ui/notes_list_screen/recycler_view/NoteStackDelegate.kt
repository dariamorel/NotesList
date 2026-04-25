package com.example.noteslist.presentation.ui.notes_list_screen.recycler_view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.domain.Note
import com.example.noteslist.presentation.ui.notes_list_screen.recycler_view.items.NoteStackItem
import com.example.noteslist.presentation.ui.notes_list_screen.recycler_view.items.NotesItem
import com.example.noteslist.presentation.ui.notes_list_screen.NoteStackView
import com.example.noteslist.presentation.ui.notes_list_screen.NotesListViewModel

class NoteStackDelegate(
    private val viewModel: NotesListViewModel
) : AdapterDelegate {

    override fun isViewType(item: NotesItem) =
        item is NoteStackItem

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = NoteStackView(parent.context)
        view.setOnChangeListener(object : NoteStackView.OnChangeListener {
            override fun onImportanceChanged(note: Note, isImportant: Boolean) {
                viewModel.changeImportance(note, isImportant)
            }

            override fun onReadChanged(note: Note, isRead: Boolean) {
                viewModel.changeRead(note, isRead)
            }

        })
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: NotesItem) {
        val stackItem = item as NoteStackItem
        (holder as ViewHolder).view.submitNotes(stackItem.notes)
    }

    class ViewHolder(val view: NoteStackView) : RecyclerView.ViewHolder(view)
}