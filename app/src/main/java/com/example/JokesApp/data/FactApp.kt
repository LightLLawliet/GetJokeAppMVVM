package com.example.JokesApp.data

import android.app.Application
import com.example.JokesApp.data.cache.CacheDataSource
import com.example.JokesApp.data.cache.ProvideRealm
import com.example.JokesApp.data.cloud.CloudDataSource
import com.example.JokesApp.data.cloud.FactService
import com.example.JokesApp.presentation.MainViewModel
import com.example.JokesApp.presentation.ManageResources
import io.realm.Realm
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FactApp : Application() {

    lateinit var viewModel: MainViewModel

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://official-joke-api.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val manageResources = ManageResources.Base(this)
        viewModel = MainViewModel(
            BaseRepository(
                CloudDataSource.Base(
                    retrofit.create(FactService::class.java),
                    manageResources
                ),
                CacheDataSource.Base(object : ProvideRealm {
                    override fun provideRealm(): Realm = Realm.getDefaultInstance()
                }, manageResources)
            )
        )
    }
}