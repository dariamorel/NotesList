package com.example.noteslist.presentation.ui.notes_list_screen.recycler_view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.domain.Note
import com.example.noteslist.presentation.ui.notes_list_screen.recycler_view.items.NoteStackItem
import com.example.noteslist.presentation.ui.notes_list_screen.recycler_view.items.NotesItem
import com.example.noteslist.presentation.ui.notes_list_screen.NoteStackView
import com.example.noteslist.presentation.view_model.NotesViewModel

class NoteStackDelegate(
    private val viewModel: NotesViewModel
) : AdapterDelegate {

    override fun isViewType(item: NotesItem) =
        item is NoteStackItem

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = NoteStackView(parent.context)
        view.setOnChangeListener(object : NoteStackView.OnChangeListener {
            override fun onNoteEdit(
                note: Note,
                newTitle: String?,
                newBody: String?,
                newIsRead: Boolean?,
                newIsImportant: Boolean?
            ) {
                viewModel.editNote(
                    note = note,
                    newTitle = newTitle,
                    newBody = newBody,
                    newIsRead = newIsRead,
                    newIsImportant = newIsImportant
                )
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