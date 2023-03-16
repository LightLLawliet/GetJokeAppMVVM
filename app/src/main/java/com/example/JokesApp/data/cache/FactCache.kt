package com.example.JokesApp.data.cache

import com.example.JokesApp.data.Fact
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/*
* No OOP with Realm :(
**/

open class FactCache : RealmObject(), Fact {
    @PrimaryKey
    var id: Int = -1
    var text: String = ""
    var punchline: String = ""
    var type: String = ""

    override suspend fun <T> map(mapper: Fact.Mapper<T>): T = mapper.map(type, text, punchline, id)
}
