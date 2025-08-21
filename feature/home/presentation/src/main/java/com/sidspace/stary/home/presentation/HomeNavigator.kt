package com.sidspace.stary.home.presentation

import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable


import com.sidspace.stary.home.presentation.screen.ListScreen
import kotlinx.serialization.Serializable




fun NavGraphBuilder.homeNavGraph(
    paddingValues: PaddingValues,
    onMovieSelect: (Long) -> Unit,
    toErrorScreen: () -> Unit,
    onSelectListMovies: (String, String) -> Unit,
    toCollectionScreen: () -> Unit
) {

    composable<Home>(popEnterTransition = {
        scaleIn(
            initialScale = 1.10f,
            animationSpec = tween(300)
        )
    }) {
        ListScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            toErrorScreen = toErrorScreen,
            onSelectMovie = onMovieSelect,
            onSelectListMovies = onSelectListMovies,
            toCollectionScreen = toCollectionScreen
        )
    }

}