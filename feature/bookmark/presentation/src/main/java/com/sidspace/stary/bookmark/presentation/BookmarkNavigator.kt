package com.sidspace.stary.bookmark.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sidspace.stary.bookmark.presentation.screen.BookmarkMoviesScreen


fun NavGraphBuilder.bookmarkNavGraph(
    navController: NavController,
    paddingValues: PaddingValues,
    onSelectMovie:(Long) -> Unit,
    toErrorScreen:() -> Unit,
    onBack:() -> Unit
){

    composable<Bookmark>{
        BookmarkMoviesScreen(
            onSelectMovie = onSelectMovie,
            toErrorScreen = toErrorScreen,
            onBack = onBack,
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        )
    }

}