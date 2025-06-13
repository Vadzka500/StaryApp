package com.example.navwithapinothing_2.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.data.MovieRepository
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.CollectionMovieDb
import com.example.navwithapinothing_2.database.models.MovieDb
import com.example.navwithapinothing_2.models.Filter
import com.example.navwithapinothing_2.usecase.AddMovieUseCase
import com.example.navwithapinothing_2.usecase.CheckMovieUseCase
import com.example.navwithapinothing_2.usecase.GetAllMoviesUseCase
import com.example.navwithapinothing_2.usecase.GetMoviesByCollection
import com.example.navwithapinothing_2.usecase.RemoveMovieUseCase
import com.example.navwithapinothing_2.utils.Collections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val getAllMoviesUseCase: GetAllMoviesUseCase,
    private val addMovieUseCase: AddMovieUseCase,
    private val removeMovieUseCase: RemoveMovieUseCase,
    private val checkMovieUseCase: CheckMovieUseCase,
    private val getMoviesByCollection: GetMoviesByCollection
) :
    ViewModel() {

    val isNavBarVisible = mutableStateOf(true)

    var searchStr = mutableStateOf("")

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

    private val _state_movie_collections_all = MutableStateFlow<Result>(Result.Loading)
    val state_movie_collections_all = _state_movie_collections_all.asStateFlow()

    private val _state_movie_all_visible_movies = MutableStateFlow<Result>(Result.Loading)
    val state_movie_all_visible_movies = _state_movie_all_visible_movies.asStateFlow()

    private val _state_movie_reviews = MutableStateFlow<Result>(Result.Loading)
    val state_movie_reviews = _state_movie_reviews.asStateFlow()

    private var result = mutableMapOf<Pair<String, String>, Result?>()

    private val _state_movie_search = MutableStateFlow<Result>(Result.Loading)
    val state_movie_search = _state_movie_search.asStateFlow()

    private val _state_movie_all_visible =
        MutableStateFlow<ResultDb<List<MovieDb>>>(ResultDb.Loading)
    val state_movie_all_visible = _state_movie_all_visible.asStateFlow()

    private val _state_movie_visible_collection =
        MutableStateFlow<MutableMap<String, Int>>(mutableMapOf())
    val state_movie_visible_collection = _state_movie_visible_collection.asStateFlow()

    private val _isMovieExists = MutableStateFlow<MovieDb?>(null)
    val isMovieExists = _isMovieExists.asStateFlow()


    init {

        println("init block")

        Collections.listOfFavoriteCollections.shuffled().take(4).forEach {
            result[it] = null
        }

        getMoviesByCollectionTopBanned("planned-to-watch-films");
        getMoviesByCollection()

        viewModelScope.launch {
            getMoviesByCollection.invoke("top250").collect {
                println("sssize 3 =" + it)
            }
        }

    }

    fun getReviewsById(id: Long){
        viewModelScope.launch {
            movieRepository.getReviewsById(id).collect{
                _state_movie_reviews.value = when(it){
                    is Result.Error<*> ->{
                        it
                    }
                    Result.Loading -> {
                        it
                    }
                    is Result.Success<*> -> {
                        it
                    }
                }
            }
        }
    }

    fun getVisibleMoviesApi(list: List<Long>) {
        viewModelScope.launch {
             movieRepository.getMoviesByIds(list).collect {

                 _state_movie_all_visible_movies.value = when (it) {
                    is Result.Error<*> -> {
                        it
                    }

                    Result.Loading -> {
                        it
                    }

                    is Result.Success<*> -> {
                        it
                    }
                }
            }
        }
    }

    fun getCountMoviesByCollection(collectionName: String) {
        viewModelScope.launch {
            _state_movie_visible_collection.value[collectionName] = 0
            getMoviesByCollection.invoke(collectionName).collect {

                when (val data = it) {
                    ResultDb.Error -> {

                    }

                    ResultDb.Loading -> {

                    }

                    is ResultDb.Success<*> -> {
                        val updatedMap = _state_movie_visible_collection.value.toMutableMap()
                        updatedMap[collectionName] = (data.data as List<*>).size
                        _state_movie_visible_collection.value = updatedMap
                        println("data2 = " + _state_movie_visible_collection.value)
                    }
                }


            }
        }
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

                    is Result.Error<*> -> {
                        it
                    }

                    is Result.Success<*> -> {
                        it
                    }

                }

            }
        }
    }

    fun getSearchMovies(search: String) {
        println("search movie")
        viewModelScope.launch {
            movieRepository.getSearchedMovies(search).collect {
                _state_movie_search.value = it
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

    fun getVisibleMovies() {
        viewModelScope.launch {

            getAllMoviesUseCase.invoke().collect {

                when (it) {
                    ResultDb.Error -> {
                        _state_movie_all_visible.value = it
                    }

                    ResultDb.Loading -> {
                        _state_movie_all_visible.value = it
                    }

                    is ResultDb.Success<List<MovieDb>> -> {
                        _state_movie_all_visible.value = it
                    }
                }

            }
        }
    }

    fun checkMovieDatabase(id: Long) {

        viewModelScope.launch {

            checkMovieUseCase.invoke(id).collect {


                when (val data = it) {
                    ResultDb.Error -> {
                        println("get error")
                    }

                    ResultDb.Loading -> {
                        println("get error 1")
                    }

                    is ResultDb.Success<*> -> {
                        println("get error 2 = " + data.data as MovieDb?)
                        _isMovieExists.value = data.data as MovieDb?
                    }
                }

                /*
                                _isMovieExists.value = when(it){
                                    is ResultDb.Error -> {
                                        println("get error")
                                        null}
                                    is ResultDb.Loading -> {
                                        println("get error 1")
                                        null}
                                    is ResultDb.Success<*> -> ({
                                        println("get error 2")
                                        it.data}) as MovieDb?
                                }*/
            }


        }
    }

    fun addMovieToDatabase(id: Long, collections: List<String>?) {
        viewModelScope.launch {
            val list = mutableListOf<CollectionMovieDb>()
            if (collections != null) {
                for (slug in collections) {
                    list.add(CollectionMovieDb(id = 0, collectionSlug = slug))
                }
            }
            addMovieUseCase.invoke(MovieDb(0, id, isViewed = true), list = list)
        }
    }

    fun removeMovieFromDatabase(id: Long) {
        viewModelScope.launch {
            removeMovieUseCase.invoke(id)
        }
    }

    fun getMovieById(id: Long) {
        println("get by id")
        //_state_movie.value = Result.Loading
        viewModelScope.launch {


            movieRepository.getMovieById(id).collect {
                _state_movie.value = when (it) {
                    is Result.Loading -> {
                        it
                    }

                    is Result.Error<*> -> {
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

                    is Result.Error<*> -> {
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

                    is Result.Error<*> -> {
                        it
                    }

                    is Result.Success<*> -> {

                        it
                    }

                }

            }
        }
    }

    fun resetStateMovieByCollectionAll() {
        _state_movie_collections_all.update { Result.Loading }
    }

    fun getMoviesByCollection(slug: String, limit: Int = 30) {
        viewModelScope.launch {

            movieRepository.getListMovieByCollection(slug = slug, limit = limit).collect {
                _state_movie_collections_all.value = when (it) {

                    is Result.Loading -> {
                        it
                    }

                    is Result.Error<*> -> {
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