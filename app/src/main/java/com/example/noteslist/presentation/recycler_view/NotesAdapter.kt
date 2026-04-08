package com.example.noteslist.presentation.recycler_view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.presentation.recycler_view.items.NotesItem

class NotesAdapter(
    private val delegates: List<AdapterDelegate>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items = listOf<NotesItem>()

    fun submitItems(items: List<NotesItem>) {
        this.items = items
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return delegates.indexOfFirst { it.isViewType(item) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        delegates[viewType].onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegates[getItemViewType(position)]
            .onBindViewHolder(holder, items[position])
    }

    override fun getItemCount() = items.size
}