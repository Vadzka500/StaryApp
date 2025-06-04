package com.example.navwithapinothing_2.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.rememberNavController
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.ui.screen.MoviesListScreen.MovieCardGrid
import com.example.navwithapinothing_2.ui.screen.MoviesListScreen.MovieCardGridShimmer
import com.example.navwithapinothing_2.ui.theme.poppinsFort

/**
 * @Author: Vadim
 * @Date: 23.04.2025
 */
@Composable
fun AllMoviesScreen(
    modifier: Modifier = Modifier,
    label: String,
    slug: String,
    onSelectMovie: (Long) -> Unit,
    movieViewModel: MovieViewModel = hiltViewModel(),
    lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {

    val state = movieViewModel.state_movie_collections_all.collectAsState()
    val navController = rememberNavController()
    LaunchedEffect(slug) {

        movieViewModel.getMoviesByCollection(slug, limit = 250)
    }


    /*BackHandler {
        movieViewModel.resetStateMovieByCollectionAll()
        navController.navigateUp()
    }*/




    Column(modifier = Modifier.padding(top = 5.dp)) {

        Box() {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = label, textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 18.sp
            )
        }

        when (val data = state.value) {

            Result.Loading -> {
                LoadList()
            }

            is Result.Success<*> -> {
                InitList(list = data.data as List<MovieDTO>, onClick = onSelectMovie)
            }

            is Result.Error<*> ->{

            }
        }

    }
}



@Composable
fun LoadList(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(16) {
            MovieCardGridShimmer()
        }
    }
}

@Composable
fun InitList(list: List<MovieDTO>, onClick: (Long) -> Unit) {
    LazyVerticalGrid(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        itemsIndexed(list) { index, item ->
            MovieCardGrid(
                item = item,
                index = index,
                onSelectMovie = { id -> onClick(id) },
                modifier = Modifier
            )
        }
    }
}