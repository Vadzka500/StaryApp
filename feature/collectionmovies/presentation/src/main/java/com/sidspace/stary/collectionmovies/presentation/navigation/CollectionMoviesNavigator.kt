package com.sidspace.stary.collectionmovies.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sidspace.stary.collectionmovies.presentation.screen.AllMoviesScreen


fun NavGraphBuilder.collectionMoviesNavGraph(
    paddingValues: PaddingValues,
    onSelectMovie: (Long) -> Unit,
    toErrorScreen: () -> Unit,
    onBack: () -> Unit
) {

    composable<CollectionMovies>(
        enterTransition = { scaleIn(initialScale = 0.85f, animationSpec = tween(400)) },
        exitTransition = { fadeOut(animationSpec = tween(100)) },
        popEnterTransition = { scaleIn(initialScale = 1.05f, animationSpec = tween(200)) },
        popExitTransition = {
            fadeOut(animationSpec = tween(100))
        }) {

        val collectionMovies: CollectionMovies = it.toRoute()

        AllMoviesScreen(
            label = collectionMovies.label,
            slug = collectionMovies.slug,
            onSelectMovie = onSelectMovie,
            toErrorScreen = toErrorScreen,
            onBack = onBack,
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        )
    }

}