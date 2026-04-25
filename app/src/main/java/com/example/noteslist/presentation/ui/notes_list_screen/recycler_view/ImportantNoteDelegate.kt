package com.example.noteslist.presentation.ui.notes_list_screen.recycler_view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.domain.Note
import com.example.noteslist.presentation.ui.notes_list_screen.NoteView
import com.example.noteslist.presentation.ui.notes_list_screen.NotesListViewModel
import com.example.noteslist.presentation.ui.notes_list_screen.recycler_view.items.ImportantNoteItem
import com.example.noteslist.presentation.ui.notes_list_screen.recycler_view.items.NotesItem

class ImportantNoteDelegate(
    private val viewModel: NotesListViewModel,
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
                viewModel.changeImportance(noteItem.note, isImportant)
            }
        })

        view.setOnLongClickListener {
            val note = noteItem.note
            viewModel.changeRead(note, !note.isRead)
            true
        }
    }

    class ViewHolder(val view: NoteView) : RecyclerView.ViewHolder(view)
}