package com.example.navwithapinothing_2.features.screen.RandomScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.data.MovieRepository
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.features.screen.MoviesListScreen.ListCollectionResult
import com.example.navwithapinothing_2.models.collection.CollectionMovie
import com.example.navwithapinothing_2.usecase.GetCollectionUseCase
import com.example.navwithapinothing_2.utils.RandomFiltersOption
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
 * @Date: 10.07.2025
 */
@HiltViewModel
class RandomViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val getCollectionUseCase: GetCollectionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RandomState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<RandomEffect>()
    val effect = _effect.asSharedFlow()

    init {
        getCollections()
    }

    fun onIntent(intent: RandomIntent) {
        when (intent) {
            RandomIntent.Random -> {
                getRandom(filter = _state.value.filter)
            }

            is RandomIntent.UpdateCurrentPage -> {
                _state.update {
                    it.copy(
                        initialPage = intent.currentPage,
                        currentPageOffSetFraction = intent.currentPageOffSetFraction
                    )
                }
            }

            is RandomIntent.IsSearch -> {
                _state.update { it.copy(isSearch = intent.isSearch) }
            }

            is RandomIntent.SetShowFilters -> {
                _state.update { it.copy(isFiltersShown = intent.isShown) }
            }

            is RandomIntent.FilterIntent.AddGenre -> {

                _state.update {
                    val list = it.filter.listOfGenres?.toMutableList() ?: mutableListOf()
                    list.add(intent.genre)

                    it.copy(
                        filter = it.filter.copy(
                            listOfGenres = list
                        )
                    )

                }

            }

            is RandomIntent.FilterIntent.AddType -> {
                _state.update {
                    val list = it.filter.listOfType?.toMutableMap() ?: mutableMapOf()
                    list.put(intent.type.first, intent.type.second)

                    it.copy(filter = it.filter.copy(listOfType = list))
                }
            }

            RandomIntent.FilterIntent.ClearGenres -> {
                _state.update {
                    it.copy(filter = it.filter.copy(listOfGenres = null))
                }
            }

            RandomIntent.FilterIntent.ClearTypes -> {
                _state.update {
                    it.copy(filter = it.filter.copy(listOfType = null))
                }
            }
            is RandomIntent.FilterIntent.SetScore -> _state.update {
                it.copy(filter = it.filter.copy(listOfScore = intent.listOfScore))
            }
            is RandomIntent.FilterIntent.SetYears -> {
                _state.update {
                    it.copy(filter = it.filter.copy(years = intent.listOfYears))
                }
            }

            is RandomIntent.FilterIntent.RemoveGenre -> {
                _state.update {
                    var list = it.filter.listOfGenres!!.toMutableList()
                    list.remove(intent.genre)
                    it.copy(filter = it.filter.copy(listOfGenres = list))
                }
            }
            is RandomIntent.FilterIntent.RemoveType ->{
                _state.update {
                    val list = it.filter.listOfType!!.toMutableMap()
                    list.remove(intent.type)

                    it.copy(filter = it.filter.copy(listOfType = list))
                }
            }
        }
    }

    fun getRandom(filter: RandomFiltersOption? = null) {
        println("rand")
        viewModelScope.launch {

            //delay(5000)
            //_state_random.value = Result.Loading
            _state.update { it.copy(randomMovie = MovieRandomStatus.Loading) }
            repository.getRandom(filter).collect { result ->
                when (val data = result) {

                    is Result.Loading -> {
                        _state.update { it.copy(randomMovie = MovieRandomStatus.Loading) }
                    }

                    is Result.Error<*> -> {
                        _state.update { it.copy(randomMovie = MovieRandomStatus.Error) }
                    }

                    is Result.Success<*> -> {
                        _state.update { it.copy(randomMovie = MovieRandomStatus.Success(data.data as MovieDTO)) }
                    }

                }

            }
        }
    }

    fun getCollections() {
        viewModelScope.launch {

            getCollectionUseCase().collect {
                when (val data = it) {
                    is Result.Error<*> -> {

                    }

                    Result.Loading -> {

                    }

                    is Result.Success<*> -> {
                        _state.update {
                            it.copy(
                                listOfCollections = ListCollectionResult.Success(
                                    data.data as List<CollectionMovie>
                                )
                            )
                        }
                    }
                }
            }

        }
    }
}