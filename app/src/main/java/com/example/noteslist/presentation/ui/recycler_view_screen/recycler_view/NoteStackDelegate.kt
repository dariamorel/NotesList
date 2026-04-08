package com.example.noteslist.presentation.ui.recycler_view_screen.recycler_view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.presentation.ui.recycler_view_screen.recycler_view.items.NoteStackItem
import com.example.noteslist.presentation.ui.recycler_view_screen.recycler_view.items.NotesItem
import com.example.noteslist.presentation.ui.recycler_view_screen.NoteStackView

class NoteStackDelegate : AdapterDelegate {

    override fun isViewType(item: NotesItem) =
        item is NoteStackItem

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = NoteStackView(parent.context)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: NotesItem) {
        val stackItem = item as NoteStackItem
        (holder as ViewHolder).view.submitNotes(stackItem.notes)
    }

    class ViewHolder(val view: NoteStackView) : RecyclerView.ViewHolder(view)
}