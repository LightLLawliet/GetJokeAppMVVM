package com.example.JokesApp.presentation

import androidx.annotation.DrawableRes
import com.example.jokesappmvvm.R

interface FactUi {

    fun show(factUiCallback: FactUiCallback)

    abstract class Abstract(
        private val text: String,
        private val punchline: String,

        @DrawableRes
        private val iconResId: Int
    ) : FactUi {

        override fun show(factUiCallback: FactUiCallback) = with(factUiCallback) {
            provideText("$text\n$punchline")
            provideIconResId(iconResId)
        }
    }

    class Base(text: String, punchline: String) :
        Abstract(text, punchline, R.drawable.ic_favorite_unselected_24)

    class Favorite(text: String, punchline: String) :
        Abstract(text, punchline, R.drawable.ic_favorite_selected_24)

    class Failed(text: String) : Abstract(text, "", 0)
}
