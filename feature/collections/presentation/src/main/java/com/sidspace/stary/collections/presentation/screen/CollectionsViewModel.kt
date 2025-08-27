package com.sidspace.stary.collections.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.collections.domain.usecase.GetCollectionUseCase
import com.sidspace.stary.ui.mapper.toCollectionUi
import com.sidspace.stary.ui.model.ResultData

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CollectionsViewModel @Inject constructor(
    private val getCollectionUseCase: GetCollectionUseCase
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

            CollectionsIntent.OnError -> {
                toErrorScreen()
            }
        }
    }

    fun getCollections() {
        viewModelScope.launch {

            getCollectionUseCase().collect {
                when (val data = it) {

                    is Result.Loading -> {

                    }

                    is Result.Error -> {
                        _state.update {
                            it.copy(
                                collectionResult = ResultData.Error
                            )
                        }
                    }

                    is Result.Success -> {

                        _state.update {
                            it.copy(
                                countCollection = data.data.size,
                                collectionResult = ResultData.Success(data.data.map { it.toCollectionUi() })
                            )
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

    fun toErrorScreen() {
        viewModelScope.launch {
            _effect.emit(CollectionsEffect.ToErorrScreen)
        }
    }

    fun onBack() {
        viewModelScope.launch {
            _effect.emit(CollectionsEffect.OnBack)
        }
    }
}