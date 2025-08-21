package com.sidspace.stary.home.presentation.screen


import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.HorizontalHomeList
import com.example.ui.ShimmerMovies
import com.example.ui.model.CollectionUi
import com.example.ui.model.MovieUi
import com.example.ui.model.ResultData
import com.example.ui.shimmerEffect
import com.example.ui.uikit.Purple40
import com.example.ui.uikit.poppinsFort
import com.example.ui.utils.InitRatingView
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.absoluteValue

@Composable

fun ListScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel(),
    onSelectMovie: (Long) -> Unit,
    onSelectListMovies: (String, String) -> Unit,
    toErrorScreen: () -> Unit,
    toCollectionScreen: () -> Unit
) {

    val state = mainViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.effect.collectLatest { effect ->
            when (effect) {
                is MainEffect.OnSelectCollection -> {
                    onSelectListMovies(effect.name, effect.slug)
                }

                is MainEffect.OnSelectMovie -> {
                    onSelectMovie(effect.id)
                }

                MainEffect.OsSelectCollections -> {
                    toCollectionScreen()
                }

                MainEffect.ToErrorScreen -> {
                    toErrorScreen()
                }
            }
        }
    }

    MainScreen(
        state = state,
        toErrorScreen = {
            mainViewModel.onIntent(MainIntent.ToErrorScreen)
        },
        toCollectionScreen = {
            mainViewModel.onIntent(MainIntent.OsSelectCollections)
        }, onSelectMovie = {
            mainViewModel.onIntent(MainIntent.OnSelectMovie(it))
        }, onSelectListMovies = { name, slug ->
            mainViewModel.onIntent(MainIntent.OnSelectCollection(name, slug))
        }, modifier = modifier
    )


}

@Composable
private fun MainScreen(
    state: State<MainState>,
    onSelectMovie: (Long) -> Unit,
    toErrorScreen: () -> Unit,
    onSelectListMovies: (String, String) -> Unit,
    toCollectionScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()

    ) {

        item {

            TopBannedList(
                state = state,
                toErrorScreen = toErrorScreen,
                onSelectMovie = onSelectMovie
            )
        }

        item {

            MainListMovies(
                state = state,
                toErrorScreen = toErrorScreen,
                onSelectMovie = onSelectMovie,
                onSelectListMovies = onSelectListMovies
            )
        }


        item {

            InitCollections(
                state = state,
                toCollectionScreen = toCollectionScreen,
                onSelectCollection = onSelectListMovies,
                toErrorScreen = toErrorScreen
            )
        }


    }
}

@Composable
fun TopBannedList(
    state: State<MainState>,
    onSelectMovie: (Long) -> Unit,
    toErrorScreen: () -> Unit,
    modifier: Modifier = Modifier
) {

    val configuration = LocalConfiguration.current
    val height = (configuration.screenWidthDp - 90) * 1.5
    val heightBox = height + 30

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(heightBox.dp)
    ) {


        AnimatedContent(
            targetState = state.value.listTopBanned,
            /*transitionSpec = { fadeIn() togetherWith ExitTransition.None }*/
        ) { state ->
            when (state) {
                is ResultData.Error -> {
                    LaunchedEffect(Unit) {
                        toErrorScreen()
                    }
                }

                ResultData.Loading -> {
                    ShimmerTop()
                }

                is ResultData.Success -> {
                    Column(modifier = Modifier.padding(top = 16.dp)) {

                        val pagerState =
                            rememberPagerState(
                                initialPage = 0,
                                pageCount = { state.data.size })


                        Text(
                            "Топ ожидаемых фильмов",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 20.dp)
                                .padding(bottom = 12.dp)
                        )

                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(height.dp)
                                .padding(top = 4.dp),
                            contentPadding = PaddingValues(end = 89.dp, start = 16.dp)
                        ) { page ->
                            InitPagerCard(
                                index = page,
                                pagerState = pagerState,
                                item = state.data[page],
                                onSelectMovie = onSelectMovie
                            )
                        }
                    }
                }

                ResultData.None -> {

                }
            }
        }

    }
}

