package com.sidspace.stary.review.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sidspace.stary.review.presentation.screen.ReviewScreen

fun NavGraphBuilder.reviewNavGraph(
    paddingValues: PaddingValues,
    toErrorScreen: () -> Unit,
    onBack: () -> Unit
) {

    composable<Review> {
        val review: Review = it.toRoute()
        ReviewScreen(id = review.idMovie, toErrorScreen = toErrorScreen, onBack = onBack,modifier = Modifier.fillMaxSize().padding(paddingValues))
    }

}