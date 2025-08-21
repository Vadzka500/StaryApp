package com.sidspace.stary.person.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sidspace.stary.person.presentation.screen.PersonScreen


fun NavGraphBuilder.personNavGraph(
    navController: NavController,
    paddingValues: PaddingValues,
    onSelectMovie:(Long) -> Unit,
    toErrorScreen:() -> Unit
) {

    composable<Person> {
        val person: Person = it.toRoute()

        PersonScreen(id = person.id, onSelectMovie = onSelectMovie, toErrorScreen = toErrorScreen,modifier = Modifier.fillMaxSize().padding(paddingValues))
    }

}