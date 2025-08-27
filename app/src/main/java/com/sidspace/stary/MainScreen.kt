package com.sidspace.stary.features.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.sidspace.stary.navigation.AppNavHost
import com.sidspace.stary.navigation.NavigationBottomBar

@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
            NavigationBottomBar(navController = navController)
    }) { innerPadding ->
        AppNavHost(navController = navController, innerPaddingValues = innerPadding)
    }
}






