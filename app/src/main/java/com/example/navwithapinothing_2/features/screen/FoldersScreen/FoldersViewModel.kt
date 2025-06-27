package com.example.navwithapinothing_2.features.screen.FoldersScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navwithapinothing_2.data.MovieRepository
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.features.screen.AccountScreen.AccountIntent
import com.example.navwithapinothing_2.usecase.GetAllFoldersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 27.06.2025
 */
@HiltViewModel
class FoldersViewModel @Inject constructor(
    private val getAllFoldersUseCase: GetAllFoldersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FoldersState())

    val state: StateFlow<FoldersState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<FoldersEffect>()

    val effect = _effect.asSharedFlow()

    init{
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
        }
    }

    fun getFolders() {
        viewModelScope.launch {
            getAllFoldersUseCase().collect { result ->

                if(result is ResultDb.Success){
                    _state.update { it.copy(filters = ResultFilterData.Success(result.data)) }
                }
            }
        }
    }

    fun toFolderScreen(id: Long){
        viewModelScope.launch {
            _effect.emit(FoldersEffect.ToFolderScreen(id))
        }
    }

    fun onBackScreen(){
        viewModelScope.launch {
            _effect.emit(FoldersEffect.OnBack)
        }
    }
}