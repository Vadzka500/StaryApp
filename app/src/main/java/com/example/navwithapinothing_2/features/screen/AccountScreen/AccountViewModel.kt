package com.example.navwithapinothing_2.features.screen.AccountScreen

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.data.MovieRepository
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.MovieDb
import com.example.navwithapinothing_2.usecase.GetBookmarkMoviesUseCase
import com.example.navwithapinothing_2.usecase.GetMovieByIdsUseCase
import com.example.navwithapinothing_2.usecase.GetViewedMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 25.06.2025
 */
@HiltViewModel
class AccountViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val getViewedMoviesUseCase: GetViewedMoviesUseCase,
    private val getBookmarkMoviesUseCase: GetBookmarkMoviesUseCase,
    private val getMovieByIdsUseCase: GetMovieByIdsUseCase

) : ViewModel() {

    private val _state = MutableStateFlow(AccountState())
    val state: StateFlow<AccountState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AccountEffect>()
    val effect: SharedFlow<AccountEffect> = _effect.asSharedFlow()

    init {
        println("init account")
        getViewedAndBookmarkMovies()
    }

    fun getViewedAndBookmarkMovies() {
        viewModelScope.launch {

            combine(
                getViewedMoviesUseCase(),
                getBookmarkMoviesUseCase()
            ) { viewedResult, bookmarkResult -> viewedResult to bookmarkResult }.collect { (viewed, bookmark) ->

                if (viewed is ResultDb.Success) {
                    _state.update { it.copy(countViewed = viewed.data.size) }

                    if (!viewed.data.isEmpty()) {
                        getVisibleMoviesApi(viewed.data.map { it.movieId }.take(10))
                    } else _state.update {
                        it.copy(
                            resultAccountViewed = ResultAccountData.Success(
                                emptyList()
                            )
                        )
                    }
                    updateHintState()
                }

                if (bookmark is ResultDb.Success) {
                    _state.update { it.copy(countBookmark = bookmark.data.size) }

                    if (!bookmark.data.isEmpty()) {
                        getBookmarkMoviesApi(bookmark.data.map { it.movieId }.take(10))
                    } else _state.update {
                        it.copy(
                            resultAccountBookmark = ResultAccountData.Success(
                                emptyList()
                            )
                        )
                    }
                    updateHintState()
                }


            }

        }
    }

    fun updateHintState() {
        if (_state.value.countViewed == 0 && _state.value.countBookmark == 0) {

            _state.update { it.copy(isShowEmptyHint = true) }
        } else {
            _state.update { it.copy(isShowEmptyHint = false) }
        }
    }


    fun onIntent(intent: AccountIntent) {
        when (val data = intent) {
            AccountIntent.ToFoldersScreen -> toCollectionScreen()
            is AccountIntent.ToMovieScreen -> {
                toMovieScreen(data.id)
            }
        }
    }

    fun getVisibleMoviesApi(list: List<Long>) {
        viewModelScope.launch {
            _state.update { it.copy(resultAccountViewed = ResultAccountData.Loading) }

            getMovieByIdsUseCase(list).collect { result ->
                if (result is Result.Success<*>) {
                    val sorted =
                        list.mapNotNull { id -> (result.data as List<MovieDTO>).find { item -> item.id == id } }

                    _state.update { it.copy(resultAccountViewed = ResultAccountData.Success(sorted)) }
                } else if (result is Result.Error<*>) {

                }

            }
        }


    }

    fun getBookmarkMoviesApi(list: List<Long>) {
        viewModelScope.launch {
            _state.update { it.copy(resultAccountBookmark = ResultAccountData.Loading) }

            getMovieByIdsUseCase(list).collect { result ->
                if (result is Result.Success<*>) {
                    val sorted =
                        list.mapNotNull { id -> (result.data as List<MovieDTO>).find { item -> item.id == id } }


                    _state.update { it.copy(resultAccountBookmark = ResultAccountData.Success(sorted)) }
                } else if (result is Result.Error<*>) {

                }

            }
        }

    }

    private fun toCollectionScreen() {
        viewModelScope.launch {
            _effect.emit(AccountEffect.ToFoldersScreen)
        }
    }

    private fun toMovieScreen(id: Long) {
        viewModelScope.launch {
            _effect.emit(AccountEffect.ToMovieScreen(id))
        }
    }

}