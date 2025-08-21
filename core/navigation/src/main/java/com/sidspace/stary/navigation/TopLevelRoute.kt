package com.sidspace.stary.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector




import com.sidspace.stary.navigation.model.Error
import com.example.presentation.Home
import com.sidspace.stary.random.presentation.Random
import com.sidspace.stary.search.presentation.Search

data class TopLevelRoute<T : Any>(
    val name: String,
    val route: T,
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector
)

val listOfScreens = listOf(
    TopLevelRoute("Home", Home, Icons.Default.Home, Icons.Outlined.Home),
    TopLevelRoute("Search", Search, Icons.Default.Search, Icons.Outlined.Search),
    TopLevelRoute("Random", Random, Icons.Default.Movie, Icons.Outlined.Movie),
    TopLevelRoute("Account", com.example.presentation.Account, Icons.Default.Person, Icons.Outlined.Person)
)

val listOfScreensAll = listOfScreens +  listOf(
    TopLevelRoute("Error", Error, Icons.Default.Home, Icons.Outlined.Home),
)
