package com.sidspace.stary.random.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidspace.stary.domain.RandomFiltersOption
import com.sidspace.stary.domain.model.Result
import com.example.domain.usecase.collection.GetCollectionUseCase
import com.example.domain.usecase.movie.GetRandomMovieUseCase
import com.sidspace.stary.random.presentation.mapper.toCollectionRandomUi

import com.sidspace.stary.ui.mapper.toMovieUi
import com.example.ui.model.ResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RandomViewModel @Inject constructor(
    private val getCollectionUseCase: GetCollectionUseCase,
    private val getRandomMovieUseCase: GetRandomMovieUseCase
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
                updateBadge()
            }

            is RandomIntent.FilterIntent.AddType -> {
                _state.update {
                    val list = it.filter.listOfType?.toMutableMap() ?: mutableMapOf()
                    list.put(intent.type.first, intent.type.second)

                    it.copy(filter = it.filter.copy(listOfType = list))
                }
                updateBadge()
            }

            RandomIntent.FilterIntent.ClearGenres -> {
                _state.update {
                    it.copy(filter = it.filter.copy(listOfGenres = null))
                }
                updateBadge()
            }

            RandomIntent.FilterIntent.ClearTypes -> {
                _state.update {
                    it.copy(filter = it.filter.copy(listOfType = null))
                }
                updateBadge()
            }

            is RandomIntent.FilterIntent.SetScore -> {
                _state.update {
                    it.copy(filter = it.filter.copy(listOfScore = intent.listOfScore))

                }
                updateBadge()
            }

            is RandomIntent.FilterIntent.SetYears -> {
                _state.update {
                    it.copy(filter = it.filter.copy(years = intent.listOfYears))
                }
                updateBadge()
            }

            is RandomIntent.FilterIntent.RemoveGenre -> {
                _state.update {
                    var list = it.filter.listOfGenres!!.toMutableList()
                    list.remove(intent.genre)
                    if (list.isEmpty())
                        it.copy(filter = it.filter.copy(listOfGenres = null))
                    else
                        it.copy(filter = it.filter.copy(listOfGenres = list))
                }
                updateBadge()
            }

            is RandomIntent.FilterIntent.RemoveType -> {
                _state.update {
                    var list = it.filter.listOfType!!.toMutableMap()
                    list.remove(intent.type)
                    if (list.isEmpty())
                        it.copy(filter = it.filter.copy(listOfType = null))
                    else
                        it.copy(filter = it.filter.copy(listOfType = list))
                }
                updateBadge()
            }

            is RandomIntent.FilterIntent.ToggleCollection -> {
                _state.update {
                    val list = it.filter.listOfCollection?.toMutableList() ?: mutableListOf()
                    if (list.contains(intent.slug)) {
                        list.remove(intent.slug)
                    } else {
                        list.add(intent.slug)
                    }
                    if (list.isEmpty())
                        it.copy(filter = it.filter.copy(listOfCollection = null))
                    else
                        it.copy(filter = it.filter.copy(listOfCollection = list))
                }
                updateBadge()
            }
        }
    }

    fun updateBadge() {
        if (_state.value.filter.listOfType != null || _state.value.filter.listOfGenres != null || _state.value.filter.listOfCollection != null || _state.value.filter.listOfScore != null || _state.value.filter.years != null) {
            _state.update { it.copy(isBadgeShown = true) }
        } else {
            _state.update { it.copy(isBadgeShown = false) }
        }
    }

    fun getRandom(filter: RandomFiltersOption? = null) {
        println("rand")
        viewModelScope.launch {


            _state.update { it.copy(randomMovie = ResultData.Loading) }
            getRandomMovieUseCase(filter).collect{ result ->
                when (val data = result) {

                    is Result.Loading -> {
                        _state.update { it.copy(randomMovie = ResultData.Loading) }
                    }

                    is Result.Error -> {
                        _state.update { it.copy(randomMovie = ResultData.Error) }
                    }

                    is Result.Success -> {
                        _state.update { it.copy(randomMovie = ResultData.Success(data.data.toMovieUi())) }
                    }

                }
            }
        }
    }

    fun getCollections() {
        viewModelScope.launch {

            getCollectionUseCase().collect {
                when (val data = it) {
                    is Result.Error -> {

                    }

                    Result.Loading -> {

                    }

                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                listOfCollections = ResultData.Success(
                                    data.data.map{ it.toCollectionRandomUi()}
                                )
                            )
                        }
                    }
                }
            }

        }
    }
}