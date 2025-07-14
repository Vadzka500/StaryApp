package com.example.navwithapinothing_2.features.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.util.CoilUtils
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

) :
    ViewModel() {

    val isNavBarVisible = mutableStateOf(true)



    private val _state = MutableStateFlow(Result.Loading)
    val state = _state.asStateFlow()

    private val _state_random = MutableStateFlow<Result>(Result.Loading)
    val state_random = _state_random.asStateFlow()

    private val _state_collection = MutableStateFlow<Result>(Result.Loading)
    val state_collection = _state_collection.asStateFlow()


    init {

        println("init block")

    }







}