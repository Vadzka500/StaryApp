package com.example.navwithapinothing_2.ui.screen

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.navwithapinothing_2.navigation.Anim
import com.example.navwithapinothing_2.navigation.Home
import com.example.navwithapinothing_2.navigation.List
import com.example.navwithapinothing_2.navigation.Slider
import com.example.navwithapinothing_2.navigation.listOfScreens
import com.example.navwithapinothing_2.ui.screen.MovieScreen.ListScreen
import com.example.navwithapinothing_2.ui.screen.MovieScreen.MovieViewModel

@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    val movieViewModel = viewModel<MovieViewModel>()

    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        NavigationBottomBar(navController = navController)
    }) { innerPadding ->
        NavHost(navController = navController, startDestination = Home, exitTransition = {ExitTransition.None}, enterTransition = {EnterTransition.None}) {
            composable<Home> {
                HomeScreen(paddings = innerPadding)
            }

            composable<List> {

                ListScreen(movieViewModel = movieViewModel)
            }

            composable<Anim> {

                AnimScreen()
            }

            composable<Slider> {

                SliderScreen()
            }
        }
    }
}

@Composable
fun NavigationBottomBar(modifier: Modifier = Modifier, navController: NavController) {

    var indexelected by remember {
        mutableStateOf(0)
    }

    NavigationBar {
        listOfScreens.forEachIndexed { index, item ->
            NavigationBarItem(
                label = { Text(text = item.name) },
                onClick = {
                    indexelected = index

                        navController.navigate(item.route)


                },
                selected = index == indexelected,
                icon = {
                    Icon(
                        imageVector = if (index == indexelected) item.iconSelected else item.iconUnselected,
                        contentDescription = null
                    )
                })
        }
    }
}
