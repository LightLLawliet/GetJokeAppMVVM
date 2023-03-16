package com.example.JokesApp.data.cloud

import retrofit2.Call
import retrofit2.http.GET

interface FactService {

    @GET("random_joke")
    fun fact(): Call<FactCloud>
}