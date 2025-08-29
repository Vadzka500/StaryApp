package com.sidspace.stary.account.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sidspace.stary.account.presentation.screen.AccountScreen


fun NavGraphBuilder.accountNavGraph(
    paddingValues: PaddingValues,
    onSelectMovie: (Long) -> Unit,
    onClickFolders: () -> Unit,
    toErrorScreen: () -> Unit,
    toViewedScreen: () -> Unit,
    toBookmarkScreen: () -> Unit
) {

    composable<Account>() {


        AccountScreen(
            onSelectMovie = onSelectMovie,
            onClickFolders = onClickFolders,
            toErrorScreen = toErrorScreen,
            toViewedScreen = toViewedScreen,
            toBookmarkScreen = toBookmarkScreen,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }

}
