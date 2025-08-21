package com.sidspace.stary.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sidspace.stary.navigation.model.Error

import com.sidspace.stary.folders.presentation.Folders

import com.sidspace.stary.review.presentation.Review
import com.sidspace.stary.viewed.presentation.Viewed


import com.sidspace.stary.account.presentation.accountNavGraph
import com.sidspace.stary.bookmark.presentation.bookmarkNavGraph
import com.sidspace.stary.collectionmovies.presentation.collectionMoviesNavGraph
import com.sidspace.stary.collections.presentation.collectionsNavGraph
import com.sidspace.stary.folder.presentation.folderNavGraph
import com.sidspace.stary.folders.presentation.foldersNavGraph

import com.sidspace.stary.person.presentation.personNavGraph


import com.sidspace.stary.movie.presentation.movieNavGraph


import com.example.presentation.searchNavGraph

import com.sidspace.stary.bookmark.presentation.Bookmark
import com.sidspace.stary.collectionmovies.presentation.CollectionMovies
import com.sidspace.stary.collections.presentation.Collections
import com.sidspace.stary.folder.presentation.Folder
import com.sidspace.stary.home.presentation.Home
import com.sidspace.stary.home.presentation.homeNavGraph
import com.sidspace.stary.movie.presentation.Profile
import com.sidspace.stary.person.presentation.Person
import com.sidspace.stary.random.presentation.randomNavGraph
import com.sidspace.stary.review.presentation.reviewNavGraph
import com.sidspace.stary.viewed.presentation.viewedNavGraph


fun getNavigationMode(context: Context): Int {

    val mode = Settings.Secure.getInt(
        context.contentResolver, "navigation_mode"
    )

    /*   0 -> "Three-button navigation"
    1 -> "Two-button navigation"
    2 -> "Gesture navigation"*/

    return mode
}


@SuppressLint("RestrictedApi")
@Composable
fun NavigationBottomBar(modifier: Modifier = Modifier, navController: NavController) {

    var indexSelected by remember {
        mutableIntStateOf(0)
    }

    val navigationMode = getNavigationMode(LocalContext.current)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination


    if (!currentDestination?.route.equals(Error::class.qualifiedName)) {

        NavigationBar(
            modifier = Modifier.then(
                if (navigationMode == 0) Modifier.height(100.dp)
                else Modifier.height(75.dp)
            )
        ) {


            navController.addOnDestinationChangedListener({ _, navDestination, _ ->
                indexSelected =
                    listOfScreens.indexOfFirst { it.route::class.qualifiedName == navDestination.route }
                        .let { if (it == -1) indexSelected else it }
            })

            listOfScreens.forEachIndexed { index, item ->
                NavigationBarItem(
                    //label = { Text(text = item.name) },
                    onClick = {

                        if (indexSelected == index) {
                            navController.popBackStack(
                                item.route, inclusive = false, saveState = false
                            )
                        } else {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {

                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        }

                        indexSelected = index
                    },
                    selected = (currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true || indexSelected == index),
                    icon = {
                        Icon(
                            imageVector = if (index == indexSelected) item.iconSelected else item.iconUnselected,
                            contentDescription = null
                        )
                    })
            }
        }
    }
}


@Composable
fun AppNavHost(
    navController: NavHostController, innerPaddingValues: PaddingValues
) {


    NavHost(
        navController = navController, startDestination = Home,
        enterTransition = { scaleIn(initialScale = 0.95f, animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(100)) },
        popEnterTransition = { scaleIn(initialScale = 1.05f, animationSpec = tween(200)) },
        popExitTransition = { fadeOut(animationSpec = tween(100)) }
    ) {


        randomNavGraph(innerPaddingValues, onSelectMovie = { id ->
            navController.navigate(
                Profile(id)
            )
        })

        searchNavGraph(innerPaddingValues, onSelectMovie = { id ->
            navController.navigate(Profile(id))
        }, toErrorScreen = {
            navController.navigate(Error)
        })

        homeNavGraph(paddingValues = innerPaddingValues, onMovieSelect = { id ->
            navController.navigate(
                Profile(id)
            )
        }, toErrorScreen = {
            navController.navigate(Error)
        }, toCollectionScreen = {
            navController.navigate(Collections)
        }, onSelectListMovies = { label, slug ->
            navController.navigate(CollectionMovies(label, slug))
        })

        movieNavGraph(
            navController, innerPaddingValues, onSelectMovie = { id ->
                navController.navigate(Profile(id))
            },
            toErrorScreen = {
                navController.navigate(Error)
            },
            onSelectPerson = { id ->
                navController.navigate(Person(id))
            }, onClickReview = { id ->
                navController.navigate(Review(id))
            })

        accountNavGraph(paddingValues = innerPaddingValues, onSelectMovie = { id ->
            navController.navigate(Profile(id))
        }, toErrorScreen = {
            navController.navigate(Error)
        }, onClickFolders = {
            navController.navigate(Folders)
        }, toViewedScreen = {
            navController.navigate(Viewed)
        }, toBookmarkScreen = {
            navController.navigate(Bookmark)
        })


        collectionMoviesNavGraph(navController, innerPaddingValues, onSelectMovie = { id ->
            navController.navigate(Profile(id))
        }, toErrorScreen = {
            navController.navigate(Error)
        }, onBack = {
            navController.popBackStack()
        })

        bookmarkNavGraph(
            navController = navController,
            paddingValues = innerPaddingValues,
            onSelectMovie = { id ->
                navController.navigate(Profile(id))
            },
            toErrorScreen = {
                navController.navigate(Error)
            },
            onBack = {
                navController.popBackStack()
            })

        collectionsNavGraph(
            navController,
            innerPaddingValues,
            onSelectCollection = { label, slug ->
                navController.navigate(CollectionMovies(label, slug))
            },
            toErrorScreen = {
                navController.navigate(Error)
            },
            onBack = {
                navController.popBackStack()
            })

        folderNavGraph(navController, innerPaddingValues, onSelectMovie = { id ->
            navController.navigate(Profile(id))
        }, toErrorScreen = {
            navController.navigate(Error)
        }, onBack = {
            navController.popBackStack()
        })

        foldersNavGraph(navController, innerPaddingValues, onSelectFolder = {id ->
            navController.navigate(Folder(id))
        }, toErrorScreen = {
            navController.navigate(Error)
        }, onBack = {
            navController.popBackStack()
        })

        personNavGraph(navController, innerPaddingValues, onSelectMovie = { id ->
            navController.navigate(Profile(id))
        }, toErrorScreen = {
            navController.navigate(Error)
        })

        reviewNavGraph(navController, innerPaddingValues, toErrorScreen = {
            navController.navigate(Error)
        }, onBack = {
            navController.popBackStack()
        })

        viewedNavGraph(navController, innerPaddingValues, onSelectMovie = { id ->
            navController.navigate(Profile(id))
        }, toErrorScreen = {
            navController.navigate(Error)
        }, onBack = {
            navController.popBackStack()
        })

    }
}



