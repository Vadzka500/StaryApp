package com.sidspace.stary.search.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidspace.stary.search.domain.usecase.GetSearchedMoviesUseCase
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.ui.enums.ViewMode
import com.sidspace.stary.ui.mapper.toMovieData
import com.sidspace.stary.ui.model.ResultData
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

            SearchIntent.OnError -> {
                toErrorScreen()
            }
        }
    }

    suspend fun getSearchMovies(str: String) {

        getSearchedMoviesUseCase(str).collect {
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

    private fun toErrorScreen() {
        viewModelScope.launch {
            _effect.emit(SearchEffect.ToErrorScreen)
        }
    }

    fun onSelectMovie(id: Long) {
        viewModelScope.launch {
            _effect.emit(SearchEffect.OnSelectMovie(id))
        }
    }
}
