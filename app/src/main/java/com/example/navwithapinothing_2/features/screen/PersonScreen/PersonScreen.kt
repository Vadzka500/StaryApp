package com.example.navwithapinothing_2.features.screen.PersonScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.moviesapi.models.Response
import com.example.moviesapi.models.movie.MovieDTO
import com.example.moviesapi.models.movie.Person
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.features.screen.MovieScreen.shimmerEffect
import com.example.navwithapinothing_2.features.screen.MoviesListScreen.ListMovies
import com.example.navwithapinothing_2.features.screen.MovieViewModel
import com.example.navwithapinothing_2.features.theme.poppinsFort
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * @Author: Vadim
 * @Date: 02.02.2025
 */
@Composable
fun PersonScreen(
    modifier: Modifier = Modifier,
    id: Long,
    movieViewModel: MovieViewModel = hiltViewModel(),
    onSelectMovie: (Long) -> Unit
) {
    val state = movieViewModel.state_person.collectAsState()
    movieViewModel.getPersonById(id)

    val stateMovie = movieViewModel.state_movie_person.collectAsState()
    movieViewModel.getMovieByPerson(id)


    InitScreen(
        statePerson = state,
        stateMovie = stateMovie,
        onSelectMovie = onSelectMovie,
        modifier = modifier
    )
}

@Composable
fun InitScreen(
    modifier: Modifier = Modifier,
    statePerson: State<*>,
    stateMovie: State<*>,
    onSelectMovie: (Long) -> Unit
) {

    var visible by remember {
        mutableStateOf(statePerson.value !is Result.Success<*>)
    }

    var visibleMovies by remember {
        mutableStateOf(stateMovie.value !is Result.Success<*>)
    }

    val scroll = rememberScrollState()
    Column(modifier = modifier.verticalScroll(state = scroll)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {

            LoadingScreen(visible = visible)


            when (val data = statePerson.value) {

                is Result.Loading -> {

                }

                is Result.Error<*> -> {

                }

                is Result.Success<*> -> {
                    visible = false
                    InitPersonScreen(person = data.data as Person)
                }

            }

        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            AnimatedVisibility(
                visible = visible,
                exit = fadeOut(animationSpec = tween(200))
            ) {
                ShimmerMovies(visible = visibleMovies)
                ShimmerMovies(visible = visibleMovies)
            }



            when (val data = stateMovie.value) {

                is Result.Loading -> {

                }

                is Result.Error<*> -> {

                }

                is Result.Success<*> -> {
                    ShowMovies(
                        list = data.data as Response<MovieDTO>,
                        onSelectMovie = onSelectMovie
                    )
                    visibleMovies = false

                }

            }
        }

    }
}

@Composable
fun InitPersonScreen(modifier: Modifier = Modifier, person: Person) {

    var visible by remember {
        mutableStateOf(false)
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(200))
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp)
        ) {
            Row {

                AsyncImage(
                    model = person.photo, contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .width(160.dp)
                        .height(240.dp)
                        .clip(
                            RoundedCornerShape(8.dp)
                        )

                )



                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)) {
                    Text(
                        text = person.name!!,
                        modifier.fillMaxWidth(),
                        fontFamily = poppinsFort,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (!person.profession.isNullOrEmpty())
                        Text(
                            text = person.profession.joinToString(
                                ", ",
                                transform = { person -> person.value.toString() }),
                            modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            fontFamily = poppinsFort,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp
                        )

                    if (person.birthday != null) {

                        val isoFormat = DateTimeFormatter.ISO_DATE_TIME
                        val date = ZonedDateTime.parse(person.birthday, isoFormat).toLocalDate()

                        val firstApiFormat =
                            DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
                        val textDate = date.format(firstApiFormat)

                        Text(
                            text = textDate,
                            //modifier = Modifier.padding(top = 4.dp),
                            fontFamily = poppinsFort,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }




                    Row() {
                        if (person.age != null) {
                            Text(
                                text = person.age.let { if(it%10 == 1) "$it год" else if(it%10 in 2..4) "$it года" else "$it лет"},
                                fontFamily = poppinsFort,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp
                            )
                            if (person.growth != null) Text(
                                text = "•",
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )
                        }

                        if (person.growth != null)
                            Text(
                                text = person.growth.toString() + " см",
                                fontFamily = poppinsFort,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp
                            )
                    }


                }


            }
        }
    }

    LaunchedEffect(true) {
        visible = true
    }
}

@Composable

fun ShowMovies(
    modifier: Modifier = Modifier,
    list: Response<MovieDTO>,
    onSelectMovie: (Long) -> Unit
) {

    val movieList =
        list.docs.filter { !it.isSeries!! }.sortedByDescending { movie -> movie.votes?.kp }
    val seriesList =
        list.docs.filter { it.isSeries!! }.sortedByDescending { movie -> movie.votes?.kp }

    if (movieList.isNotEmpty()) MovieRow(
        list = movieList,
        text = "Фильмы  ${movieList.size}",
        modifier = Modifier.padding(top = 16.dp),
        onSelectMovie = onSelectMovie
    )
    if (seriesList.isNotEmpty()) MovieRow(
        list = seriesList,
        text = "Сериалы  ${seriesList.size}",
        modifier = Modifier.padding(top = 16.dp),
        onSelectMovie = onSelectMovie
    )


}

@Composable
fun MovieRow(
    modifier: Modifier = Modifier,
    list: List<MovieDTO>,
    text: String,
    onSelectMovie: (Long) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = text,
            fontFamily = poppinsFort,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
        ListMovies(
            list = list,
            onSelectMovie = onSelectMovie,
            modifier = Modifier.padding(top = 16.dp)
        )
    }


}

@Composable
fun ShimmerMovies(modifier: Modifier = Modifier, visible: Boolean) {

        Column(modifier = Modifier.padding(top = 16.dp).padding(top = 16.dp)) {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .width(150.dp)
                    .height(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
            LazyRow(modifier = Modifier.padding(top = 16.dp)) {
                items(5) { index ->
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .width(150.dp)
                            .height(240.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .shimmerEffect()
                    )
                }
            }

            /*Box(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp)
                    .width(150.dp)
                    .height(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )

            LazyRow(modifier = Modifier.padding(top = 16.dp)) {
                items(5) {
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .width(150.dp)
                            .height(240.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .shimmerEffect()
                    )
                }
            }*/

    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier, visible: Boolean) {

    AnimatedVisibility(
        visible = visible,
        exit = fadeOut(animationSpec = tween(200))
    ) {
        Column(
            modifier = Modifier
                .padding(top = 30.dp)
        ) {

            Row {
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .width(160.dp)
                        .height(240.dp)
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                        .shimmerEffect()
                )

                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                    Box(
                        modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .clip(
                                RoundedCornerShape(6.dp)
                            )
                            .shimmerEffect()
                    )

                    Box(
                        modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .height(40.dp)
                            .clip(
                                RoundedCornerShape(6.dp)
                            )
                            .shimmerEffect()
                    )

                    Box(
                        modifier
                            .fillMaxWidth(0.6f)
                            .padding(top = 16.dp)
                            .height(20.dp)
                            .clip(
                                RoundedCornerShape(6.dp)
                            )
                            .shimmerEffect()
                    )

                    Box(
                        modifier
                            .fillMaxWidth(0.9f)
                            .padding(top = 16.dp)
                            .height(20.dp)
                            .clip(
                                RoundedCornerShape(6.dp)
                            )
                            .shimmerEffect()
                    )
                }


            }
        }
    }
}