package com.sidspace.stary.collectionmovies.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
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
    composable<CollectionMovies> {

        val collectionMovies: CollectionMovies = it.toRoute()

        AllMoviesScreen(
            label = collectionMovies.label,
            slug = collectionMovies.slug,
            onSelectMovie = onSelectMovie,
            toErrorScreen = toErrorScreen,
            onBack = onBack,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}
