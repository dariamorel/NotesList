package com.example.noteslist.presentation.di

import com.example.noteslist.data.di.DataComponentHolder
import com.example.noteslist.data.repository.NotesRepository
import com.example.noteslist.data.repository.SettingsRepository

object PresentationComponentHolder {
    lateinit var component: PresentationComponent
        private set

    fun init(dependencies: PresentationComponent.Dependencies = DefaultDependencies()) {
        component = PresentationComponent(dependencies)
    }

    private class DefaultDependencies : PresentationComponent.Dependencies {
        override fun getNotesRepository(): NotesRepository {
            return DataComponentHolder.component.getNotesRepository()
        }

        override fun getSettingsRepository(): SettingsRepository {
            return DataComponentHolder.component.getSettingsRepository()
        }

    }
}