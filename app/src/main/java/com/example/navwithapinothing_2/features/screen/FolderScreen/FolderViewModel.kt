package com.example.navwithapinothing_2.features.screen.FolderScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.Folder
import com.example.navwithapinothing_2.database.models.FolderWithMovies
import com.example.navwithapinothing_2.features.screen.toggle
import com.example.navwithapinothing_2.models.common.ListMoviesResult
import com.example.navwithapinothing_2.models.common.SortDirection
import com.example.navwithapinothing_2.models.common.SortType
import com.example.navwithapinothing_2.models.common.ViewMode
import com.example.navwithapinothing_2.usecase.GetFolderUseCase
import com.example.navwithapinothing_2.usecase.GetMovieByIdsUseCase
import com.example.navwithapinothing_2.usecase.RemoveFolderUseCase
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
 * @Date: 03.07.2025
 */
@HiltViewModel
class FolderViewModel @Inject constructor(
    private val getFolderUseCase: GetFolderUseCase,
    private val removeFolderUseCase: RemoveFolderUseCase,
    private val getMovieByIdsUseCase: GetMovieByIdsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FolderState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<FolderEffect>()
    val effect = _effect.asSharedFlow()

    fun onIntent(intent: FolderIntent) {
        when (val data = intent) {
            is FolderIntent.LoadFolder -> {
                getFolder(data.id)
            }

            FolderIntent.HideDialog -> {
                _state.update { it.copy(isShowDialog = false) }
            }
            FolderIntent.RemoveFolder -> {
                _state.update { it.copy(isShowDialog = false) }
                removeFolder()
            }
            FolderIntent.ShowDialog -> {
                _state.update { it.copy(isShowDialog = true) }
            }

            is FolderIntent.IsShowFilters -> {
                _state.update { it.copy(isShowFilter = data.isShow) }
            }
            is FolderIntent.OnSelectMovie -> {
                onSelectMovie(data.id)
            }
            FolderIntent.SetGridView -> {
                _state.update { it.copy(viewMode = ViewMode.GRID) }
            }
            FolderIntent.SetListView -> {
                _state.update { it.copy(viewMode = ViewMode.LIST) }
            }
            is FolderIntent.SetSortType -> {
                _state.update { it.copy(sortType = data.sort) }
            }
            FolderIntent.SortMovies -> {
                sortMovies()
            }
            FolderIntent.ToggleSortDirection -> {
                _state.update { it.copy(sortDirection = _state.value.sortDirection.toggle()) }
            }

            FolderIntent.OnBack -> {
                onBack()
            }
        }
    }

    fun removeFolder(){
        viewModelScope.launch {
            when(removeFolderUseCase(_state.value.folder!!.folder)){
                ResultDb.Error -> {

                }
                ResultDb.Loading -> {

                }
                is ResultDb.Success<*> -> {
                    onBack()
                }
            }
        }
    }

    fun getFolder(id: Long) {
        viewModelScope.launch {
            when (val data = getFolderUseCase(id)) {
                ResultDb.Error -> {
                    data
                }

                ResultDb.Loading -> {
                    data
                }

                is ResultDb.Success<*> -> {
                    _state.update { it.copy(folder = data.data as FolderWithMovies) }
                    val count = (data.data as FolderWithMovies).movies.size
                    _state.update { it.copy(countMovies = count) }
                    if(count > 0) {
                        getFolderMovies()
                    }else{
                        _state.update { it.copy(list = ListMoviesResult.Success(emptyList())) }
                    }
                }
            }
        }
    }

    suspend fun getFolderMovies(){
        getMovieByIdsUseCase(_state.value.folder!!.movies.map { it.movieId }).collect{
            when(val data = it){
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

    fun sortMovies() {


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

    fun onBack(){
        viewModelScope.launch {
            _effect.emit(FolderEffect.OnBack)
        }
    }

    fun onSelectMovie(id : Long){
        viewModelScope.launch {
            _effect.emit(FolderEffect.OnSelectedMovie(id))
        }
    }
}