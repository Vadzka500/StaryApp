package com.example.navwithapinothing_2.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector

data class TopLevelRoute<T : Any>(
    val name: String,
    val route: T,
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector
)

val listOfScreens = listOf(
    TopLevelRoute("Home", Home, Icons.Default.Home, Icons.Outlined.Home),
    TopLevelRoute("List", List, Icons.AutoMirrored.Default.List, Icons.AutoMirrored.Outlined.List),
    TopLevelRoute("Anim", Anim, Icons.Default.Star, Icons.Outlined.Star),
    TopLevelRoute("Pager", Slider, Icons.Default.Favorite, Icons.Outlined.Favorite)
)
