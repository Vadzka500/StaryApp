package com.example.navwithapinothing_2.ui.screen

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas

import androidx.compose.foundation.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.filled.ViewStream
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.R
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.ui.screen.MovieScreen.shimmerEffect
import com.example.navwithapinothing_2.ui.screen.MoviesListScreen.MovieCardGrid
import com.example.navwithapinothing_2.ui.screen.MoviesListScreen.MovieCardGridShimmer
import com.example.navwithapinothing_2.ui.theme.Purple40

import com.example.navwithapinothing_2.ui.theme.poppinsFort
import com.example.navwithapinothing_2.utils.ScoreManager
import com.example.navwithapinothing_2.utils.TimeManager
import com.skydoves.cloudy.cloudy

/**
 * @Author: Vadim
 * @Date: 23.04.2025
 */
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AllMoviesScreen(
    modifier: Modifier = Modifier,
    label: String,
    slug: String,
    onSelectMovie: (Long) -> Unit,
    onBack: () -> Unit,
    movieViewModel: MovieViewModel = hiltViewModel(),
    lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {

    val state = movieViewModel.state_movie_collections_all.collectAsState()
    val navController = rememberNavController()
    LaunchedEffect(slug) {

        movieViewModel.getMoviesByCollection(slug, limit = 250)
    }

    var isGridView by remember {
        mutableStateOf(true)
    }

    /*BackHandler {
        movieViewModel.resetStateMovieByCollectionAll()
        navController.navigateUp()
    }*/


    var isVisibleFilter by remember {
        mutableStateOf(false)
    }



    Box(modifier = Modifier.padding(top = 5.dp)) {

        when (val data = state.value) {

            Result.Loading -> {
                LoadList()
            }

            is Result.Success<*> -> {
                InitList(
                    list = data.data as List<MovieDTO>,
                    onClick = onSelectMovie,
                    isGridViewType = isGridView
                )
            }

            is Result.Error<*> -> {

            }
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)

        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
                    .align(Alignment.TopCenter),
                text = label, textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 18.sp
            )

            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .clip(
                        CircleShape
                    )
                    .clickable {
                        onBack()
                    }
                    .padding(8.dp)
                    .align(Alignment.TopStart))

            Icon(
                painter = painterResource(R.drawable.settings),
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .clip(
                        CircleShape
                    )
                    .clickable {
                        isVisibleFilter = !isVisibleFilter
                    }
                    .padding(8.dp)
                    .align(Alignment.TopEnd)) // внутренний отступ, чтобы иконка была не впритык)

        }

        val alpha by animateFloatAsState(
            targetValue = if (isVisibleFilter) 0.4f else 0f,
            animationSpec = tween(durationMillis = 300)
        )




        AnimatedVisibility(
            visible = isVisibleFilter,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxSize()
                .background(Color.Black.copy(alpha = alpha))
                //.cloudy(radius = 15)
                .clickable(indication = null, interactionSource = null) { isVisibleFilter = false }
        ) {

        }





        AnimatedVisibility(
            visible = isVisibleFilter,
            enter = expandVertically(),
            exit = shrinkVertically(),
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxWidth()
        ) {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp
                ),
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {

                    Row() {
                        FilterChip(
                            modifier = Modifier.padding(5.dp),
                            shape = RoundedCornerShape(15.dp),
                            onClick = {
                                isGridView = !isGridView
                            },
                            label = {
                                Text("Таблица")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.GridView,
                                    contentDescription = "",
                                    tint = Purple40
                                )
                            },
                            selected = isGridView,

                            )

                        FilterChip(
                            modifier = Modifier.padding(5.dp),
                            shape = RoundedCornerShape(15.dp),
                            onClick = {
                                isGridView = !isGridView
                            },
                            label = {
                                Text("Список")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ViewList,
                                    contentDescription = "",
                                    tint = Purple40
                                )
                            },
                            selected = !isGridView,

                            )
                    }
                }
            }
        }

    }


}


