package com.example.navwithapinothing_2.features.screen.CollectionsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navwithapinothing_2.data.MovieRepository
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.MovieDb
import com.example.navwithapinothing_2.models.collection.CollectionMovie
import com.example.navwithapinothing_2.usecase.GetMoviesByCollection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 03.07.2025
 */
@HiltViewModel
class CollectionsViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val getMoviesByCollection: GetMoviesByCollection,
) : ViewModel() {

    private val _state = MutableStateFlow(CollectionsState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CollectionsEffect>()
    val effect = _effect.asSharedFlow()


    init {
        getCollections()
    }

    fun onIntent(intent: CollectionsIntent) {
        when (intent) {
            is CollectionsIntent.OnSelectCollection -> {
                onSelectCollection(intent.name, intent.slug)
            }

            CollectionsIntent.OnBack -> {
                onBack()
            }
        }
    }

    fun getCollections() {
        viewModelScope.launch {

            movieRepository.getCollections().collect {
                when (val data = it) {

                    is Result.Loading -> {

                    }

                    is Result.Error<*> -> {

                    }

                    is Result.Success<*> -> {

                        println("list = " + (data.data as List<CollectionMovie>).size)

                        _state.update { it.copy(countCollection = data.data.size) }
                        val flows = (data.data as List<CollectionMovie>).map { collection ->
                            getMoviesByCollection.invoke(collection.slug)
                                .filterIsInstance<ResultDb.Success<*>>().map {
                                    collection.copy(viewedCount = (it.data as List<MovieDb>).size)

                                 }
                        }

                        combine(flows){ it.toList()}.onEach {result -> _state.update { it.copy(collectionResult = CollectionsResult.Success(result)) } }.launchIn(viewModelScope)
                        /*combine(flows) {
                            it.toMap()
                        }.onEach { println("it = " + it) }.launchIn(viewModelScope)*/

                        /*_state.update {
                            it.copy(
                                collectionResult = CollectionsResult.Success(
                                    data.data as List<CollectionMovie>
                                )
                            )
                        }*/


                    }

                }

            }

        }
    }

    fun getCountMoviesByCollection(collectionName: String) {
        viewModelScope.launch {

            _state.update { current ->
                current.copy(collectionViewed = current.collectionViewed.toMutableMap().apply {
                    put(collectionName, 0)
                })
            }


            getMoviesByCollection.invoke(collectionName).collect {

                when (val data = it) {
                    ResultDb.Error -> {

                    }

                    ResultDb.Loading -> {

                    }

                    is ResultDb.Success<*> -> {

                        _state.update { current ->
                            current.copy(
                                collectionViewed = current.collectionViewed.toMutableMap().apply {
                                    set(collectionName, (data.data as List<*>).size)
                                })
                        }

                    }
                }


            }
        }
    }


    fun onSelectCollection(name: String, slug: String) {
        viewModelScope.launch {
            _effect.emit(CollectionsEffect.OnSelectCollection(name, slug))
        }
    }

    fun onBack(){
        viewModelScope.launch {
            _effect.emit(CollectionsEffect.OnBack)
        }
    }
}