package com.example.navwithapinothing_2.features.screen.BookmarkMoviesScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.data.ResultDb

import com.example.navwithapinothing_2.features.screen.ViewedMoviesScreen.ViewedMovieEffect
import com.example.navwithapinothing_2.features.screen.ViewedMoviesScreen.ViewedMovieIntent
import com.example.navwithapinothing_2.features.screen.toggle
import com.example.navwithapinothing_2.models.common.ListMoviesResult
import com.example.navwithapinothing_2.models.common.SortDirection
import com.example.navwithapinothing_2.models.common.SortType
import com.example.navwithapinothing_2.models.common.ViewMode
import com.example.navwithapinothing_2.usecase.GetBookmarkMoviesUseCase
import com.example.navwithapinothing_2.usecase.GetMovieByIdsUseCase
import com.example.navwithapinothing_2.usecase.GetViewedMoviesUseCase
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
 * @Date: 09.07.2025
 */
@HiltViewModel
class BookmarkMoviesViewModel @Inject constructor(
    private val getBookmarkMoviesUseCase: GetBookmarkMoviesUseCase,
    private val getMovieByIdsUseCase: GetMovieByIdsUseCase
): ViewModel() {

    private val _state = MutableStateFlow(BookmarkMoviesState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<BookmarkMoviesEffect>()
    val effect = _effect.asSharedFlow()

    init {
        getBookmarkMovies()
    }

    fun onIntent(intent: BookmarkMoviesIntent) {
        when (intent) {
            BookmarkMoviesIntent.OnBack -> {
                onBack()
            }
            is BookmarkMoviesIntent.OnSelectMovie -> {
                onSelectMovie(intent.id)
            }

            is BookmarkMoviesIntent.IsShowFilters -> {
                _state.update { it.copy(isShowFilter = intent.isShow) }
            }
            BookmarkMoviesIntent.SetGridView -> {
                _state.update { it.copy(viewMode = ViewMode.GRID) }
            }
            BookmarkMoviesIntent.SetListView -> {
                _state.update { it.copy(viewMode = ViewMode.LIST) }
            }
            is BookmarkMoviesIntent.SetSortType -> {
                _state.update { it.copy(sortType = intent.sort) }
            }
            BookmarkMoviesIntent.SortMovies -> {
                sortMovies()
            }
            BookmarkMoviesIntent.ToggleSortDirection -> {
                _state.update { it.copy(sortDirection = _state.value.sortDirection.toggle()) }
            }
        }
    }

    fun getBookmarkMovies() {
        viewModelScope.launch {
            getBookmarkMoviesUseCase().collect { resultDatabase ->

                if (resultDatabase is ResultDb.Success) {
                    _state.update { it.copy(countMovies = (resultDatabase.data as List<*>).size) }

                    getBookmarkMoviesApi(resultDatabase.data.map { it.movieId })

                }

            }
        }
    }

    suspend fun getBookmarkMoviesApi(list: List<Long>) {

        getMovieByIdsUseCase(list).collect { result ->
            if (result is Result.Success<*>) {
                val sorted =
                    list.mapNotNull { id -> (result.data as List<MovieDTO>).find { item -> item.id == id } }

                _state.update { it.copy(list = ListMoviesResult.Success(sorted)) }

            } else if (result is Result.Error<*>) {

            }

        }
    }

    fun sortMovies() {


        when (_state.value.sortType) {
            SortType.DATE -> {
                if (_state.value.sortDirection == SortDirection.DESCENDING) {
                    _state.update {
                        it.copy(
                            list = ListMoviesResult.Success(
                                (_state.value.list as ListMoviesResult.Success).list.sortedBy { if (!it.isSeries!!) it.year else if (it.releaseYears != null) it.releaseYears[0].start else null }
                                    .reversed()
                            ))
                    }

                } else {

                    _state.update {
                        it.copy(
                            list = ListMoviesResult.Success(
                                (_state.value.list as ListMoviesResult.Success).list.sortedBy { if (!it.isSeries!!) it.year else if (it.releaseYears != null) it.releaseYears[0].start else null }
                            ))
                    }

                }
            }

            SortType.NAME -> {

                if (_state.value.sortDirection == SortDirection.DESCENDING)
                    _state.update {
                        it.copy(
                            list = ListMoviesResult.Success(
                                (_state.value.list as ListMoviesResult.Success).list.sortedBy { it.name }
                                    .reversed()
                            ))
                    }
                else {

                    _state.update {
                        it.copy(
                            list = ListMoviesResult.Success(
                                (_state.value.list as ListMoviesResult.Success).list.sortedBy { it.name }
                            ))
                    }


                }

            }

            SortType.RATING -> {
                if (_state.value.sortDirection == SortDirection.DESCENDING) {

                    _state.update {
                        it.copy(
                            list = ListMoviesResult.Success(
                                (_state.value.list as ListMoviesResult.Success).list.sortedBy { it.rating?.kp }
                                    .reversed()
                            ))
                    }
                } else {

                    _state.update {
                        it.copy(
                            list = ListMoviesResult.Success(
                                (_state.value.list as ListMoviesResult.Success).list.sortedBy { it.rating?.kp }
                            ))
                    }


                }
            }

            SortType.NONE -> {

            }
        }

    }

    fun onSelectMovie(id: Long){
        viewModelScope.launch {
            _effect.emit(BookmarkMoviesEffect.OnSelectMovie(id))
        }
    }

    fun onBack(){
        viewModelScope.launch {
            _effect.emit(BookmarkMoviesEffect.OnBack)
        }
    }

}