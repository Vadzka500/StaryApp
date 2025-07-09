package com.example.navwithapinothing_2.features.screen.MoviesListScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.util.CoilUtils
import coil.util.CoilUtils.result
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.models.collection.CollectionMovie
import com.example.navwithapinothing_2.models.common.ListMoviesResult
import com.example.navwithapinothing_2.usecase.GetCollectionUseCase
import com.example.navwithapinothing_2.usecase.GetListMoviesByCollectionUseCase
import com.example.navwithapinothing_2.usecase.InitDefaultFoldersUseCase
import com.example.navwithapinothing_2.utils.Collections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.collections.set

/**
 * @Author: Vadim
 * @Date: 09.07.2025
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getListMoviesByCollectionUseCase: GetListMoviesByCollectionUseCase,
    private val getCollectionUseCase: GetCollectionUseCase,
    private val initDefaultFoldersUseCase: InitDefaultFoldersUseCase
) : ViewModel() {



    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MainEffect>()
    val effect = _effect.asSharedFlow()

    private var result = mutableMapOf<Pair<String, String>, Result?>()


    init {
        initFolders()
        getHomeData()
        getCollections()
    }

    fun onIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.OnSelectCollection -> {
                onSelectCollection(intent.name, intent.slug)
            }

            is MainIntent.OnSelectMovie -> {
                onSelectMovie(intent.id)
            }

            MainIntent.OsSelectCollections -> {
                onSelectCollections()
            }

            MainIntent.ToErrorScreen -> {
                toErrorScreen()
            }
        }
    }

    fun onSelectCollections(){
        viewModelScope.launch {
            _effect.emit(MainEffect.OsSelectCollections)
        }
    }

    fun onSelectCollection(name: String, slug: String){
        viewModelScope.launch {
            _effect.emit(MainEffect.OnSelectCollection(name, slug))
        }
    }

    fun toErrorScreen(){
        viewModelScope.launch {
            _effect.emit(MainEffect.ToErrorScreen)
        }
    }

    fun onSelectMovie(id: Long){
        viewModelScope.launch {
            _effect.emit(MainEffect.OnSelectMovie(id))
        }
    }



    fun getHomeData() {

        Collections.listOfFavoriteCollections.shuffled().take(4).forEach {
            result[it] = null
        }

        getMoviesByCollectionTopBanned("planned-to-watch-films");
        getMoviesByCollection()
    }


    fun getMoviesByCollectionTopBanned(slug: String) {

        viewModelScope.launch {

            getListMoviesByCollectionUseCase(slug = slug, limit = 250).collect {
                when (val data = it) {
                    is Result.Error<*> -> {
                        _state.update { it.copy(listTopBanned = ListMoviesResult.Error(data.data as String)) }
                    }

                    Result.Loading -> {

                    }

                    is Result.Success<*> -> {
                        _state.update { it.copy(listTopBanned = ListMoviesResult.Success(data.data as List<MovieDTO>)) }
                    }
                }
            }
        }
    }


    fun getMoviesByCollection() {
        viewModelScope.launch {
            try {

                combine(
                    getListMoviesByCollectionUseCase(result.keys.elementAt(0).second, limit = 10),
                    getListMoviesByCollectionUseCase(result.keys.elementAt(1).second, limit = 10),
                    getListMoviesByCollectionUseCase(result.keys.elementAt(2).second, limit = 10),
                    getListMoviesByCollectionUseCase(result.keys.elementAt(3).second, limit = 10)
                )
                { list1, list2, list3, list4 ->

                    listOf(list1, list2, list3, list4)


                }.collect { data ->

                    result[result.keys.elementAt(0)] = data[0]
                    result[result.keys.elementAt(1)] = data[1]
                    result[result.keys.elementAt(2)] = data[2]
                    result[result.keys.elementAt(3)] = data[3]

                    _state.update { it.copy(listHomePage = ListHomePageResult.Success(result)) }

                }

            } catch (e: SocketTimeoutException) {
                e.printStackTrace()
                _state.update { it.copy(listHomePage = ListHomePageResult.Error) }
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
                        _state.update { it.copy(listCollection = ListCollectionResult.Success(data.data as List<CollectionMovie>)) }
                    }
                }
            }

        }
    }

    fun initFolders() {
        viewModelScope.launch {
            initDefaultFoldersUseCase()
        }
    }
}