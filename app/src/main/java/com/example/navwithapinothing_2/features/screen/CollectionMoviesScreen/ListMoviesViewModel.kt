package com.example.navwithapinothing_2.features.screen.CollectionMoviesScreen

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapi.models.Response
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.data.MovieRepository
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.features.screen.SearchScreen.SearchResult
import com.example.navwithapinothing_2.features.screen.toggle
import com.example.navwithapinothing_2.models.common.ListMoviesResult
import com.example.navwithapinothing_2.models.common.SortDirection
import com.example.navwithapinothing_2.models.common.SortType
import com.example.navwithapinothing_2.models.common.ViewMode
import com.example.navwithapinothing_2.usecase.GetListMoviesByCollectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 08.07.2025
 */
@HiltViewModel
class ListMoviesViewModel @Inject constructor(
    private val repository: MovieRepository,
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
        }
    }

    fun getMoviesByCollection(slug: String, limit: Int = 30) {
        viewModelScope.launch {

            getListMoviesByCollectionUseCase(slug, limit).collect {
                when (val data = it) {
                    is Result.Error<*> -> {

                    }

                    Result.Loading -> {

                    }

                    is Result.Success<*> -> {
                        _state.update { it.copy(list = ListMoviesResult.Success(data.data as List<MovieDTO>)) }
                    }
                }
            }

        }
    }

    private fun sortMovies() {


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

    fun onSelectMovie(id: Long) {
        viewModelScope.launch {
            _effect.emit(ListMoviesEffect.OnSelectMovie(id))
        }
    }

    fun onBack() {
        viewModelScope.launch {
            _effect.emit(ListMoviesEffect.OnBack)
        }
    }


}

