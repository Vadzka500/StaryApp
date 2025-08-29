package com.sidspace.stary.folders.presentation.screen

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidspace.stary.folders.domain.usecase.AddFolderUseCase
import com.sidspace.stary.domain.model.Folder
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.folders.domain.usecase.GetAllFoldersUseCase
import com.sidspace.stary.ui.model.ResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FoldersViewModel @Inject constructor(
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    private val addFolderUseCase: AddFolderUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FoldersState())

    val state: StateFlow<FoldersState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<FoldersEffect>()

    val effect = _effect.asSharedFlow()

    init {
        getFolders()
    }

    fun onIntent(intent: FoldersIntent) {
        when (val data = intent) {
            FoldersIntent.ClickCreateFolder -> {
                _state.update { it.copy(isShowBottomSheet = true) }
            }

            is FoldersIntent.ToFolderScreen -> {
                toFolderScreen((data.id))
            }

            FoldersIntent.OnBack -> {
                onBackScreen()
            }

            FoldersIntent.HideBottomSheet -> {
                _state.update { it.copy(isShowBottomSheet = false) }
            }

            is FoldersIntent.UpdateNameFolder -> {
                _state.update { it.copy(textFieldFolderValue = data.name) }
            }

            is FoldersIntent.UpdateColor -> {
                _state.update { it.copy(selectColor = data.colorIndex) }
            }

            is FoldersIntent.UpdateImage -> {
                _state.update {
                    it.copy(
                        selectImage = data.imageIndex,
                        selectImageName = data.selectImageName
                    )
                }
            }

            FoldersIntent.AddFolder -> {
                if (_state.value.textFieldFolderValue.isEmpty()) {
                    emptyNameError()
                    _state.update { it.copy(isErrorEmptyName = true) }
                } else {
                    _state.update { it.copy(isErrorEmptyName = false, isShowBottomSheet = false) }
                    addFolder()
                }
            }

            FoldersIntent.OnError -> {
                toErrorScreen()
            }
        }
    }

    fun addFolder() {
        viewModelScope.launch {
            delay(FoldersState.ADD_FOLDER_DELAY)
            val name = _state.value.textFieldFolderValue
            val color = _state.value.listOfColors[_state.value.selectColor].toArgb()
            val image = _state.value.selectImageName


            addFolderUseCase(
                Folder(
                    name = name,
                    color = color,
                    imageResName = image
                )
            )
            _state.update { it.copy(textFieldFolderValue = "") }
        }
    }

    fun getFolders() {
        viewModelScope.launch {
            getAllFoldersUseCase().collect { result ->

                if (result is Result.Success) {
                    _state.update { it.copy(filters = ResultData.Success(result.data)) }
                }
            }
        }
    }

    fun toErrorScreen() {
        viewModelScope.launch {
            _effect.emit(FoldersEffect.ToErrorScreen)
        }
    }

    fun toFolderScreen(id: Long) {
        viewModelScope.launch {
            _effect.emit(FoldersEffect.ToFolderScreen(id))
        }
    }

    fun onBackScreen() {
        viewModelScope.launch {
            _effect.emit(FoldersEffect.OnBack)
        }
    }

    fun emptyNameError() {
        viewModelScope.launch {
            _effect.emit(FoldersEffect.ErrorToast("Название не может быть пустым"))
        }
    }
}
