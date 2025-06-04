package com.example.navwithapinothing_2.ui.screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.navwithapinothing_2.R
import com.example.navwithapinothing_2.navigation.Account
import com.example.navwithapinothing_2.navigation.Anim
import com.example.navwithapinothing_2.navigation.Collection
import com.example.navwithapinothing_2.navigation.ErrorM
import com.example.navwithapinothing_2.navigation.Home
import com.example.navwithapinothing_2.navigation.ListMovies
import com.example.navwithapinothing_2.navigation.Person

import com.example.navwithapinothing_2.navigation.Profile
import com.example.navwithapinothing_2.navigation.Random
import com.example.navwithapinothing_2.navigation.Search
import com.example.navwithapinothing_2.navigation.Slider
import com.example.navwithapinothing_2.navigation.UserCollection


import com.example.navwithapinothing_2.navigation.listOfScreens
import com.example.navwithapinothing_2.ui.screen.AccountScreen.AccountScreen
import com.example.navwithapinothing_2.ui.screen.CollectionsScreen.CollectionsScreen
import com.example.navwithapinothing_2.ui.screen.MovieScreen.MovieScreen

import com.example.navwithapinothing_2.ui.screen.MoviesListScreen.ListScreen
import com.example.navwithapinothing_2.ui.screen.PersonScreen.PersonScreen
import com.example.navwithapinothing_2.ui.screen.SearchScreen.SearchScreen
import com.example.navwithapinothing_2.ui.screen.UserCollection.UserCollectionScreen
import com.example.navwithapinothing_2.ui.screen.slider.SliderScreen
import com.example.navwithapinothing_2.ui.theme.poppinsFort

@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    val movieViewModel = viewModel<MovieViewModel>()

    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        if (movieViewModel.isNavBarVisible.value) {
            NavigationBottomBar(navController = navController)
        }
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Home,
            enterTransition = { scaleIn(initialScale = 0.95f, animationSpec = tween(300)) },
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

            composable<Home>(
               /* popEnterTransition = {
                    scaleIn(
                        initialScale = 1.20f,
                        animationSpec = tween(500)
                    )
                },
                popExitTransition = { scaleOut(targetScale = 1.25f, animationSpec = tween(500)) },*/
            ) {
                println("click list")
                ListScreen(
                    modifier = Modifier.padding(paddingValues = innerPadding),
                    toErrorScreen = {
                        navController.navigate(ErrorM)
                    },
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
                        println("click 1")
                        navController.navigate(Profile(id)) {

                        }
                    }, onSelectListMovies = { label, slug ->
                        navController.navigate(ListMovies(label, slug))
                    }, toCollectionScreen = {
                        navController.navigate(Collection)
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

            composable<Profile>(/*enterTransition = { EnterTransition.None }*/) { navBackStackEntry ->
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
                        navController.navigate(Person(id)) {

                        }
                    })
            }

            composable<ListMovies>(
                enterTransition = { scaleIn(initialScale = 0.85f, animationSpec = tween(400)) },
                exitTransition = { fadeOut(animationSpec = tween(100)) },
                popEnterTransition = { scaleIn(initialScale = 1.05f, animationSpec = tween(200)) },
                popExitTransition = { fadeOut(animationSpec = tween(100)) }
            ) { navBackStackEntry ->
                println("list")
                val list: ListMovies = navBackStackEntry.toRoute()
                println("label = " + list.label)
                AllMoviesScreen(
                    label = list.label,
                    slug = list.slug,
                    modifier = Modifier.padding(paddingValues = innerPadding),
                    onSelectMovie = { id ->
                        navController.navigate(Profile(id)) {

                        }
                    })
            }

            composable<Person> { navBackStackEntry ->
                val person: Person = navBackStackEntry.toRoute()
                PersonScreen(
                    id = person.id,
                    modifier = Modifier.padding(paddingValues = innerPadding),
                    onSelectMovie = { id ->
                        navController.navigate(Profile(id)) {

                        }
                    })
            }



            composable<Account> { navBackStackEntry ->
                AccountScreen(onClickUserCollection = {navController.navigate(UserCollection)})
            }

            composable<UserCollection> { navBackStackEntry ->
                UserCollectionScreen()
            }

            composable<Collection> { navBackStackEntry ->
                CollectionsScreen(modifier = Modifier.padding(paddingValues = innerPadding), onSelectCollection = { label, slug ->
                    navController.navigate(ListMovies(label, slug))
                })
            }

            composable<ErrorM> { navBackStackEntry ->
                InitErrorServerMessage(modifier = modifier, movieViewModel = movieViewModel)
            }

            composable<Search> { navBackStackEntry ->
                SearchScreen(
                    modifier = Modifier.padding(paddingValues = innerPadding),
                    onSelectMovie = { id ->
                        navController.navigate(Profile(id))
                    })
            }

        }
    }
}

@Composable
fun InitErrorServerMessage(modifier: Modifier, movieViewModel: MovieViewModel) {

    LaunchedEffect(Unit) {
        movieViewModel.isNavBarVisible.value = false
    }

    DisposableEffect(Unit) {
        onDispose {
            movieViewModel.isNavBarVisible.value = true
        }
    }

    BackHandler {

    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.error_ic),
                contentDescription = "Error",
                modifier = Modifier.padding(start = 15.dp)
            )
            Text(
                text = "У нас технические неполадки, вернитесь позже",
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp),
                textAlign = TextAlign.Center
            )
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


