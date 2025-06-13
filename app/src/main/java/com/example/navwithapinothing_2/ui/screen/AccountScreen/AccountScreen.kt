package com.example.navwithapinothing_2.ui.screen.AccountScreen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.R
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.MovieDb
import com.example.navwithapinothing_2.ui.screen.MovieViewModel
import com.example.navwithapinothing_2.ui.screen.MoviesListScreen.InitRow
import com.example.navwithapinothing_2.ui.screen.MoviesListScreen.MovieCard
import com.example.navwithapinothing_2.ui.theme.poppinsFort

/**
 * @Author: Vadim
 * @Date: 24.04.2025
 */

@Preview(backgroundColor = android.graphics.Color.WHITE.toLong(), showBackground = true)
@Composable
fun AccountScreen(modifier: Modifier = Modifier, onClickUserCollection: () -> Unit, movieViewModel: MovieViewModel = hiltViewModel()) {

    val stateVisibleMovies = movieViewModel.state_movie_all_visible.collectAsState()
    val stateAllMoviesVisible = movieViewModel.state_movie_all_visible_movies.collectAsState()

    var movieCount by remember {
        mutableIntStateOf(0)
    }
    


    LaunchedEffect(Unit) {
        movieViewModel.getVisibleMovies()
    }



    when(val data = stateVisibleMovies.value){
        is ResultDb.Loading -> {

        }

        ResultDb.Error -> {

        }
        is ResultDb.Success<*> -> {
            movieCount = (data.data as List<MovieDb>).size

            LaunchedEffect(Unit) {
                movieViewModel.getVisibleMoviesApi(data.data.map { it.idMovie })
            }
        }
    }

    Column(modifier = modifier) {

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()

                .padding(horizontal = 16.dp)
                .padding(vertical = 24.dp),
            onClick = {},
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.google_ic),
                    contentDescription = null,
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp)
                )

                Text(
                    text = "Войдите в аккаунт",
                    modifier = Modifier.padding(start = 32.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFort,
                    fontSize = 14.sp,
                )
            }
        }


        Spacer(
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(1.dp)
                .background(color = Color.Gray)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(horizontal = 16.dp)
                .padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(
                    R.drawable.ic_visibility_fill
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(25.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Text(
                text = "Просмотрено",
                modifier = Modifier.padding(start = 16.dp),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = movieCount.toString(), fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 14.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(horizontal = 16.dp)
                .padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(
                    R.drawable.ic_bookmark_fill
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(25.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Text(
                text = "Закладки",
                modifier = Modifier.padding(start = 16.dp),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "23", fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 14.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClickUserCollection() }
                .padding(horizontal = 16.dp)
                .padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(
                    R.drawable.ic_folder
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(25.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Text(
                text = "Мои коллекции",
                modifier = Modifier.padding(start = 16.dp),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.weight(1f))


        }

        /*Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector =
                    Icons.Default.Settings,
                contentDescription = null,
                modifier = Modifier
                    .size(25.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Text(
                text = "Настройки",
                modifier = Modifier.padding(start = 16.dp),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.weight(1f))


        }*/

        when(val data = stateAllMoviesVisible.value){
            is Result.Error<*> -> {

            }
            Result.Loading -> {

            }
            is Result.Success<*> -> {


                InitRow(
                    label = "В закладках",
                    onClick = {
                        /* onSelectListMovies(
                             (result.key as Pair<*, *>).first.toString(),
                             (result.key as Pair<*, *>).second.toString()
                         )*/
                    })

                LazyRow(
                    modifier = Modifier.padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {

                    items((data.data as List<MovieDTO>).size) { index ->

                        MovieCard(
                            item = data.data[index]!!,
                            index = index,
                            onSelectMovie = {}
                        )

                    }
                }

            }
        }



    }


}

