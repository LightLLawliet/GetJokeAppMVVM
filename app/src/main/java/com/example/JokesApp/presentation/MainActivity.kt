package com.example.JokesApp.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.JokesApp.data.FactApp
import com.example.jokesappmvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val textView = binding.textView
        val button = binding.actionButton
        val progressBar = binding.progressBar
        setContentView(binding.root)

        viewModel = (application as FactApp).viewModel

        binding.showFavoriteCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.chooseFavorite(isChecked)
        }

        binding.favoriteImageButton.setOnClickListener {
            viewModel.changeFactStatus()
        }

        button.setOnClickListener {
            button.isEnabled = false
            binding.progressBar.visibility = View.VISIBLE
            binding.favoriteImageButton.visibility = View.VISIBLE
            viewModel.getFact()
        }

        val factUiCallback = object : FactUiCallback {
            override fun provideText(text: String) {
                button.isEnabled = true
                progressBar.visibility = View.INVISIBLE
                textView.text = text
            }

            override fun provideIconResId(iconResId: Int) {
                binding.favoriteImageButton.setImageResource(iconResId)
            }
        }
        viewModel.init(factUiCallback)
    }
}