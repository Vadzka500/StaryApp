package com.example.navwithapinothing_2.features.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navwithapinothing_2.data.MovieRepository
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.CollectionMovieDb
import com.example.navwithapinothing_2.database.models.Folder
import com.example.navwithapinothing_2.database.models.FolderWithMovies
import com.example.navwithapinothing_2.database.models.MovieDb
import com.example.navwithapinothing_2.models.Filter
import com.example.navwithapinothing_2.usecase.AddFolderUseCase
import com.example.navwithapinothing_2.usecase.AddMovieToFolderUseCase
import com.example.navwithapinothing_2.usecase.AddMovieUseCase
import com.example.navwithapinothing_2.usecase.CheckMovieUseCase
import com.example.navwithapinothing_2.usecase.GetAllFoldersUseCase

import com.example.navwithapinothing_2.usecase.GetFolderUseCase
import com.example.navwithapinothing_2.usecase.GetMoviesByCollection
import com.example.navwithapinothing_2.usecase.InitDefaultFoldersUseCase
import com.example.navwithapinothing_2.usecase.RemoveMovieFromFolderUseCase
import com.example.navwithapinothing_2.usecase.RemoveMovieUseCase
import com.example.navwithapinothing_2.usecase.UpdateMovieBookmarkUseCase
import com.example.navwithapinothing_2.usecase.UpdateMovieViewedUseCase
import com.example.navwithapinothing_2.utils.Collections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository,

    private val addMovieUseCase: AddMovieUseCase,
    private val removeMovieUseCase: RemoveMovieUseCase,
    private val checkMovieUseCase: CheckMovieUseCase,
    private val getMoviesByCollection: GetMoviesByCollection,
    private val addFolderUseCase: AddFolderUseCase,
    private val initDefaultFoldersUseCase: InitDefaultFoldersUseCase,
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    private val getFolderUseCase: GetFolderUseCase,
    private val addMovieToFolderUseCase: AddMovieToFolderUseCase,
    private val removeMovieFromFolderUseCase: RemoveMovieFromFolderUseCase,
    private val updateMovieViewedUseCase: UpdateMovieViewedUseCase,
    private val updateMovieBookmarkUseCase: UpdateMovieBookmarkUseCase
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



    private val _state_movie_reviews = MutableStateFlow<Result>(Result.Loading)
    val state_movie_reviews = _state_movie_reviews.asStateFlow()

    private var result = mutableMapOf<Pair<String, String>, Result?>()

    private val _state_movie_search = MutableStateFlow<Result>(Result.Loading)
    val state_movie_search = _state_movie_search.asStateFlow()



    private val _state_movie_visible_collection =
        MutableStateFlow<MutableMap<String, Int>>(mutableMapOf())
    val state_movie_visible_collection = _state_movie_visible_collection.asStateFlow()

    private val _isMovieExists = MutableStateFlow<MovieDb?>(null)
    val isMovieExists = _isMovieExists.asStateFlow()


    private val _getAllFoldersState =
        MutableStateFlow<ResultDb<List<FolderWithMovies>>>(ResultDb.Loading)
    val getAllFoldersState = _getAllFoldersState.asStateFlow()

    private val _getFolderState = MutableStateFlow<ResultDb<Folder>>(ResultDb.Loading)
    val getFolderState = _getFolderState.asStateFlow()

    private val _addMovieToFolderState = MutableStateFlow<ResultDb<Unit>>(ResultDb.Loading)
    val addMovieToFolderState = _addMovieToFolderState.asStateFlow()

    private val _removeMovieFromFolderState = MutableStateFlow<ResultDb<Unit>>(ResultDb.Loading)
    val removeMovieFromFolderState = _removeMovieFromFolderState.asStateFlow()

    init {


        println("init block")

    }

    private suspend fun addMovie(idMovie: Long, collections: List<String>?){

        val list = mutableListOf<CollectionMovieDb>()
        if (collections != null) {
            for (slug in collections) {
                list.add(CollectionMovieDb(movieId = idMovie, collectionSlug = slug))
            }
        }
        addMovieUseCase.invoke(MovieDb(idMovie), list = list)
    }

    fun addMovieToFolder(idMovie: Long, idFolder: Long, collections: List<String>?) {
        viewModelScope.launch {
            addMovie(idMovie, collections)
            _addMovieToFolderState.value = addMovieToFolderUseCase(idMovie, idFolder)
        }
    }

    fun removeMovieFromFolder(idMovie: Long, idFolder: Long) {
        viewModelScope.launch {
            _removeMovieFromFolderState.value = removeMovieFromFolderUseCase(idMovie, idFolder)
        }
    }

    fun initFolders() {
        viewModelScope.launch {
            initDefaultFoldersUseCase()
        }
    }

    fun getFolder(id: Long) {
        viewModelScope.launch {
            _getFolderState.value = when (val data = getFolderUseCase(id)) {
                ResultDb.Error -> {
                    data
                }

                ResultDb.Loading -> {
                    data
                }

                is ResultDb.Success<*> -> {
                    data
                }
            }
        }
    }


    fun getHomeData() {
        println("get main data")

        Collections.listOfFavoriteCollections.shuffled().take(4).forEach {
            result[it] = null
        }

        getMoviesByCollectionTopBanned("planned-to-watch-films");
        getMoviesByCollection()
    }



    fun getReviewsById(id: Long) {
        viewModelScope.launch {
            movieRepository.getReviewsById(id).collect {
                _state_movie_reviews.value = when (it) {
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



    fun setViewedToMovie(id:Long, collections: List<String>?, isViewed: Boolean ){
        viewModelScope.launch {
            addMovie(id, collections)
            updateMovieViewedUseCase(id, isViewed)
        }
    }

    fun setBookmarkToMovie(id:Long, collections: List<String>?, isBookmark: Boolean ){
        viewModelScope.launch {
            addMovie(id, collections)
            updateMovieBookmarkUseCase(id, isBookmark)
        }
    }

    /*fun addMovieToViewedDatabase(
        id: Long,
        collections: List<String>?,
        isViewed: Boolean = false,
        isBookmark: Boolean = false
    ) {
        viewModelScope.launch {
            val list = mutableListOf<CollectionMovieDb>()
            if (collections != null) {
                for (slug in collections) {
                    list.add(CollectionMovieDb(movieId = id, collectionSlug = slug))
                }
            }
            addMovieUseCase.invoke(MovieDb(id, isViewed = isViewed, isBookmark = isBookmark), list = list)
        }
    }*/



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
            try {
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
            } catch (e: SocketTimeoutException) {
                _state_collection.value = Result.Error(e)
            }
        }
    }


    fun getMoviesByCollectionTopBanned(slug: String) {

        viewModelScope.launch {
            try {

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
            } catch (e: SocketTimeoutException) {
                _state_movies_collection.value = Result.Error(e)
            }
        }
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
            try {

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

            } catch (e: SocketTimeoutException) {
                _state_movie_home.value = Result.Error(e)
            }
        }
    }

}