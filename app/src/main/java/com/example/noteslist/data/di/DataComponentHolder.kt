package com.example.noteslist.data.di

import com.example.noteslist.presentation.di.PresentationComponent

object DataComponentHolder {
    lateinit var component: DataComponent
        private set

    fun init(dependencies: DataComponent.Dependencies) {
        component = DataComponent(dependencies)
    }
}