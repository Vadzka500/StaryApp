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
import com.sidspace.stary.account.presentation.navigation.Account
import com.sidspace.stary.home.presentation.navigation.Home
import com.sidspace.stary.random.presentation.navigation.Random
import com.sidspace.stary.search.presentation.navigation.Search

data class Routes<T : Any>(
    val name: String,
    val route: T,
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector
)

val listOfScreens = listOf(
    Routes("Home", Home, Icons.Default.Home, Icons.Outlined.Home),
    Routes("Search", Search, Icons.Default.Search, Icons.Outlined.Search),
    Routes("Random", Random, Icons.Default.Movie, Icons.Outlined.Movie),
    Routes("Account", Account, Icons.Default.Person, Icons.Outlined.Person)
)
