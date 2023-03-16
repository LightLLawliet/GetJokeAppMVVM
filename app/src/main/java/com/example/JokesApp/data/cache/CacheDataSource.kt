package com.example.JokesApp.data.cache

import com.example.JokesApp.data.*
import com.example.JokesApp.presentation.FactUi
import com.example.JokesApp.presentation.ManageResources
import io.realm.Realm

interface CacheDataSource : DataSource {

    suspend fun addOrRemove(id: Int, fact: Fact): FactUi

    class Base(
        private val realm: ProvideRealm,
        manageResources: ManageResources,
        private val error: Error = Error.NoFavoriteFact(manageResources),
        private val mapper: Fact.Mapper<FactCache> = ToCache(),
        private val toFavorite: Fact.Mapper<FactUi> = ToFavoriteUi(),
        private val toBaseUi: Fact.Mapper<FactUi> = ToBaseUi()
    ) : CacheDataSource {

        override suspend fun addOrRemove(id: Int, fact: Fact): FactUi {
            realm.provideRealm().use { realm ->
                val factCached = realm.where(FactCache::class.java).equalTo("id", id).findFirst()
                return if (factCached == null) {
                    val factCache = fact.map(mapper)
                    realm.executeTransaction {
                        it.insert(factCache)
                    }
                    fact.map(toFavorite)
                } else {
                    realm.executeTransaction {
                        factCached.deleteFromRealm()
                    }
                    fact.map(toBaseUi)
                }
            }
        }

        override suspend fun fetch(): FactResult {
            realm.provideRealm().use { realm ->
                val facts = realm.where(FactCache::class.java).findAll()
                return if (facts.isEmpty())
                    FactResult.Failure(error)
                else FactResult.Success(realm.copyFromRealm(facts.random()), true)
            }
        }
    }
}

interface DataSource {
    suspend fun fetch(): FactResult
}

interface FactResult : Fact {

    fun toFavorite(): Boolean

    fun isSuccessful(): Boolean

    fun errorMessage(): String

    class Success(private val fact: Fact, private val toFavorite: Boolean) : FactResult {

        override fun toFavorite(): Boolean = toFavorite

        override fun isSuccessful(): Boolean = true

        override fun errorMessage(): String = ""

        override suspend fun <T> map(mapper: Fact.Mapper<T>): T {
            return fact.map(mapper)
        }
    }

    class Failure(private val error: Error) : FactResult {

        override fun toFavorite(): Boolean = false

        override fun isSuccessful(): Boolean = false

        override fun errorMessage(): String = error.message()

        override suspend fun <T> map(mapper: Fact.Mapper<T>): T =
            throw java.lang.IllegalStateException()
    }
}

interface ProvideRealm {
    fun provideRealm(): Realm
}