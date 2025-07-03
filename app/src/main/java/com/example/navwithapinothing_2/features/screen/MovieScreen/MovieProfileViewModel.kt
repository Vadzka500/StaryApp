package com.example.navwithapinothing_2.features.screen.MovieScreen

import android.graphics.Movie
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.data.MovieRepository
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.CollectionMovieDb
import com.example.navwithapinothing_2.database.models.MovieDb
import com.example.navwithapinothing_2.features.screen.FoldersScreen.ResultFilterData
import com.example.navwithapinothing_2.usecase.AddMovieToFolderUseCase
import com.example.navwithapinothing_2.usecase.AddMovieUseCase
import com.example.navwithapinothing_2.usecase.CheckMovieUseCase
import com.example.navwithapinothing_2.usecase.GetAllFoldersUseCase
import com.example.navwithapinothing_2.usecase.RemoveMovieFromFolderUseCase
import com.example.navwithapinothing_2.usecase.UpdateMovieBookmarkUseCase
import com.example.navwithapinothing_2.usecase.UpdateMovieViewedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 28.06.2025
 */
@HiltViewModel
class MovieProfileViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val checkMovieUseCase: CheckMovieUseCase,
    private val updateMovieViewedUseCase: UpdateMovieViewedUseCase,
    private val updateMovieBookmarkUseCase: UpdateMovieBookmarkUseCase,
    private val addMovieUseCase: AddMovieUseCase,
    private val addMovieToFolderUseCase: AddMovieToFolderUseCase,
    private val removeMovieFromFolderUseCase: RemoveMovieFromFolderUseCase,
    private val getAllFoldersUseCase: GetAllFoldersUseCase,

) : ViewModel() {

    private val _state = MutableStateFlow(MovieState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MovieEffect>()
    val effect = _effect.asSharedFlow()

    init {
        getFolders()
    }

    fun onIntent(intent: MovieIntent) {
        when (intent) {
            is MovieIntent.LoadMovie -> {
                println("load")
                getMovieById(intent.id)
                checkMovieDatabase(intent.id)
            }

            is MovieIntent.ToMovieScreen -> {
                toMovieScreen(intent.id)
            }

            is MovieIntent.ToPersonScreen -> {
                toPersonScreen(intent.id)
            }

            is MovieIntent.ToReviewScreen -> {
                toReviewScreen(intent.id)
            }

            is MovieIntent.PlayTrailer -> {
                playTrailer(intent.url)
            }

            is MovieIntent.BookmarkToMovie -> {
                setBookmarkToMovie(intent.id, intent.collections, intent.isBookmark)
            }
            is MovieIntent.ViewedToMovie -> {
                setViewedToMovie(intent.id, intent.collections, intent.isViewed)
            }

            is MovieIntent.OnSelectFolder -> {
                if ((_state.value.filters as ResultFilterData.Success).data.first { it.folder.folderId == intent.id }.movies.any { it.movieId == intent.movie.id }) {
                    removeMovieFromFolder(intent.movie.id!!, intent.id)
                } else {
                    addMovieToFolder(intent.movie.id!!, intent.id, intent.movie.lists)
                }
            }

            MovieIntent.HideFoldersSheet -> {
                _state.update { it.copy(isShowSheetFolders = false) }
            }
            MovieIntent.ShowFoldersSheet -> {
                _state.update { it.copy(isShowSheetFolders = true) }
            }

            MovieIntent.HideTrailerSheet -> {
                _state.update { it.copy(isShowTrailerSheet = false) }
            }
            MovieIntent.ShowTrailerSheet -> {
                _state.update { it.copy(isShowTrailerSheet = true) }
            }
        }
    }

    fun getMovieById(id: Long) {
        println("get movie")

        viewModelScope.launch {


            movieRepository.getMovieById(id).collect {

                when (val data = it) {
                    is Result.Error<*> -> {
                        println("err 1")
                    }

                    Result.Loading -> {
                        println("err 2")
                    }

                    is Result.Success<*> -> {
                        println("err 3")
                        _state.update { it.copy(movie = ResultMovie.Success(data.data as MovieDTO)) }
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
                        println("get loading")
                    }

                    is ResultDb.Success<*> -> {
                        _state.update { it.copy(isExistMovieDb = data.data as MovieDb?) }
                        println("get movie check 2 = " + data.data)


                    }
                }


            }


        }
    }

    fun addMovieToFolder(idMovie: Long, idFolder: Long, collections: List<String>?) {
        viewModelScope.launch {
            addMovie(idMovie, collections)
            addMovieToFolderUseCase(idMovie, idFolder)
        }
    }

    fun getFolders() {
        viewModelScope.launch {
            getAllFoldersUseCase().collect { result ->

                if(result is ResultDb.Success){
                    _state.update { it.copy(filters = ResultFilterData.Success(result.data)) }
                }
            }
        }
    }

    fun removeMovieFromFolder(idMovie: Long, idFolder: Long) {
        viewModelScope.launch {
            removeMovieFromFolderUseCase(idMovie, idFolder)
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

    private suspend fun addMovie(idMovie: Long, collections: List<String>?){

        val list = mutableListOf<CollectionMovieDb>()
        if (collections != null) {
            for (slug in collections) {
                list.add(CollectionMovieDb(movieId = idMovie, collectionSlug = slug))
            }
        }
        addMovieUseCase.invoke(MovieDb(idMovie), list = list)
    }

    private fun toMovieScreen(id: Long){
        viewModelScope.launch {
            _effect.emit(MovieEffect.ToMovieScreen(id))
        }
    }

    private fun toPersonScreen(id: Long){
        viewModelScope.launch {
            _effect.emit(MovieEffect.ToPersonScreen(id))
        }
    }

    private fun toReviewScreen(id: Long){
        viewModelScope.launch {
            _effect.emit(MovieEffect.ToReviewScreen(id))
        }
    }

    private fun playTrailer(url: String){
        viewModelScope.launch {
            _effect.emit(MovieEffect.PlayTrailer(url))
        }
    }

}