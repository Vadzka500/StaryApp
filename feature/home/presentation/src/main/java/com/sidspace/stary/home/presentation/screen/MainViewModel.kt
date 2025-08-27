package com.sidspace.stary.home.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.home.domain.usecase.GetCollectionUseCase
import com.sidspace.stary.home.domain.usecase.InitDefaultFoldersUseCase
import com.sidspace.stary.home.domain.usecase.GetListMoviesByCollectionUseCase
import com.sidspace.stary.home.presentation.utils.MoviesCollections
import com.sidspace.stary.ui.mapper.toCollectionUi
import com.sidspace.stary.ui.mapper.toMoviePreviewUi
import com.sidspace.stary.ui.model.MoviePreviewUi
import com.sidspace.stary.ui.model.ResultData


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



@HiltViewModel
class MainViewModel @Inject constructor(
    val getListMoviesByCollectionUseCase: GetListMoviesByCollectionUseCase,
    val getCollectionUseCase: GetCollectionUseCase,
    val initDefaultFoldersUseCase: InitDefaultFoldersUseCase
) : ViewModel() {


    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MainEffect>()
    val effect = _effect.asSharedFlow()

    private var result = mutableMapOf<Pair<String, String>, ResultData<List<MoviePreviewUi>>?>()


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

    fun onSelectCollections() {
        viewModelScope.launch {
            _effect.emit(MainEffect.OsSelectCollections)
        }
    }

    fun onSelectCollection(name: String, slug: String) {
        viewModelScope.launch {
            _effect.emit(MainEffect.OnSelectCollection(name, slug))
        }
    }

    fun toErrorScreen() {
        viewModelScope.launch {
            _effect.emit(MainEffect.ToErrorScreen)
        }
    }

    fun onSelectMovie(id: Long) {
        viewModelScope.launch {
            _effect.emit(MainEffect.OnSelectMovie(id))
        }
    }


    fun getHomeData() {

        MoviesCollections.Companion.listOfFavoriteCollections.shuffled().take(5).forEach {
            result[it] = null
        }

        getMoviesByCollectionTopBanned("planned-to-watch-films");
        getMoviesByCollection()
    }


    fun getMoviesByCollectionTopBanned(slug: String) {

        viewModelScope.launch {

            getListMoviesByCollectionUseCase(slug = slug, limit = 250).collect {
                when (val data = it) {
                    is Result.Error -> {
                        _state.update { it.copy(listTopBanned = ResultData.Error) }
                    }

                    Result.Loading -> {

                    }

                    is Result.Success -> {
                        _state.update { it.copy(listTopBanned = ResultData.Success(data.data.map { it.toMoviePreviewUi() })) }
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
                    getListMoviesByCollectionUseCase(result.keys.elementAt(3).second, limit = 10),
                    getListMoviesByCollectionUseCase(result.keys.elementAt(4).second, limit = 10)
                )
                { list1, list2, list3, list4, list5 ->

                    listOf(list1, list2, list3, list4, list5)


                }.collect { data ->
                    data.forEachIndexed { index, item ->
                        when (val res = data[index]) {

                            is Result.Success -> {
                                result[result.keys.elementAt(index)] =
                                    ResultData.Success(res.data.map { it.toMoviePreviewUi() })
                            }

                            else -> {

                            }
                        }
                    }



                    _state.update { it.copy(listHomePage = ResultData.Success(result)) }

                }

            } catch (e: SocketTimeoutException) {
                e.printStackTrace()
                _state.update { it.copy(listHomePage = ResultData.Error) }
            }
        }
    }

    fun getCollections() {
        viewModelScope.launch {

            getCollectionUseCase().collect {

                when (val data = it) {
                    is Result.Error -> {

                        _state.update { it.copy(listCollection = ResultData.Error) }
                    }

                    Result.Loading -> {

                    }

                    is Result.Success -> {
                        _state.update { it.copy(listCollection = ResultData.Success(data.data.map{it.toCollectionUi()})) }
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