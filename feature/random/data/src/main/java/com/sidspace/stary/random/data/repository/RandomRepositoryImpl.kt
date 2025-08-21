package com.sidspace.stary.random.data.repository

import com.sidspace.stary.data.api.MovieApi

import com.sidspace.stary.data.utils.mapSuccess
import com.sidspace.stary.data.utils.safeCall
import com.sidspace.stary.domain.RandomFiltersOption
import com.sidspace.stary.domain.model.Collection
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.random.domain.repository.RandomRepository
import com.sidspace.stary.data.mapper.toCollection
import com.sidspace.stary.data.mapper.toDomain
import com.sidspace.stary.data.mapper.toMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class RandomRepositoryImpl @Inject constructor(private val movieApi: MovieApi) : RandomRepository {
    override suspend fun getRandomMovie(filter: RandomFiltersOption?): Flow<Result<Movie>> = flow {
        val years = filter!!.years?.joinToString("-")
        val type = filter.listOfType?.map { it.value }
        var genres = filter.listOfGenres?.map { "+" + it.lowercase() }
        val score = filter.listOfScore?.joinToString("-")
        val lists = filter.listOfCollection


        if (genres == null) {
            genres = listOf<String>()
        }

        genres = genres.toMutableList().apply {
            add("!короткометражка")
        }

        if (!genres.contains("документальный")) {
            genres = genres.toMutableList().apply {
                add("!документальный")
            }
        }


        println("year = " + years)
        println("type = " + type)
        println("genres = " + genres)
        println("score = " + score)

        emit(safeCall {
            movieApi.getRandom(
                listYears = years,
                listsType = type,
                listGenres = genres,
                score = score,
                lists = lists
            )
        }.toDomain { it.toMovie() })
    }

    override suspend fun getCollections(): Flow<Result<List<Collection>>> = flow {

        emit(safeCall { movieApi.getCollections() }.mapSuccess { it.docs }.toDomain {
            it.map { it.toCollection() }.sortedBy { it.name.contains("top") }
                .filter { it.name.length < 45 }
        })

    }
}