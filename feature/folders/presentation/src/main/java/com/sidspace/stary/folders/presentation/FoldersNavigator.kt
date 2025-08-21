package com.sidspace.stary.folders.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sidspace.stary.folders.presentation.screen.UserCollectionsScreen


fun NavGraphBuilder.foldersNavGraph(
    navController: NavController,
    paddingValues: PaddingValues,
    onSelectFolder: (Long) -> Unit,
    toErrorScreen: () -> Unit,
    onBack: () -> Unit
) {

    composable<Folders> {
        UserCollectionsScreen(
            onSelectFolder = onSelectFolder,
            toErrorScreen = toErrorScreen,
            onBack = onBack,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }

}