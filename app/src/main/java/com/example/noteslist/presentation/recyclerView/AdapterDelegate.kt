package com.example.noteslist.presentation.recyclerView

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.domain.recyclerView.ImportantNoteItem
import com.example.noteslist.domain.recyclerView.NoteStackItem
import com.example.noteslist.presentation.NoteStackView
import com.example.noteslist.presentation.NoteView
import com.example.noteslist.domain.recyclerView.NotesItem

interface AdapterDelegate {
    fun isViewType(item: NotesItem): Boolean

    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: NotesItem)
}