@Composable
fun MainListMovies(
    state: State<MainState>,
    onSelectMovie: (Long) -> Unit,
    toErrorScreen: () -> Unit,
    onSelectListMovies: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(

        targetState = state.value.listHomePage,
        /*transitionSpec = { fadeIn() togetherWith ExitTransition.None }*/
    ) { state ->

        when (state) {

            ResultData.Error -> {
                toErrorScreen()
            }

            ResultData.Loading -> {
                Column {
                    ShimmerMovies()
                    ShimmerMovies()
                    ShimmerMovies()
                    ShimmerMovies()
                }
            }

            is ResultData.Success -> {
                Column {
                    val d = state.data

                    d.forEach { result ->


                        when (val collection = result.value) {

                            ResultData.Loading -> {

                            }

                            is ResultData.Success -> {

                                HorizontalHomeList(
                                    list = collection.data.take(10),
                                    label = (result.key as Pair<*, *>).first.toString(),
                                    onSelectMovie = onSelectMovie,
                                    onClickHeader = {
                                        onSelectListMovies(
                                            (result.key as Pair<*, *>).first.toString(),
                                            (result.key as Pair<*, *>).second.toString()
                                        )
                                    })

                                /*InitRow(
                                    label = (result.key as Pair<*, *>).first.toString(),
                                    onClick = {
                                        onSelectListMovies(
                                            (result.key as Pair<*, *>).first.toString(),
                                            (result.key as Pair<*, *>).second.toString()
                                        )
                                    })

                                LazyRow(
                                    modifier = Modifier.padding(top = 10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp)
                                ) {

                                    itemsIndexed(
                                        collection.data.take(10),
                                        key = { _, item -> item.id }) { index, item ->


                                        MovieCardHorizontal(
                                            id = item.id,
                                            name = item.name,
                                            enName = item.enName,
                                            previewUrl = item.previewUrl!!,
                                            score = item.score,
                                            index = index,
                                            onSelectMovie = onSelectMovie
                                        )

                                    }
                                }*/

                            }

                            is ResultData.Error -> {

                            }

                            ResultData.None -> {

                            }

                            null -> {

                            }
                        }


                    }
                }
            }

            ResultData.None -> {

            }
        }
    }
}

@Composable
fun ShimmerTop(modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val height = (configuration.screenWidthDp - 90) * 1.5

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    Column {
        Box(
            modifier = Modifier
                .padding(start = 16.dp, bottom = 14.dp, top = 16.dp)
                .width(200.dp)
                .height(22.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect()
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp)
                .padding(top = 4.dp),
            userScrollEnabled = false,
            contentPadding = PaddingValues(end = 89.dp, start = 16.dp)
        ) { page ->

            val pageOffSet = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            Column(
                modifier = Modifier
                    .padding(end = 15.dp)
                    .fillMaxSize()
                    .graphicsLayer {
                        lerp(
                            start = 0.80f.dp,
                            stop = 1f.dp,
                            fraction = 1f - pageOffSet.absoluteValue.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale.value
                            scaleY = scale.value
                        }
                    }) {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(12.dp),
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmerEffect()
                    )
                }

                Column(modifier = Modifier.padding(top = 14.dp)) {
                    Box(
                        modifier = Modifier
                            .width(250.dp)
                            .height(20.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .shimmerEffect()
                    )
                }
            }

        }
    }
}


@Composable

fun LoadingScreen(modifier: Modifier = Modifier) {

}

@Composable

fun InitCollections(
    modifier: Modifier = Modifier,
    state: State<MainState>,
    toCollectionScreen: () -> Unit,
    onSelectCollection: (String, String) -> Unit,
    toErrorScreen: () -> Unit
) {
    when (val data = state.value.listCollection) {

        ResultData.Error -> {

            toErrorScreen()
        }

        ResultData.Loading -> {

        }

        is ResultData.Success -> {
            ShowCollection(
                list = data.data,
                toCollectionScreen = toCollectionScreen,
                onSelectCollection = onSelectCollection
            )
        }

        ResultData.None -> {

        }
    }
}

