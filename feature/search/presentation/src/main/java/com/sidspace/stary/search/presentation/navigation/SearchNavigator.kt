package com.sidspace.stary.search.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sidspace.stary.search.presentation.screen.SearchScreen


fun NavGraphBuilder.searchNavGraph(
    paddingValues: PaddingValues,
    onSelectMovie: (Long) -> Unit,
    toErrorScreen: () -> Unit
) {

    composable<Search> {
        SearchScreen(
            onSelectMovie = onSelectMovie,
            toErrorScreen = toErrorScreen,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }

}
