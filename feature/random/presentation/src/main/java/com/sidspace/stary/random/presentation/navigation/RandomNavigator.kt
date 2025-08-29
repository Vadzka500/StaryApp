package com.sidspace.stary.random.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sidspace.stary.random.presentation.screen.SliderScreen


fun NavGraphBuilder.randomNavGraph(
    paddingValues: PaddingValues,
    onSelectMovie: (Long) -> Unit
) {

    composable<Random> {
        SliderScreen(
            onSelectMovie = onSelectMovie,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }

}
