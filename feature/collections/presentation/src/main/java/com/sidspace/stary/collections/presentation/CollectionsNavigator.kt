package com.sidspace.stary.collections.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sidspace.stary.collections.presentation.screen.CollectionsScreen


fun NavGraphBuilder.collectionsNavGraph(
    navController: NavController,
    paddingValues: PaddingValues,
    onSelectCollection: (String, String) -> Unit,
    toErrorScreen: () -> Unit,
    onBack: () -> Unit
) {

    composable<Collections> {
        CollectionsScreen(
            onSelectCollection = onSelectCollection,
            toErrorScreen = toErrorScreen,
            onBack = onBack,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }

}