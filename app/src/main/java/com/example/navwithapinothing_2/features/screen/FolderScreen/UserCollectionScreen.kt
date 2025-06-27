package com.example.navwithapinothing_2.features.screen.FolderScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.Folder
import com.example.navwithapinothing_2.features.screen.MovieViewModel
import com.example.navwithapinothing_2.features.screen.MoviesListScreen.ListMovies
import com.example.navwithapinothing_2.features.theme.poppinsFort

/**
 * @Author: Vadim
 * @Date: 23.06.2025
 */

@Composable
fun UserCollectionScreen(
    modifier: Modifier = Modifier,
    folderId: Long,
    movieViewModel: MovieViewModel = hiltViewModel()
) {
    val state = movieViewModel.getFolderState.collectAsState()

    LaunchedEffect(Unit) {
        movieViewModel.getFolder(folderId)
    }

    Column(modifier = modifier) {


        if (state.value is ResultDb.Success<Folder>) {

            Row(
                modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Назад")

                Text(
                    (state.value as ResultDb.Success<Folder>).data.folderName,
                    modifier = Modifier.padding(start = 16.dp),
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFort,
                    fontSize = 24.sp,
                )

                Text(
                    "0",
                    modifier = Modifier.padding(start = 8.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFort,
                    fontSize = 24.sp,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize(), contentAlignment = Alignment.BottomStart
            ) {
                Image(
                    painter = painterResource(
                        LocalContext.current.resources.getIdentifier(
                            (state.value as ResultDb.Success<Folder>).data.imageResName,
                            "drawable",
                            LocalContext.current.packageName
                        )
                    ),
                    contentDescription = "",
                    modifier = Modifier
                        .size(380.dp)
                        .offset(y = 70.dp, x = (-40).dp)
                        .rotate(40f)
                        .alpha(0.3f)
                )
            }

        }
    }


}