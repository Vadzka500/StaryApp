package com.sidspace.stary.features.screen

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.sidspace.stary.navigation.AppNavHost
import com.sidspace.stary.navigation.NavigationBottomBar


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






