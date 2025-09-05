package com.sidspace.stary.collectionmovies.presentation.screen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidspace.stary.collectionmovies.domain.usecase.GetListMoviesByCollectionUseCase
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.ui.enums.ViewMode
import com.sidspace.stary.ui.enums.toggle
import com.sidspace.stary.ui.mapper.toMovieData
import com.sidspace.stary.ui.model.ResultData
import com.sidspace.stary.ui.sort.sortListMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ListMoviesViewModel @Inject constructor(

    private val getListMoviesByCollectionUseCase: GetListMoviesByCollectionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ListMoviesState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ListMoviesEffect>()
    val effect = _effect.asSharedFlow()

    fun onIntent(intent: ListMoviesIntent) {
        when (intent) {
            is ListMoviesIntent.OnBack -> {
                onBack()
            }

            is ListMoviesIntent.OnSelectMovie -> {
                onSelectMovie(intent.id)
            }

            is ListMoviesIntent.IsShowFilter -> {
                _state.update { it.copy(isShowFilter = intent.isShow) }
            }

            ListMoviesIntent.SetGridViewMode -> {
                _state.update { it.copy(viewMode = ViewMode.GRID) }
            }

            ListMoviesIntent.SetListViewMode -> {
                _state.update { it.copy(viewMode = ViewMode.LIST) }
            }

            is ListMoviesIntent.SetSortType -> {
                _state.update { it.copy(sortType = intent.sort) }
            }

            ListMoviesIntent.ToggleSortDirection -> {
                _state.update { it.copy(sortDirection = _state.value.sortDirection.toggle()) }
            }

            ListMoviesIntent.SortMovies -> {
                sortMovies()
            }

            ListMoviesIntent.ToErrorScreen -> {
                toErrorScreen()
            }
        }
    }

    fun getMoviesByCollection(slug: String, limit: Int = 30) {
        viewModelScope.launch {

            getListMoviesByCollectionUseCase(slug, limit).collect {
                when (val data = it) {
                    is Result.Error -> {
                        _state.update { it.copy(list = ResultData.Error) }
                    }

                    Result.Loading -> {

                    }

                    is Result.Success -> {
                        _state.update { it.copy(list = ResultData.Success(data.data.map { it.toMovieData() })) }
                    }
                }
            }

        }
    }

    private fun sortMovies() {

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
            _effect.emit(ListMoviesEffect.OnSelectMovie(id))
        }
    }

    fun toErrorScreen() {
        viewModelScope.launch {
            _effect.emit(ListMoviesEffect.ToErrorScreen)
        }
    }

    fun onBack() {
        viewModelScope.launch {
            _effect.emit(ListMoviesEffect.OnBack)
        }
    }


}

