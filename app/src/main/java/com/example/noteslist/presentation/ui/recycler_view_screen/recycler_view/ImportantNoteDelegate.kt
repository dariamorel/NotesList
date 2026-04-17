package com.example.noteslist.presentation.ui.recycler_view_screen.recycler_view

import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.Application
import com.example.noteslist.domain.Note
import com.example.noteslist.presentation.ui.recycler_view_screen.NoteView
import com.example.noteslist.presentation.ui.recycler_view_screen.RecyclerViewFragmentDirections
import com.example.noteslist.presentation.ui.recycler_view_screen.recycler_view.items.ImportantNoteItem
import com.example.noteslist.presentation.ui.recycler_view_screen.recycler_view.items.NotesItem
import com.example.noteslist.presentation.view_model.NotesViewModel

class ImportantNoteDelegate(
    private val viewModel: NotesViewModel,
    private val onClick: (Note) -> Unit
) : AdapterDelegate {

    override fun isViewType(item: NotesItem) =
        item is ImportantNoteItem

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = NoteView(parent.context)
        view.setOnClickListener {
            onClick(view.getNote())
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: NotesItem) {
        val noteItem = item as ImportantNoteItem
        val view = (holder as ViewHolder).view
        view.setNote(noteItem.note)
        view.setOnChangeListener(object : NoteView.OnChangeListener {
            override fun onImportanceChanged(isImportant: Boolean) {
                viewModel.editNote(note = noteItem.note, newIsImportant = isImportant)
            }

            override fun onReadChanged(isRead: Boolean) {
                viewModel.editNote(note = noteItem.note, newIsRead = isRead)
            }
        })

        view.setOnLongClickListener {
            viewModel.editNote(
                note = noteItem.note,
                newIsRead = true
            )
            true
        }
    }

    class ViewHolder(val view: NoteView) : RecyclerView.ViewHolder(view)
}