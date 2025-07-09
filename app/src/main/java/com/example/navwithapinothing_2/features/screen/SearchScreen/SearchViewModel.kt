package com.example.navwithapinothing_2.features.screen.SearchScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapi.models.Response
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.data.MovieRepository
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.models.common.ViewMode
import com.example.navwithapinothing_2.usecase.GetSearchedMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 08.07.2025
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchedMoviesUseCase: GetSearchedMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SearchEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            state.map { it.searchStr }.collectLatest {
                getSearchMovies(it)
            }
        }

    }


    fun onIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.UpdateSearchStr -> {
                _state.update { it.copy(searchStr = intent.str) }
            }

            SearchIntent.SetGridViewMode -> {
                _state.update { it.copy(viewMode = ViewMode.GRID) }
            }

            SearchIntent.SetListViewMode -> {
                _state.update { it.copy(viewMode = ViewMode.LIST) }
            }

            is SearchIntent.IsShowFilter -> {
                _state.update { it.copy(isVisibleFilter = intent.isShow) }
            }

            is SearchIntent.OnSelectMovie -> {
                onSelectMovie(intent.id)
            }
        }
    }

    suspend fun getSearchMovies(str: String) {
        println("search movie")

        getSearchedMoviesUseCase(str).collect {
            when (val data = it) {
                is Result.Error<*> -> {

                }

                Result.Loading -> {

                }

                is Result.Success<*> -> {
                    _state.update { it.copy(list = SearchResult.Success((data.data as Response<*>).docs as List<MovieDTO>)) }
                }
            }
            //_state_movie_search.value = it
        }

    }

    fun onSelectMovie(id: Long) {
        viewModelScope.launch {
            _effect.emit(SearchEffect.OnSelectMovie(id))
        }
    }
}