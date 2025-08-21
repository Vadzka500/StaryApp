package com.sidspace.stary.review.presentation.screen


sealed class ReviewEffect {
    object OnBack: ReviewEffect()
    object ToErrorScreen: ReviewEffect()
}