@Composable
fun InitRow(modifier: Modifier = Modifier, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp)
            .padding(start = 16.dp)
            .padding(vertical = 10.dp)
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f),
            text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )





        Text(
            "Все",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Purple40,
            modifier = Modifier.clickable {
                onClick()
            }
        )
        Icon(
            modifier = Modifier.size(14.dp),
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Все",
            tint = Purple40
        )
    }
}

@Composable
fun ShowCollection(
    modifier: Modifier = Modifier,
    list: List<CollectionUi>,
    toCollectionScreen: () -> Unit,
    onSelectCollection: (String, String) -> Unit
) {


    InitRow(label = "Коллекции", onClick = toCollectionScreen)


    LazyRow(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 16.dp)
            .fillMaxWidth()
            .height(100.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
    ) {
        items(list) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                    .clickable { onSelectCollection(it.name, it.slug) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = it.name.split(" ").joinToString(separator = "\n"),
                    fontFamily = poppinsFort,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun ListMovies(modifier: Modifier = Modifier, list: List<MovieUi>, onSelectMovie: (Long) -> Unit) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {

        itemsIndexed(list) { index, item ->

            MovieCard(
                id = item.id,
                name = item.name,
                enName = item.enName,
                previewUrl = item.previewUrl!!,
                score = item.score,
                index = index,
                onSelectMovie = onSelectMovie
            )

        }
    }

}


@Composable
fun InitPagerCard(
    modifier: Modifier = Modifier,
    index: Int,
    pagerState: PagerState,
    item: MovieUi,
    onSelectMovie: (Long) -> Unit
) {
    val pageOffSet = (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction
    Column(
        modifier = Modifier
            .padding(end = 15.dp)
            .fillMaxSize()
            .graphicsLayer {
                lerp(
                    start = 0.80f.dp,
                    stop = 1f.dp,
                    fraction = 1f - pageOffSet.absoluteValue.coerceIn(0f, 1f)
                ).also { scale ->
                    scaleX = scale.value
                    scaleY = scale.value
                }
            }) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .clickable {
                    onSelectMovie(item.id!!)
                },
            elevation = CardDefaults.cardElevation(12.dp),
            shape = RoundedCornerShape(32.dp)
        ) {
            AsyncImage(
                //model = listOfPoster[index],
                model = item.previewUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .shimmerEffect()

            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp, top = 10.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = item.name ?: "null",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    id: Long,
    name: String?,
    enName: String?,
    previewUrl: String,
    score: Double?,
    index: Int,
    onSelectMovie: (Long) -> Unit
) {

    var scale by remember { mutableStateOf(ContentScale.Crop) }
    var isShimmer by remember { mutableStateOf(true) }
    //println("name = " + item.name)
    Box(
        modifier = Modifier
            /*.then(
            if (index == 0) Modifier.padding(start = 16.dp)
            else Modifier
        )*/
            .fillMaxWidth()
            .height(255.dp)
            .width(150.dp)
            .clickable {
                onSelectMovie(id)
            }) {
        Column(

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .width(150.dp)
                    .height(215.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .then(
                        if (isShimmer) Modifier.shimmerEffect()
                        else Modifier
                    ),
                model = ImageRequest.Builder(LocalContext.current).data(previewUrl)
                    .crossfade(true)

                    .listener(onStart = {
                        scale = ContentScale.Crop
                    }, onSuccess = { _, _ ->
                        isShimmer = false
                        scale = ContentScale.FillBounds
                    })
                    .crossfade(true).build(),
                contentScale = scale,
                contentDescription = null,
                error = painterResource(com.example.ui.R.drawable.ic_placeholder_4)
            )
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = name ?: enName ?: "null",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,

                fontSize = 14.sp
            )
        }

        InitRatingView(score)

    }
}