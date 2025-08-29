package com.sidspace.stary.viewed.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sidspace.stary.viewed.presentation.screen.ViewedMoviesScreen

fun NavGraphBuilder.viewedNavGraph(
    paddingValues: PaddingValues,
    onSelectMovie: (Long) -> Unit,
    toErrorScreen: () -> Unit,
    onBack: () -> Unit
) {

    composable<Viewed> {
        ViewedMoviesScreen(
            onSelectMovie = onSelectMovie,
            toErrorScreen = toErrorScreen,
            onBack = onBack,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }

}
