package com.example.JokesApp.data.cloud

import com.example.JokesApp.data.Fact
import com.google.gson.annotations.SerializedName


class FactCloud(
    @SerializedName("type")
    private val type: String,
    @SerializedName("setup")
    private val setup: String,
    @SerializedName("punchline")
    private val punchline: String,
    @SerializedName("id")
    private val id: Int,
) : Fact {

    override suspend fun <T> map(mapper: Fact.Mapper<T>): T = mapper.map(type, setup, punchline, id)
}