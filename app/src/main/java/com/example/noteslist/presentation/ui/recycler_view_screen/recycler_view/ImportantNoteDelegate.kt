package com.example.noteslist.presentation.ui.recycler_view_screen.recycler_view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.presentation.ui.recycler_view_screen.recycler_view.items.ImportantNoteItem
import com.example.noteslist.presentation.ui.recycler_view_screen.recycler_view.items.NotesItem
import com.example.noteslist.presentation.ui.recycler_view_screen.NoteView

class ImportantNoteDelegate : AdapterDelegate {

    override fun isViewType(item: NotesItem) =
        item is ImportantNoteItem

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = NoteView(parent.context)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: NotesItem) {
        val noteItem = item as ImportantNoteItem
        (holder as ViewHolder).view.setNote(noteItem.note)
    }

    class ViewHolder(val view: NoteView) : RecyclerView.ViewHolder(view)
}