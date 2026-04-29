package com.example.noteslist

import android.app.Application
import com.example.noteslist.data.di.DataComponentHolder
import com.example.noteslist.presentation.di.PresentationComponentHolder

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        DataComponentHolder.init()
        PresentationComponentHolder.init()
    }
}