package com.sidspace.stary.features.screen

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.sidspace.stary.navigation.AppNavHost
import com.sidspace.stary.navigation.NavigationBottomBar


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {




    val navController = rememberNavController()





    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
               println("Разрешение на уведомления получено")
            } else {
                println("Пользователь запретил уведомления")
            }
        }

        LaunchedEffect(Unit) {
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
            NavigationBottomBar(navController = navController)
    }) { innerPadding ->


        AppNavHost(navController = navController, innerPaddingValues = innerPadding)

        /*NavHost(
            navController = navController,
            startDestination = Home,
            enterTransition = { scaleIn(initialScale = 0.95f, animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(100)) },
            popEnterTransition = { scaleIn(initialScale = 1.05f, animationSpec = tween(200)) },
            popExitTransition = { fadeOut(animationSpec = tween(100)) }) {

            composable<Home>(
                popEnterTransition = {
                    scaleIn(
                        initialScale = 1.10f,
                        animationSpec = tween(300)
                    )
                },
            ) {

                ListScreen(
                    modifier = Modifier.fillMaxSize().padding(paddingValues = innerPadding),
                    toErrorScreen = {
                        toErrorScreen()
                    },
                    onSelectMovie = { id ->

                        navController.navigate(Profile(id)) {

                        }
                    }, onSelectListMovies = { label, slug ->
                        navController.navigate(ListMovies(label, slug))
                    }, toCollectionScreen = {
                        navController.navigate(Collection)
                    })
            }

            *//*composable<Home>(
                popEnterTransition = {
                    scaleIn(
                        initialScale = 1.10f,
                        animationSpec = tween(300)
                    )
                },
            ) {

                ListScreen(
                    modifier = Modifier.fillMaxSize().padding(paddingValues = innerPadding),
                    toErrorScreen = {
                        toErrorScreen()
                    },
                    onSelectMovie = { id ->

                        navController.navigate(Profile(id)) {

                        }
                    }, onSelectListMovies = { label, slug ->
                        navController.navigate(ListMovies(label, slug))
                    }, toCollectionScreen = {
                        navController.navigate(Collection)
                    })
            }


            composable<Random>() {
                println("click slider")
                SliderScreen(onSelectMovie = { id ->
                    if(id != -1L) {
                        navController.navigate(Profile(id))
                    }
                }, modifier = Modifier.fillMaxSize().padding(paddingValues = innerPadding))
            }


            composable<Profile> { navBackStackEntry ->
                println("click profile")
                val profile: Profile = navBackStackEntry.toRoute()
                println("id = " + profile.id)
                MovieScreen(
                    id = profile.id,
                    modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background ).padding(bottom = innerPadding.calculateBottomPadding()),
                    onSelectMovie = { id ->
                        navController.navigate(Profile(id)) {

                        }
                    },
                    onSelectPerson = { id ->
                        navController.navigate(Person(id)) {

                        }
                    }, onClickReviews = { id ->

                        navController.navigate(Review(id))
                    }, toErrorScreen = {
                        toErrorScreen()
                    })
            }

            composable<Review> {
                val review: Review = it.toRoute()
                ReviewScreen(
                    modifier = Modifier.fillMaxSize().padding(paddingValues = innerPadding),
                    id = review.idMovie,
                    onBack = {
                        navController.popBackStack()
                    }, toErrorScreen = {
                        toErrorScreen()
                    }
                )
            }

            composable<ListMovies>(
                enterTransition = { scaleIn(initialScale = 0.85f, animationSpec = tween(400)) },
                exitTransition = { fadeOut(animationSpec = tween(100)) },
                popEnterTransition = { scaleIn(initialScale = 1.05f, animationSpec = tween(200)) },
                popExitTransition = { fadeOut(animationSpec = tween(100)) }
            ) { navBackStackEntry ->

                val list: ListMovies = navBackStackEntry.toRoute()

                AllMoviesScreen(
                    label = list.label,
                    slug = list.slug,
                    modifier = Modifier.fillMaxSize().padding(paddingValues = innerPadding),
                    onSelectMovie = { id ->
                        navController.navigate(Profile(id)) {

                        }
                    },
                    onBack = { navController.popBackStack() }, toErrorScreen = {
                        toErrorScreen()
                    })
            }

            composable<Person> { navBackStackEntry ->
                val person: Person = navBackStackEntry.toRoute()
                PersonScreen(
                    id = person.id,
                    modifier = Modifier.fillMaxSize().padding(paddingValues = innerPadding),
                    onSelectMovie = { id ->
                        navController.navigate(Profile(id)) {

                        }
                    }, toErrorScreen = {
                        toErrorScreen()
                    })
            }

            composable<Account> { navBackStackEntry ->
                AccountScreen(
                    onClickFolders = { navController.navigate(UserCollections) },
                    onSelectMovie = { id ->
                        navController.navigate(Profile(id))
                    },
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    toViewedScreen = {
                        navController.navigate(Viewed)
                    },
                    toBookmarkScreen = {
                        navController.navigate(Bookmark)
                    }, toErrorScreen = {
                        toErrorScreen()
                    }
                )
            }

            composable<Viewed> { navBackStackEntry ->
                ViewedMoviesScreen(onBack = {
                    navController.popBackStack()
                }, onSelectMovie = { id ->
                    navController.navigate(Profile(id))
                }, toErrorScreen = {
                    toErrorScreen()
                }, modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = innerPadding))
            }

            composable<Bookmark> { navBackStackEntry ->
                BookmarkMoviesScreen(
                    onBack = {
                        navController.popBackStack()
                    }, toErrorScreen = {
                        toErrorScreen()
                    },
                    onSelectMovie = { id ->
                        navController.navigate(Profile(id))
                    }, modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = innerPadding)
                )
            }


            composable<UserCollections> { navBackStackEntry ->
                UserCollectionsScreen(
                    modifier = Modifier.fillMaxSize().padding(paddingValues = innerPadding),
                    onSelectFolder = { id ->
                        navController.navigate(UserCollection(id))
                    }, onBack = {
                        navController.popBackStack()
                    }, toErrorScreen = {
                        toErrorScreen()
                    })
            }

            composable<UserCollection> { navBackStackEntry ->
                val userCollection: UserCollection = navBackStackEntry.toRoute()
                UserCollectionScreen(
                    folderId = userCollection.id,
                    onBack = {
                        navController.popBackStack()
                    },
                    onSelectMovie = {
                        navController.navigate(Profile(it))
                    }, toErrorScreen = {
                        toErrorScreen()
                    },
                    modifier = Modifier.fillMaxSize().padding(innerPadding)
                )
            }

            composable<Collection> { navBackStackEntry ->
                CollectionsScreen(
                    modifier = Modifier.fillMaxSize().padding(paddingValues = innerPadding),
                    onSelectCollection = { label, slug ->
                        navController.navigate(ListMovies(label, slug))
                    }, onBack = {
                        navController.popBackStack()
                    }, toErrorScreen = {
                        toErrorScreen()
                    })
            }

            composable<ErrorM> { navBackStackEntry ->
                InitErrorServerMessage(modifier = modifier, movieViewModel = movieViewModel)
            }

            composable<Search> { navBackStackEntry ->
                SearchScreen(
                    modifier = Modifier.fillMaxSize().padding(paddingValues = innerPadding),
                    onSelectMovie = { id ->
                        navController.navigate(Profile(id))
                    }, toErrorScreen = {
                        toErrorScreen()
                    })
            }*//*

        }*/
    }


}


/*@Composable
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
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp),
                textAlign = TextAlign.Center
            )
        }
    }

}*/






