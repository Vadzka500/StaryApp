package com.sidspace.stary.movie.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.sidspace.stary.domain.model.LocalMovie
import com.sidspace.stary.domain.model.LocalResult
import com.sidspace.stary.domain.model.Result
import com.example.domain.usecase.folder.GetAllFoldersUseCase
import com.example.domain.usecase.movie.AddMovieToFolderUseCase
import com.example.domain.usecase.movie.AddMovieUseCase
import com.example.domain.usecase.movie.CheckMovieUseCase
import com.example.domain.usecase.movie.GetMovieByIdUseCase
import com.example.domain.usecase.movie.RemoveMovieFromFolderUseCase
import com.sidspace.stary.movie.domain.usecase.UpdateMovieBookmarkUseCase
import com.sidspace.stary.movie.domain.usecase.UpdateMovieViewedUseCase

import com.sidspace.stary.ui.mapper.toMovieData

import com.sidspace.stary.movie.presentation.mapper.toLocalMovieUi
import com.sidspace.stary.ui.model.ResultData

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieProfileViewModel @Inject constructor(

    private val checkMovieUseCase: CheckMovieUseCase,
    private val updateMovieViewedUseCase: UpdateMovieViewedUseCase,
    private val updateMovieBookmarkUseCase: UpdateMovieBookmarkUseCase,
    private val addMovieUseCase: AddMovieUseCase,
    private val addMovieToFolderUseCase: AddMovieToFolderUseCase,
    private val removeMovieFromFolderUseCase: RemoveMovieFromFolderUseCase,
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    private val getMovieByIdUseCase: GetMovieByIdUseCase

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
                if ((_state.value.filters as ResultData.Success).data.first { it.id == intent.id }.listOfMovies!!.any { it.id == intent.movie.id }) {
                    removeMovieFromFolder(intent.movie.id, intent.id)
                } else {
                    addMovieToFolder(intent.movie.id, intent.id, intent.movie.collections)
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

            MovieIntent.OnError -> {
                toErrorScreen()
            }
        }
    }

    fun getMovieById(id: Long) {

        viewModelScope.launch {
            getMovieByIdUseCase(id).collect{
                when (val data = it) {
                    is Result.Error -> {
                        _state.update { it.copy(movie = ResultData.Error) }
                    }

                    Result.Loading -> {

                    }
                    is Result.Success -> {
                        _state.update { it.copy(movie = ResultData.Success(data.data.toMovieData())) }
                    }
                }
            }
        }
    }

    fun checkMovieDatabase(id: Long) {

        viewModelScope.launch {

            checkMovieUseCase.invoke(id).collect {


                when (val data = it) {


                    LocalResult.Error -> {

                    }
                    LocalResult.Loading -> {

                    }
                    is LocalResult.Success -> {
                        _state.update { it.copy(isExistMovieDb = data.data?.toLocalMovieUi()) }

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

                if(result is Result.Success){
                    _state.update { it.copy(filters = ResultData.Success(result.data)) }
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

    private suspend fun addMovie(movieId: Long, collections: List<String>?){
        addMovieUseCase.invoke(LocalMovie(movieId = movieId), list = collections)
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

    private fun toErrorScreen(){
        viewModelScope.launch {
            _effect.emit(MovieEffect.ToErrorScreen)
        }
    }

    private fun playTrailer(url: String){
        viewModelScope.launch {
            _effect.emit(MovieEffect.PlayTrailer(url))
        }
    }

}