package com.sidspace.stary.review.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidspace.stary.domain.model.Result

import com.sidspace.stary.review.domain.usecase.GetReviewUseCase
import com.sidspace.stary.review.presentation.mapper.toReviewUi
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
class ReviewViewModel @Inject constructor(
    private val getReviewUseCase: GetReviewUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ReviewState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ReviewEffect>()
    val effect = _effect.asSharedFlow()

    fun onIntent(intent: ReviewIntent) {
        when (intent) {
            is ReviewIntent.LoadReviews -> {
                getReviewsById(intent.id)
            }

            ReviewIntent.OnBack -> {
                onBack()
            }

            ReviewIntent.OnError -> {
                toErrorScreen()
            }
        }
    }

    fun getReviewsById(id: Long) {
        viewModelScope.launch {
            getReviewUseCase(id).collect { result ->

                when (val data = result) {
                    is Result.Error -> {
                        _state.update { it.copy(reviews = ResultData.Error) }
                    }

                    Result.Loading -> {

                    }

                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                reviews = ResultData.Success(data.data.map { it.toReviewUi() }),
                                countReviews = data.data.size
                            )
                        }
                    }
                }

            }

        }
    }

    private fun toErrorScreen() {
        viewModelScope.launch {
            _effect.emit(ReviewEffect.ToErrorScreen)
        }
    }

    fun onBack() {
        viewModelScope.launch {
            _effect.emit(ReviewEffect.OnBack)
        }
    }
}