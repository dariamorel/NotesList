package com.example.noteslist.presentation.recycler_view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.presentation.recycler_view.items.NotesItem

interface AdapterDelegate {
    fun isViewType(item: NotesItem): Boolean

    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: NotesItem)
}
