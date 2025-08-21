package com.sidspace.stary.bookmark.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidspace.stary.domain.model.Result


import com.example.ui.enum.ViewMode
import com.example.ui.enum.toggle
import com.sidspace.stary.ui.mapper.toMovieData
import com.example.ui.model.ResultData
import com.example.ui.sort.sortListMovies
import com.sidspace.stary.bookmark.domain.usecase.GetBookmarkMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BookmarkMoviesViewModel @Inject constructor(
    private val getBookmarkMoviesUseCase: GetBookmarkMoviesUseCase
) : ViewModel() {

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

            BookmarkMoviesIntent.ToErrorScreen -> {
                toErrorScreen()
            }
        }
    }

    fun getBookmarkMovies() {
        viewModelScope.launch {
            getBookmarkMoviesUseCase().collect { result ->

                if (result is Result.Success) {
                    _state.update { it.copy(countMovies = result.data.size) }

                    /* val sorted =
                         result.data.mapNotNull { id -> (result.data as List<MovieUi>).find { item -> item.id == id } }*/

                    _state.update { it.copy(list = ResultData.Success(result.data.map { it.toMovieData() })) }

                } else if (result is Result.Error) {
                    _state.update { it.copy(list = ResultData.Error) }
                }

            }
        }
    }

    fun sortMovies() {

        _state.update {
            it.copy(
                list = ResultData.Success(
                    sortListMovies(
                        list = (_state.value.list as ResultData.Success).data,
                        sortDirection = _state.value.sortDirection,
                        sortType = _state.value.sortType
                    )
                )
            )
        }

    }

    fun onSelectMovie(id: Long) {
        viewModelScope.launch {
            _effect.emit(BookmarkMoviesEffect.OnSelectMovie(id))
        }
    }

    fun toErrorScreen() {
        viewModelScope.launch {
            _effect.emit(BookmarkMoviesEffect.ToErrorScreen)
        }
    }

    fun onBack() {
        viewModelScope.launch {
            _effect.emit(BookmarkMoviesEffect.OnBack)
        }
    }

}