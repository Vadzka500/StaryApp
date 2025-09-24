package com.sidspace.stary.home.presentation.screen


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.sidspace.stary.ui.HorizontalHomeList
import com.sidspace.stary.ui.ShimmerMovies
import com.sidspace.stary.ui.model.CollectionUi
import com.sidspace.stary.ui.model.MoviePreviewUi
import com.sidspace.stary.ui.model.ResultData
import com.sidspace.stary.ui.shimmerEffect
import com.sidspace.stary.ui.uikit.Dimens
import com.sidspace.stary.ui.uikit.Purple40
import com.sidspace.stary.ui.uikit.poppinsFort
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
    modifier: Modifier = Modifier,
    onSelectMovie: (Long) -> Unit,
    toErrorScreen: () -> Unit
) {

    val configuration = LocalConfiguration.current
    val height =
        (configuration.screenWidthDp - MainState.TOP_BANNED_HORIZONTAL_PADDING) * Dimens.ImageHeightScaleByWidth
    val heightBox = height + MainState.TOP_BANNED_TEXT_HEIGHT

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(heightBox.dp)
    ) {


        AnimatedContent(
            targetState = state.value.listTopBanned,
            transitionSpec = { fadeIn() togetherWith fadeOut() }
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
                            stringResource(com.sidspace.stary.home.presentation.R.string.top_expected_movies),
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
    modifier: Modifier = Modifier,
    onSelectMovie: (Long) -> Unit,
    toErrorScreen: () -> Unit,
    onSelectListMovies: (String, String) -> Unit,

    ) {
    AnimatedContent(
        modifier = modifier,
        targetState = state.value.listHomePage,
        transitionSpec = { scaleIn(
            initialScale = 0.99f,
            //animationSpec = tween(100)
        ) + fadeIn() togetherWith fadeOut() }
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
                                    list = collection.data.take(MainState.MAX_MOVIES),
                                    label = (result.key as Pair<*, *>).first.toString(),
                                    onSelectMovie = onSelectMovie,
                                    onClickHeader = {
                                        onSelectListMovies(
                                            (result.key as Pair<*, *>).first.toString(),
                                            (result.key as Pair<*, *>).second.toString()
                                        )
                                    })

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
    val height =
        (configuration.screenWidthDp - MainState.TOP_BANNED_HORIZONTAL_PADDING) * Dimens.ImageHeightScaleByWidth

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    Column {
        Box(
            modifier = modifier
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

            ShimmerItemTop(page, pagerState)

        }
    }
}

@Composable
fun ShimmerItemTop(page: Int, pagerState: PagerState) {
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
            }
    ) {
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

@Composable

fun InitCollections(
    state: State<MainState>,
    modifier: Modifier = Modifier,
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
                modifier = modifier,
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
        modifier = modifier
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
            stringResource(com.sidspace.stary.home.presentation.R.string.all),
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
            contentDescription = stringResource(com.sidspace.stary.home.presentation.R.string.all),
            tint = Purple40
        )
    }
}

@Composable
fun ShowCollection(
    list: List<CollectionUi>,
    modifier: Modifier = Modifier,
    toCollectionScreen: () -> Unit,
    onSelectCollection: (String, String) -> Unit
) {


    InitRow(
        label = stringResource(com.sidspace.stary.home.presentation.R.string.collection),
        onClick = toCollectionScreen
    )


    LazyRow(
        modifier = modifier
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
fun InitPagerCard(
    index: Int,
    pagerState: PagerState,
    item: MoviePreviewUi,
    modifier: Modifier = Modifier,
    onSelectMovie: (Long) -> Unit
) {
    val pageOffSet = (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction
    Column(
        modifier = modifier
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
                    onSelectMovie(item.id)
                },
            elevation = CardDefaults.cardElevation(12.dp),
            shape = RoundedCornerShape(32.dp)
        ) {
            AsyncImage(
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