@Composable
fun LoadList(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 54.dp),
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
fun InitList(list: List<MovieDTO>, onClick: (Long) -> Unit, isGridViewType: Boolean) {
    AnimatedContent(
        targetState = isGridViewType,
        transitionSpec = { fadeIn() togetherWith ExitTransition.None }) { gridViewType ->

        if (gridViewType) {
            GridView(list = list, onClick = onClick)
        } else {
            ListView(list = list, onClick = onClick)
        }
    }

}

@Composable
@Preview(showBackground = true)
fun ListView(modifier: Modifier = Modifier, list: List<MovieDTO>, onClick: (Long) -> Unit) {
    LazyColumn(
        modifier = Modifier

            .padding(top = 54.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(list) { index, item ->
            CardList(
                movie = item,
                index = index,
                onSelectMovie = { id -> onClick(id) },
                modifier = Modifier
            )
        }
    }
}

@Composable
fun GridView(modifier: Modifier = Modifier, list: List<MovieDTO>, onClick: (Long) -> Unit) {
    LazyVerticalGrid(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 54.dp),
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

@Composable
fun CardList(
    modifier: Modifier = Modifier,
    movie: MovieDTO,
    index: Int,
    onSelectMovie: (Long) -> Unit
) {
    var scale by remember { mutableStateOf(ContentScale.Crop) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onSelectMovie(movie.id!!) }
            .padding(horizontal = 16.dp)
    ) {
        Row() {
            AsyncImage(
                modifier = Modifier
                    .width(80.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp)),
                model = ImageRequest.Builder(LocalContext.current).data(movie.poster?.previewUrl)

                    .listener(onStart = {
                        scale = ContentScale.Crop
                    }, onSuccess = { _, _ ->
                        scale = ContentScale.FillBounds
                    })
                    .crossfade(true).build(),
                contentScale = scale,
                contentDescription = null, error = painterResource(R.drawable.ic_placeholder_4)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = movie.name!!,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFort,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp
                )

                if (!movie.isSeries!!) {
                    Text(
                        text = movie.year.toString(),
                        fontWeight = FontWeight.Normal,
                        fontFamily = poppinsFort,
                        fontSize = 12.sp
                    )
                } else {

                    val seasonCount = movie.seasonsInfo?.let { it ->
                        if (it.isEmpty()) {
                            1
                        } else {
                            it.count { season -> season.number != 0 }
                        }
                    }



                    if (movie.releaseYears != null) {
                        val releaseStart = movie.releaseYears[0].start
                        val releaseEnd = movie.releaseYears[0].end

                        var text = releaseStart.toString()

                        if (releaseStart != null && releaseEnd != null && releaseStart != releaseEnd) {
                            text = "$releaseStart - $releaseEnd"
                        } else if (releaseStart != null && releaseEnd == null) {
                            text =
                                if (seasonCount == 1 || movie.status?.equals("completed") == true) {
                                    releaseStart.toString()
                                } else {
                                    "$releaseStart - н.в."
                                }
                        }

                        Text(
                            text = text,
                            fontWeight = FontWeight.Normal,
                            fontFamily = poppinsFort,
                            fontSize = 12.sp
                        )
                    } else {
                        Text(
                            text = movie.year.toString(),
                            fontWeight = FontWeight.Normal,
                            fontFamily = poppinsFort,
                            fontSize = 12.sp
                        )
                    }
                }


                Row(
                    modifier = Modifier.padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {

                    movie.genres?.take(3)?.forEachIndexed { index, item ->

                        if (index == 1 || index == 2) {
                            Text(
                                text = "•",
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            )
                        }

                        Text(
                            text = item.name.replaceFirstChar { it.uppercase() },
                            fontWeight = FontWeight.Light,
                            fontFamily = poppinsFort,
                            fontSize = 12.sp
                        )

                    }

                }

                //===
                if (movie.rating?.kp != null && movie.rating.kp != 0.0) {

                    val rating = ScoreManager.ratingToFormat(movie.rating.kp)


                    val color = if (rating < 5) {
                        Color.Red
                    } else if (rating < 7) {
                        Color.Gray
                    } else Purple40


                    Text(
                        text = rating.toString(),
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppinsFort,
                        fontSize = 14.sp,
                        color = color
                    )
                }

            }
        }
    }


}