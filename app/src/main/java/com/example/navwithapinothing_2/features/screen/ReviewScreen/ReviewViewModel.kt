package com.example.navwithapinothing_2.features.screen.ReviewScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navwithapinothing_2.data.MovieRepository
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.models.UserReview
import com.example.navwithapinothing_2.usecase.GetReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.List

/**
 * @Author: Vadim
 * @Date: 03.07.2025
 */
@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
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
        }
    }

    fun getReviewsById(id: Long) {
        viewModelScope.launch {
            getReviewUseCase(id).collect { result ->

                when (val data = result) {
                    is Result.Error<*> -> {
                        println("error review" + data.data)
                    }
                    Result.Loading -> {

                    }
                    is Result.Success<*> -> {
                        _state.update {
                            it.copy(
                                reviews = ReviewResult.Success((data.data as List<UserReview>)),
                                countReviews = data.data.size
                            )
                        }
                    }
                }

            }

        }
    }

    fun onBack(){
        viewModelScope.launch {
            _effect.emit(ReviewEffect.OnBack)
        }
    }
}