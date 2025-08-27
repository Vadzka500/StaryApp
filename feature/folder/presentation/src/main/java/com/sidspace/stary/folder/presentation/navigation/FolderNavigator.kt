package com.sidspace.stary.folder.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sidspace.stary.folder.presentation.screen.UserCollectionScreen

fun NavGraphBuilder.folderNavGraph(
    paddingValues: PaddingValues,
    onSelectMovie: (Long) -> Unit,
    toErrorScreen: () -> Unit,
    onBack: () -> Unit
) {

    composable<Folder> {

        val folder: Folder = it.toRoute()

        UserCollectionScreen(
            folderId = folder.id,
            onSelectMovie = onSelectMovie,
            toErrorScreen = toErrorScreen,
            onBack = onBack,
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        )
    }

}