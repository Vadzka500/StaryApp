package com.sidspace.stary.viewed.presentation.screen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.ui.enums.ViewMode
import com.sidspace.stary.ui.enums.toggle
import com.sidspace.stary.ui.mapper.toMovieData
import com.sidspace.stary.ui.model.ResultData
import com.sidspace.stary.ui.sort.sortListMovies
import com.sidspace.stary.viewed.domain.usecase.GetViewedMoviesFromDbUseCase
import com.sidspace.stary.viewed.domain.usecase.GetViewedMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewedMoviesViewModel @Inject constructor(
    private val getViewedMoviesUseCase: GetViewedMoviesUseCase,
    private val getViewedMoviesFromDb: GetViewedMoviesFromDbUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ViewedMovieState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ViewedMovieEffect>()
    val effect = _effect.asSharedFlow()


    init {
        getViewedMovies()
    }

    fun onIntent(intent: ViewedMovieIntent) {
        when (intent) {
            ViewedMovieIntent.OnBack -> {
                onBack()
            }

            is ViewedMovieIntent.OnSelectMovie -> {
                onSelectMovie(intent.id)
            }

            is ViewedMovieIntent.IsShowFilters -> {
                _state.update { it.copy(isShowFilter = intent.isShow) }
            }

            ViewedMovieIntent.SetGridView -> {
                _state.update { it.copy(viewMode = ViewMode.GRID) }
            }

            ViewedMovieIntent.SetListView -> {
                _state.update { it.copy(viewMode = ViewMode.LIST) }
            }

            is ViewedMovieIntent.SetSortType -> {
                _state.update { it.copy(sortType = intent.sort) }
            }

            ViewedMovieIntent.SortMovies -> {
                sortMovies()
            }

            ViewedMovieIntent.ToggleSortDirection -> {
                _state.update { it.copy(sortDirection = _state.value.sortDirection.toggle()) }
            }

            ViewedMovieIntent.OnError -> {
                toErrorScreen()
            }
        }
    }

    fun getViewedMovies() {
        viewModelScope.launch {

            getViewedMoviesFromDb().collect { resultDb ->

                if (resultDb is Result.Success) {
                    _state.update { it.copy(countMovies = resultDb.data.size) }

                    if (resultDb.data.isNotEmpty()) {
                        getViewedMoviesUseCase(resultDb.data).collect { result ->

                            if (result is Result.Success) {

                                _state.update {
                                    it.copy(
                                        list = ResultData.Success(
                                            result.data.map { it.toMovieData() })
                                    )
                                }

                            } else if (result is Result.Error) {
                                _state.update { it.copy(list = ResultData.Error) }
                            }

                        }
                    } else {
                        _state.update { it.copy(list = ResultData.Success(emptyList())) }
                    }

                } else if (resultDb is Result.Error) {
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
            _effect.emit(ViewedMovieEffect.OnSelectMovie(id))
        }
    }

    private fun toErrorScreen() {
        viewModelScope.launch {
            _effect.emit(ViewedMovieEffect.ToErrorScreen)
        }
    }

    fun onBack() {
        viewModelScope.launch {
            _effect.emit(ViewedMovieEffect.OnBack)
        }
    }

}
