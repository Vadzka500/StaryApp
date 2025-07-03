package com.example.navwithapinothing_2.features.screen.FolderScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.Folder
import com.example.navwithapinothing_2.usecase.GetFolderUseCase
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
                    _state.update { it.copy(folder = data.data as Folder) }
                }
            }
        }
    }

}