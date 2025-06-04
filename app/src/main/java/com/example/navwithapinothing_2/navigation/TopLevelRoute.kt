package com.example.navwithapinothing_2.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SdCardAlert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SdCardAlert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import com.example.navwithapinothing_2.R

data class TopLevelRoute<T : Any>(
    val name: String,
    val route: T,
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector
)

val listOfScreens = listOf(
    TopLevelRoute("Home", Home, Icons.Default.Home, Icons.Outlined.Home),
    TopLevelRoute("Search", Search, Icons.Default.Search, Icons.Outlined.Search),
    TopLevelRoute("Random", Random, Icons.Default.SdCardAlert, Icons.Outlined.SdCardAlert),
    TopLevelRoute("Account", Account, Icons.Default.Person, Icons.Outlined.Person)
)

val listOfScreensAll = listOfScreens +  listOf(
    TopLevelRoute("Error", ErrorM, Icons.Default.Home, Icons.Outlined.Home),
)
