package com.sidspace.stary.movie.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sidspace.stary.movie.presentation.screen.MovieScreen


fun NavGraphBuilder.movieNavGraph(
    navController: NavController,
    innerPadding: PaddingValues,
    onSelectMovie: (Long) -> Unit,
    onClickReview: (Long) -> Unit,
    onSelectPerson: (Long) -> Unit,
    toErrorScreen: () -> Unit
) {

    composable<Profile> { navBackStackEntry ->

        val profile: Profile = navBackStackEntry.toRoute()

        MovieScreen(
            id = profile.id,
            onSelectMovie = onSelectMovie,
            onClickReviews = onClickReview,
            onSelectPerson = onSelectPerson, toErrorScreen = toErrorScreen,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        )
    }

}