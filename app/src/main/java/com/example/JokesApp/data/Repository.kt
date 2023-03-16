package com.example.JokesApp.data

import com.example.JokesApp.data.cache.FactResult
import com.example.JokesApp.presentation.FactUi

interface Repository<S, E> {

    suspend fun fetch(): FactResult

    suspend fun changeFactStatus(): FactUi

    fun chooseFavorites(favorites: Boolean)
}