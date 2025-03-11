package com.example.navwithapinothing_2.ui.screen.MoviesListScreen

import android.graphics.Movie
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviesapi.models.Response
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.data.MovieRepository
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.models.Filter
import com.example.navwithapinothing_2.utils.Collections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val movieRepository: MovieRepository) :
    ViewModel() {

    private val _state = MutableStateFlow(Result.Loading)
    val state = _state.asStateFlow()

    private val _state_random = MutableStateFlow<Result>(Result.Loading)
    val state_random = _state_random.asStateFlow()

    private val _state_collection = MutableStateFlow<Result>(Result.Loading)
    val state_collection = _state_collection.asStateFlow()

    private val _state_movies_collection = MutableStateFlow<Result>(Result.Loading)
    val state_movies_collection = _state_movies_collection.asStateFlow()

    private val _state_movie = MutableStateFlow<Result>(Result.Loading)
    val state_movie = _state_movie.asStateFlow()

    private val _state_person = MutableStateFlow<Result>(Result.Loading)
    val state_person = _state_person.asStateFlow()

    private val _state_movie_person = MutableStateFlow<Result>(Result.Loading)
    val state_movie_person = _state_movie_person.asStateFlow()

    private val _state_movie_home = MutableStateFlow<Result>(Result.Loading)
    val state_movie_home = _state_movie_home.asStateFlow()

    private var result = mutableMapOf<Pair<String, String>, Result?>()


    init {


        Collections.listOfFavoriteCollections.shuffled().take(4).forEach {
            result[it] = null
        }

        getMoviesByCollectionTopBanned("planned-to-watch-films");
        getMoviesByCollection()
    }

    fun getRandom(filter: Filter? = null) {
        println("rand")
        viewModelScope.launch {

            //delay(5000)
            //_state_random.value = Result.Loading
            movieRepository.getRandom(filter).collect {
                _state_random.value = when (it) {

                    is Result.Loading -> {
                        it
                    }

                    is Result.Error -> {
                        it
                    }

                    is Result.Success<*> -> {
                        it
                    }

                }

            }
        }
    }

    fun getPersonById(id: Long) {
        viewModelScope.launch {
            movieRepository.getPersonById(id).collect {
                _state_person.value = it
            }
        }
    }

    fun getMovieByPerson(id: Long) {

        viewModelScope.launch {
            movieRepository.getMoviesByPerson(id).collect {
                _state_movie_person.value = it
            }
        }
    }

    fun getMovieById(id: Long) {
        viewModelScope.launch {
            movieRepository.getMovieById(id).collect {
                _state_movie.value = when (it) {
                    is Result.Loading -> {
                        it
                    }

                    is Result.Error -> {
                        it
                    }

                    is Result.Success<*> -> {
                        it
                    }
                }
            }
        }
    }

    fun getCollections() {
        viewModelScope.launch {
            movieRepository.getCollections().collect {
                _state_collection.value = when (it) {

                    is Result.Loading -> {
                        it
                    }

                    is Result.Error -> {
                        it
                    }

                    is Result.Success<*> -> {
                        it
                    }

                }

            }
        }
    }

    fun getHomePageData() {
        viewModelScope.launch {

        }
    }

    fun getMoviesByCollectionTopBanned(slug: String) {

        viewModelScope.launch {
            movieRepository.getListMovieByCollection(slug = slug).collect {
                _state_movies_collection.value = when (it) {

                    is Result.Loading -> {
                        it
                    }

                    is Result.Error -> {
                        it
                    }

                    is Result.Success<*> -> {

                        it
                    }

                }

            }
        }
    }

    fun getMoviesByCollection() {
        viewModelScope.launch {

            val flow1 =
                movieRepository.getListMovieByCollection(result.keys.elementAt(0).second)
            val flow2 =
                movieRepository.getListMovieByCollection(result.keys.elementAt(1).second)
            val flow3 =
                movieRepository.getListMovieByCollection(result.keys.elementAt(2).second)
            val flow4 =
                movieRepository.getListMovieByCollection(result.keys.elementAt(3).second)



            combine(flow1, flow2, flow3, flow4) { f1, f2, f3, f4 ->

                listOf(f1, f2, f3, f4)


            }.collect { data ->
                result[result.keys.elementAt(0)] = data[0]
                result[result.keys.elementAt(1)] = data[1]
                result[result.keys.elementAt(2)] = data[2]
                result[result.keys.elementAt(3)] = data[3]

                _state_movie_home.value = Result.Success(result)
                println("success load1")
            }

        }
    }

    fun getAll() {

        /*viewModelScope.launch {
            movieRepository.getAll().collect {
                _state.value = when (it) {
                    is Result.Success -> it
                    is Result.Error -> it
                    is Result.Loading -> it
                }
            }
        }*/
    }


    val list = flow {
        movieRepository.getAll().cachedIn(viewModelScope).collect {
            emit(it)
        }
    }

    fun getAllPaging(): Flow<PagingData<MovieDTO>> {
        return list
    }
}