package com.sidspace.stary.folder.presentation.screen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidspace.stary.domain.model.Folder
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.folder.domain.usecase.GetFolderFromApiUseCase
import com.sidspace.stary.folder.domain.usecase.GetFolderFromDbUseCase
import com.sidspace.stary.folder.domain.usecase.RemoveFolderUseCase
import com.sidspace.stary.ui.enum.ViewMode
import com.sidspace.stary.ui.enum.toggle
import com.sidspace.stary.ui.mapper.toMovieData
import com.sidspace.stary.ui.model.ResultData
import com.sidspace.stary.ui.sort.sortListMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FolderViewModel @Inject constructor(
    private val getFolderUseCase: GetFolderFromApiUseCase,
    private val getFolderFromDbUseCase: GetFolderFromDbUseCase,
    private val removeFolderUseCase: RemoveFolderUseCase
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

            FolderIntent.OnError -> {
                toErrorScreen()
            }
        }
    }

    fun removeFolder() {
        viewModelScope.launch {
            when (removeFolderUseCase(_state.value.folder!!.id)) {
                Result.Error -> {

                }

                Result.Loading -> {

                }

                is Result.Success<*> -> {
                    onBack()
                }
            }
        }
    }

    fun getFolder(id: Long) {
        viewModelScope.launch {

            getFolderFromDbUseCase(id).collect {

                when (val data = it) {
                    Result.Error -> {
                        data
                    }

                    Result.Loading -> {
                        data
                    }

                    is Result.Success -> {

                        _state.update { it.copy(folder = data.data) }
                        val count = data.data.listOfMovies!!.size
                        _state.update { it.copy(countMovies = count) }

                        if (count > 0) {
                            getFolderUseCase(data.data).collect {
                                when (val data = it) {
                                    Result.Error -> {
                                        _state.update { it.copy(list = ResultData.Error) }
                                    }

                                    Result.Loading -> {
                                        data
                                    }

                                    is Result.Success<Folder> -> {
                                        _state.update { it.copy(folder = data.data) }
                                        val count = data.data.listOfMovies!!.size
                                        _state.update { it.copy(countMovies = count) }
                                        if (count > 0) {
                                            _state.update {
                                                it.copy(
                                                    list = ResultData.Success(
                                                        data.data.listOfMovies!!.map { it.toMovieData() })
                                                )
                                            }
                                        } else {
                                            _state.update {
                                                it.copy(
                                                    list = ResultData.Success(
                                                        emptyList()
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            _state.update { it.copy(list = ResultData.Success(emptyList())) }
                        }
                    }
                }
            }


        }
    }


    fun sortMovies() {

        _state.update {
            it.copy(
                list = ResultData.Success(
                    sortListMovies(
                        list = (_state.value.list as ResultData.Success).data,
                        sortDirection = _state.value.sortDirection,
                        sortType = _state.value.sortType
                    )
                )
            )
        }

    }

    fun toErrorScreen() {
        viewModelScope.launch {
            _effect.emit(FolderEffect.ToErrorScreen)
        }
    }

    fun onBack() {
        viewModelScope.launch {
            _effect.emit(FolderEffect.OnBack)
        }
    }

    fun onSelectMovie(id: Long) {
        viewModelScope.launch {
            _effect.emit(FolderEffect.OnSelectedMovie(id))
        }
    }
}
