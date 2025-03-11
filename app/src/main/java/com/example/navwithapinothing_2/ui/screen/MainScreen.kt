package com.example.navwithapinothing_2.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.navwithapinothing_2.navigation.Account
import com.example.navwithapinothing_2.navigation.Anim
import com.example.navwithapinothing_2.navigation.Home
import com.example.navwithapinothing_2.navigation.List
import com.example.navwithapinothing_2.navigation.Person

import com.example.navwithapinothing_2.navigation.Profile
import com.example.navwithapinothing_2.navigation.Random
import com.example.navwithapinothing_2.navigation.Search
import com.example.navwithapinothing_2.navigation.Slider


import com.example.navwithapinothing_2.navigation.listOfScreens
import com.example.navwithapinothing_2.ui.screen.MovieScreen.MovieScreen

import com.example.navwithapinothing_2.ui.screen.MoviesListScreen.ListScreen
import com.example.navwithapinothing_2.ui.screen.MoviesListScreen.MovieViewModel
import com.example.navwithapinothing_2.ui.screen.PersonScreen.PersonScreen
import com.example.navwithapinothing_2.ui.screen.slider.SliderScreen

@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    val movieViewModel = viewModel<MovieViewModel>()

    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        NavigationBottomBar(navController = navController)
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Home,
            enterTransition = { scaleIn(initialScale = 0.85f, animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(100)) },
            popEnterTransition = { scaleIn(initialScale = 1.05f, animationSpec = tween(200)) },
            popExitTransition = { fadeOut(animationSpec = tween(100)) }
            /* exitTransition = { slideOutHorizontally { -it } },
             enterTransition = { slideInHorizontally { it } },
             popEnterTransition = { slideInHorizontally { -it } },
             popExitTransition = { slideOutHorizontally { it } }*/) {


           /* composable<Home>(popEnterTransition = {
                scaleIn(
                    initialScale = 0.95f,
                    animationSpec = tween(200)
                )
            }) {
                HomeScreen(paddings = innerPadding)
            }*/

            composable<Home>(popEnterTransition = {
                scaleIn(
                    initialScale = 1.20f,
                    animationSpec = tween(500)
                )
            },   popExitTransition = { scaleOut(targetScale = 1.25f, animationSpec = tween(500)) },) {
                println("click list")
                ListScreen(
                    modifier = Modifier.padding(paddingValues = innerPadding),
                    movieViewModel = movieViewModel,
                    toRandomScreen = {
                        navController.navigate(Slider) {
                            /*  popUpTo(navController.graph.findStartDestination().id) {
                                  saveState = true
                              }*/

                            //  launchSingleTop = true
                            //  restoreState = true

                        }
                    },
                    onSelectMovie = { id ->
                        navController.navigate(Profile(id)) {

                        }
                    })
            }

            composable<Anim> {
                AnimScreen()
            }

            composable<Random>() {
                println("click slider")
                SliderScreen(onSelectMovie = { id ->
                    navController.navigate(Profile(id)) {

                    }
                })
            }

            composable<Profile> { navBackStackEntry ->
                println("click profile")
                val profile: Profile = navBackStackEntry.toRoute()
                println("id = " + profile.id)
                MovieScreen(
                    id = profile.id,
                    modifier = Modifier.padding(paddingValues = innerPadding),
                    onSelectMovie = { id ->
                        navController.navigate(Profile(id)) {

                        }
                    },
                    onSelectPerson = { id ->
                        navController.navigate(Person(id)){

                        }
                    })
            }

            composable<Person> { navBackStackEntry ->
                val person : Person = navBackStackEntry.toRoute()
                PersonScreen(id = person.id, modifier = Modifier.padding(paddingValues = innerPadding), onSelectMovie = { id ->
                    navController.navigate(Profile(id)) {

                    }
                })
            }

            composable<Account> { navBackStackEntry ->

            }

            composable<Search> { navBackStackEntry ->

            }
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun NavigationBottomBar(modifier: Modifier = Modifier, navController: NavController) {

    var indexelected by remember {
        mutableStateOf(0)
    }



    NavigationBar(modifier = Modifier.height(60.dp)) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        navController.addOnDestinationChangedListener({ _, navDestination, _ ->
            indexelected =
                listOfScreens.indexOfFirst { it.route::class.qualifiedName == navDestination.route }
                    .let { if (it == -1) indexelected else it }
        })

        listOfScreens.forEachIndexed { index, item ->
            NavigationBarItem(
                //label = { Text(text = item.name) },
                onClick = {

                    if (indexelected == index) {
                        navController.popBackStack(item.route, inclusive = false, saveState = false)
                    } else {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {

                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    }

                    indexelected = index
                },
                selected = (currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true || indexelected == index),
                icon = {
                    Icon(
                        imageVector = if (index == indexelected) item.iconSelected else item.iconUnselected,
                        contentDescription = null
                    )
                })
        }
    }
}
