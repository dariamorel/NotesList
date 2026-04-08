package com.example.noteslist.presentation.recycler_view

import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.R
import com.example.noteslist.presentation.recycler_view.items.DateItem
import com.example.noteslist.presentation.recycler_view.items.NotesItem

class DateHeaderDelegate: AdapterDelegate {

    override fun isViewType(item: NotesItem): Boolean {
        return item is DateItem
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val dateSize = parent.context.resources.getDimension(R.dimen.recycler_date_header_size)
        val textView = TextView(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
            textSize = dateSize
            gravity = Gravity.CENTER
        }
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: NotesItem
    ) {
        val header = item as DateItem
        (holder.itemView as TextView).text = header.getDateFormatted()
    }

    class ViewHolder(view: TextView) : RecyclerView.ViewHolder(view)
}