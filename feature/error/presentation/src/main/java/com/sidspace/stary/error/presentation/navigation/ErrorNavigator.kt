package com.sidspace.stary.error.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sidspace.stary.error.presentation.screen.ErrorScreen


fun NavGraphBuilder.errorNavGraph(
) {
    composable<Error> {
        ErrorScreen()
    }
}
