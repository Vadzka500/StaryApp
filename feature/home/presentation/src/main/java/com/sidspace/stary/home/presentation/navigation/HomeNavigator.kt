package com.sidspace.stary.home.presentation.navigation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sidspace.stary.home.presentation.screen.ListScreen


fun NavGraphBuilder.homeNavGraph(
    paddingValues: PaddingValues,
    onMovieSelect: (Long) -> Unit,
    toErrorScreen: () -> Unit,
    onSelectListMovies: (String, String) -> Unit,
    toCollectionScreen: () -> Unit
) {

    composable<Home> {
        ListScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
                .background(MaterialTheme.colorScheme.background),
            toErrorScreen = toErrorScreen,
            onSelectMovie = onMovieSelect,
            onSelectListMovies = onSelectListMovies,
            toCollectionScreen = toCollectionScreen
        )
    }

}
