package com.example.noteslist.presentation.recyclerView

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.domain.recyclerView.ImportantNoteItem
import com.example.noteslist.domain.recyclerView.NotesItem
import com.example.noteslist.presentation.NoteView

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