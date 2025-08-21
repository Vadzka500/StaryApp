package com.sidspace.stary.person.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.ListView
import com.example.ui.ShimmerMovies
import com.example.ui.model.MovieData
import com.example.ui.model.PersonUi
import com.example.ui.model.ResultData
import com.example.ui.shimmerEffect
import com.example.ui.uikit.poppinsFort

import kotlinx.coroutines.flow.collectLatest
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun PersonScreen(
    id: Long,
    onSelectMovie: (Long) -> Unit,
    toErrorScreen: () -> Unit,
    modifier: Modifier = Modifier,
    personViewModel: PersonViewModel = hiltViewModel(),
) {
    val state = personViewModel.state.collectAsState()

    LaunchedEffect(id) {
        personViewModel.initData(id)
    }

    LaunchedEffect(Unit) {
        personViewModel.effect.collectLatest {
            when (it) {
                is PersonEffect.OnSelectMovie -> {
                    onSelectMovie(it.id)
                }

                PersonEffect.ToErrorScreen -> {
                    toErrorScreen()
                }
            }
        }
    }


    InitScreen(
        state = state,
        modifier = modifier,
        onSelectMovie = {
            personViewModel.onIntent(PersonIntent.OnSelectMovie(it))
        }, onError = {
            personViewModel.onIntent(PersonIntent.OnError)
        }
    )
}

@Composable
fun InitScreen(
    state: State<PersonState>,
    onSelectMovie: (Long) -> Unit,
    onError: () -> Unit,
    modifier: Modifier = Modifier,
) {


    val scroll = rememberScrollState()
    Column(modifier = modifier.verticalScroll(state = scroll)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {

            AnimatedContent(
                targetState = state.value.person,
                transitionSpec = { fadeIn() togetherWith ExitTransition.None }) { person ->
                if (person != null) {
                    InitPersonScreen(person = person)
                } else {
                    LoadingScreen()
                }
            }

        }

        AnimatedContent(
            targetState = state.value.listOfMovie,
            transitionSpec = { fadeIn() togetherWith ExitTransition.None }) { result ->
            when (result) {
                is ResultData.Error -> {
                    onError()
                }

                ResultData.Loading -> {
                    ShimmerMovies()
                }

                is ResultData.Success -> {
                    ShowMovies(
                        title = "Фильмы",
                        list = result.data,
                        onSelectMovie = onSelectMovie
                    )
                }

                ResultData.None -> {

                }
            }
        }

        AnimatedContent(
            targetState = state.value.listOfSerials,
            transitionSpec = { fadeIn() togetherWith ExitTransition.None }) { result ->
            when (result) {
                is ResultData.Error -> {
                    onError()
                }

                ResultData.Loading -> {
                    ShimmerMovies()
                }

                is ResultData.Success -> {
                    ShowMovies(
                        title = "Сериалы",
                        list = result.data,
                        onSelectMovie = onSelectMovie
                    )
                }

                ResultData.None -> {

                }
            }

        }
    }
}

@Composable
fun InitPersonScreen(modifier: Modifier = Modifier, person: PersonUi) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp)
    ) {
        Row {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(person.photo)
                    .crossfade(true).build(), contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .width(160.dp)
                    .height(240.dp)
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .shimmerEffect()

            )



            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
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
                        text = person.profession?:"",
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
                            text = person.age?.let { if (it % 10 == 1) "$it год" else if (it % 10 in 2..4) "$it года" else "$it лет" } ?: "0",
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

@Composable

fun ShowMovies(
    title: String,
    list: List<MovieData>,
    modifier: Modifier = Modifier,
    onSelectMovie: (Long) -> Unit
) {


    if (list.isNotEmpty()) MovieRow(
        list = list,
        text = "$title ${list.size}",
        modifier = Modifier.padding(top = 16.dp),
        onSelectMovie = onSelectMovie
    )


}

@Composable
fun MovieRow(
    modifier: Modifier = Modifier,
    list: List<MovieData>,
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
        ListView(
            list = list,
            onClick = onSelectMovie,
            modifier = Modifier.padding(top = 16.dp)
        )
    }


}


@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {


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