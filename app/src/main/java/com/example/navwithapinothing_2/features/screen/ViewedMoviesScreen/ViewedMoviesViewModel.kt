package com.example.navwithapinothing_2.features.screen.ViewedMoviesScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.MovieDb
import com.example.navwithapinothing_2.features.screen.AccountScreen.ResultAccountData

import com.example.navwithapinothing_2.features.screen.MovieScreen.ResultMovie
import com.example.navwithapinothing_2.features.screen.toggle
import com.example.navwithapinothing_2.models.common.ListMoviesResult
import com.example.navwithapinothing_2.models.common.SortDirection
import com.example.navwithapinothing_2.models.common.SortType
import com.example.navwithapinothing_2.models.common.ViewMode
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
 * @Date: 08.07.2025
 */
@HiltViewModel
class ViewedMoviesViewModel @Inject constructor(
    private val getViewedMoviesUseCase: GetViewedMoviesUseCase,
    private val getMovieByIdsUseCase: GetMovieByIdsUseCase
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
        }
    }

    fun getViewedMovies() {
        viewModelScope.launch {
            getViewedMoviesUseCase().collect { resultDatabase ->

                if (resultDatabase is ResultDb.Success) {
                    _state.update { it.copy(countMovies = (resultDatabase.data as List<*>).size) }

                    getVisibleMoviesApi(resultDatabase.data.map { it.movieId })

                }

            }
        }
    }

    suspend fun getVisibleMoviesApi(list: List<Long>) {

        getMovieByIdsUseCase(list).collect { result ->
            if (result is Result.Success<*>) {
                val sorted =
                    list.mapNotNull { id -> (result.data as List<MovieDTO>).find { item -> item.id == id } }

                _state.update { it.copy(list = ListMoviesResult.Success(sorted)) }

            } else if (result is Result.Error<*>) {

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

    fun onSelectMovie(id: Long){
        viewModelScope.launch {
            _effect.emit(ViewedMovieEffect.OnSelectMovie(id))
        }
    }

    fun onBack(){
        viewModelScope.launch {
            _effect.emit(ViewedMovieEffect.OnBack)
        }
    }

}