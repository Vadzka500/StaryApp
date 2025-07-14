package com.example.navwithapinothing_2.features.screen.RandomScreen

import android.annotation.SuppressLint
import androidx.compose.animation.core.EaseInOutCirc
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.navwithapinothing_2.R
import com.example.navwithapinothing_2.features.screen.MovieViewModel
import com.example.navwithapinothing_2.features.screen.MoviesListScreen.ListCollectionResult
import com.example.navwithapinothing_2.features.theme.Purple40
import com.example.navwithapinothing_2.features.theme.poppinsFort
import com.example.navwithapinothing_2.utils.RandomFiltersOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


val listOfPoster = listOf(
    "https://image.openmoviedb.com/kinopoisk-images/1704946/ade65029-952f-488d-87b2-20c676970151/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/1946459/146b1b20-347b-4b6a-98c8-fdc2c75495cb/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/1900788/5fb4b4c8-6d73-46dd-a4c8-e0bf953f8481/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/1704946/245177a8-7500-41fe-aebb-c1eef3575974/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/1773646/96d93e3a-fdbf-4b6f-b02d-2fc9c2648a18/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/1599028/87753e26-5f31-4fad-be34-2522840420f0/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/1629390/f073cd3d-5662-492a-9131-1081034edd88/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/1773646/50d44821-4afe-4343-a093-4eb9d10a7f37/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/4483445/00a0489e-2bef-4727-b604-19351fd711d7/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/1599028/7c3460dc-344d-433f-8220-f18d86c8397d/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/1599028/68e30424-60d9-4dcc-b21d-ba40eaa70b06/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/4303601/b35131e0-041b-4ebc-af90-7104f2f75821/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/1773646/a575032b-1b9f-4ea4-adf2-a3dd3359acc8/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/4774061/a9819b63-adb1-472a-8711-88736a021f16/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/1600647/a74d86f6-a78a-46dd-8c1e-65cd79984902/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/10835644/7ab9e6b0-87d8-4c6d-96ef-e5c519ce8fa3/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/1946459/84934543-5991-4c93-97eb-beb6186a3ad7/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/1629390/4b8fefbb-d17f-43cc-a3f4-33c639227c88/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/1599028/cdd49af8-7ba4-42ee-9f92-10ba7e3d021b/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/1898899/bbf6d634-b936-44bc-85d4-605d552f5d50/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/4303601/d2be057c-8fef-4ca8-966d-64c549a74715/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/1600647/e5013408-8374-400b-a444-b9753e9da28d/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/1900788/6f631486-e947-487d-94d6-41c2b5a8f5a0/x1000",
    "https://image.openmoviedb.com/kinopoisk-images/1629390/72ba01f5-4cb3-49b6-9dd5-1edffe4411d5/x1000",
)

//val listOfYears = (1874..Calendar.getInstance().get(Calendar.YEAR)).toList()

enum class RandomStatus {
    SUCCESS, ERROR, LOADING, EMPTY, NONE
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
@Preview
fun SliderScreen(
    modifier: Modifier = Modifier,
    movieViewModel: MovieViewModel = hiltViewModel(),
    randomViewModel: RandomViewModel = hiltViewModel(),
    onSelectMovie: (Long) -> Unit
) {

    val state by randomViewModel.state.collectAsState()

    val status = remember {
        derivedStateOf { state.randomMovie }
    }

    LaunchedEffect(Unit) {
        randomViewModel.effect.collectLatest { effect ->
            when (effect) {

                else -> {}
            }
        }
    }



    InitView(
        status = status,
        isSearch = state.isSearch,
        setShowFilters = {
            randomViewModel.onIntent(RandomIntent.SetShowFilters(it))
        },
        setSearch = {
            randomViewModel.onIntent(RandomIntent.IsSearch(it))
        },
        random = {
            randomViewModel.onIntent(RandomIntent.Random)
        }, updateCurrentPage = { page, offset ->
            randomViewModel.onIntent(RandomIntent.UpdateCurrentPage(page, offset))
        }, onSelectMovie = onSelectMovie, initialPage = state.initialPage, modifier = modifier
    )


    if (state.isFiltersShown) {

        FiltersPopup(
            filters = state.filter,
            listOfCollectionResult = state.listOfCollections,
            filerIntent = randomViewModel::onIntent
        )

    }

}

@Composable

fun InitView(
    random: () -> Unit,
    onSelectMovie: (Long) -> Unit,
    setSearch: (Boolean) -> Unit,
    setShowFilters: (Boolean) -> Unit,
    updateCurrentPage: (Int, Float) -> Unit,
    status: State<MovieRandomStatus>,
    initialPage: Int,
    isSearch: Boolean,
    modifier: Modifier = Modifier,
) {

    var posterUrl by remember {
        mutableStateOf("")
    }

    var nameOfMovie by remember { mutableStateOf("") }

    var movieId by remember {
        mutableLongStateOf(0L)
    }

    var statusFlow = remember {
        mutableStateOf(RandomStatus.NONE)
    }

    when (val data = status.value) {

        is MovieRandomStatus.Success -> {
            posterUrl = data.movie.poster!!.previewUrl!!
            nameOfMovie = data.movie.name!! + " (" + data.movie.year + ")"
            movieId = data.movie.id!!
            statusFlow.value = RandomStatus.SUCCESS
        }

        MovieRandomStatus.None -> {
            nameOfMovie = stringResource(R.string.text_random_none)

        }



       /* else -> {
            statusFlow.value = RandomStatus.LOADING
        }*/
        MovieRandomStatus.Error -> {
            nameOfMovie = ""
            statusFlow.value = RandomStatus.ERROR
        }
        MovieRandomStatus.Loading -> {
            statusFlow.value = RandomStatus.LOADING
        }
    }

    println("STATUSS = " + statusFlow.value)


    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { Int.MAX_VALUE },
        initialPageOffsetFraction = 0f
    )

    DisposableEffect(Unit) {
        onDispose {
            updateCurrentPage(pagerState.currentPage, pagerState.currentPageOffsetFraction)
        }
    }

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).size(coil.size.Size.ORIGINAL)
            .data(posterUrl).build()
    )


    val coroutineScope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    Column(modifier = modifier.verticalScroll(state = scrollState)) {

        RandomCardPager(
            pagerState = pagerState,
            painter = painter,
            posterUrl = posterUrl,
            onSelectMovie = onSelectMovie,
            status = status,
            movieId = movieId
        )


        val displayMetrics = LocalContext.current.resources.displayMetrics
        val widthPx = displayMetrics.widthPixels

        val density = LocalDensity.current
        val screenWidthPx = with(density) {
            widthPx - 140.dp.roundToPx()
        }

        val scrollWidth = screenWidthPx * 100f

        var targetPage = remember { mutableIntStateOf(pagerState.currentPage + 100) }


        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (button, filters, text) = createRefs()

            androidx.compose.animation.AnimatedVisibility(
                visible = !isSearch,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut(),
                modifier = Modifier.constrainAs(text) {
                    start.linkTo(anchor = parent.start)
                    end.linkTo(anchor = parent.end)
                    top.linkTo(anchor = parent.top, margin = 15.dp)
                }) {


                Text(
                    text = nameOfMovie,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
            }

            ElevatedButton(
                shape = CircleShape,
                onClick = {

                    anim(
                        coroutineScope = coroutineScope,
                        setSearch = setSearch,
                        scrollWidth = scrollWidth,
                        pagerState = pagerState,
                        targetPage = targetPage,
                        random = random,
                        randomStatus = status,
                        widthItem = screenWidthPx,
                        easy = EaseInOutCirc
                    )

                    coroutineScope.launch {
                        delay(500)
                        random()
                    }


                },
                elevation = ButtonDefaults.elevatedButtonElevation(8.dp),
                enabled = !isSearch,
                colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                modifier = Modifier
                    .size(150.dp)
                    .constrainAs(button) {
                        start.linkTo(anchor = parent.start)
                        end.linkTo(anchor = parent.end)
                        bottom.linkTo(anchor = parent.bottom, margin = 25.dp)
                        top.linkTo(anchor = parent.top, margin = 90.dp)
                    }) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "ПОИСК",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFort,
                    fontSize = 20.sp
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable {
                        setShowFilters(true)
                    }
                    .constrainAs(filters) {
                        start.linkTo(anchor = button.end, margin = 30.dp)
                        top.linkTo(anchor = parent.top, margin = 65.dp)
                        bottom.linkTo(anchor = parent.bottom)
                    }, contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(25.dp),
                    painter = painterResource(R.drawable.settings),
                    contentDescription = null
                )
            }


        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {


        }

    }

}

@Composable
private fun RandomCardPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    painter: AsyncImagePainter,
    posterUrl: String,
    onSelectMovie: (Long) -> Unit,
    status: State<MovieRandomStatus>,
    movieId: Long
) {
    val configuration = LocalConfiguration.current
    val height = (configuration.screenWidthDp - 140) * 1.5

    HorizontalPager(
        modifier = Modifier
            .padding(top = 20.dp, bottom = 20.dp)
            .height(height.dp),
        state = pagerState,
        userScrollEnabled = false,
        contentPadding = PaddingValues(horizontal = 70.dp)
    ) { page ->
        if (page > 100 && page % 100 == 1) {
            println("state = " + painter.state)
            println("state = " + painter.request.data)
            PagerCardImage(
                posterUrl = posterUrl,
                pagerState,
                page,
                painter = painter,
                status = status.value,
                onSelectMovie = onSelectMovie,
                movieId = movieId
            )
        } else {
            PagerCardImage(
                posterUrl = listOfPoster[page % 24],
                pagerState,
                page,
                onSelectMovie = onSelectMovie,
                status = status.value
            )
        }
    }
}



fun anim(
    coroutineScope: CoroutineScope,
    setSearch: (Boolean) -> Unit,
    scrollWidth: Float,
    pagerState: PagerState,
    targetPage: MutableState<Int>,
    random: () -> Unit,
    randomStatus: State<MovieRandomStatus>,
    widthItem: Int,
    easy: Easing,
) {
    println("status = ${randomStatus.value}")
    println("anim")

    coroutineScope.launch {
        setSearch(true)
        targetPage.value += 100 //
        pagerState.animateScrollBy(
            scrollWidth,
            animationSpec = tween<Float>(
                3000/*, easing = LinearOutSlowInEasing*//*, easing = Ease*/,
                easing = easy
            )
        ).let {
            //coroutineScope.launch {
            //pagerState.animateScrollToPage(targetPage.value)
            //targetPage.value += 100
            //}
            setSearch(false)
        }

    }


    coroutineScope.launch {
        delay(1500)
        println("randomStatus = " + randomStatus.value)
        println("randomStatus1 = " + pagerState.getOffsetDistanceInPages(targetPage.value + 100))

        val width = pagerState.getOffsetDistanceInPages(targetPage.value + 100) * widthItem
        println("randomStatus2 = " + width)
        if (randomStatus.value is MovieRandomStatus.Loading) {

            anim(
                coroutineScope,
                setSearch,
                width,
                pagerState,
                targetPage,
                random,
                randomStatus,
                widthItem,
                EaseOutCubic
            )
        }
    }
}

@Composable
fun PagerCardImage(
    posterUrl: String,
    pagesState: PagerState,
    index: Int,
    painter: Painter? = null,
    status: MovieRandomStatus,
    onSelectMovie: (Long) -> Unit,
    movieId: Long = -1
) {

    val pageOffSet = (pagesState.currentPage - index) + pagesState.currentPageOffsetFraction
    ElevatedCard(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                lerp(
                    start = 0.85f.dp,
                    stop = 1f.dp,
                    fraction = 1f - pageOffSet.absoluteValue.coerceIn(0f, 1f)
                ).also { scale ->
                    scaleX = scale.value
                    scaleY = scale.value
                }
            }
            .clickable { onSelectMovie(movieId) },
        elevation = CardDefaults.cardElevation(12.dp),
        shape = RoundedCornerShape(32.dp)
    ) {
        if (painter == null) {
            AsyncImage(
                model = if (index == 1) R.drawable.question_sign_icon_icons_com_73445 else posterUrl,
                contentDescription = null,
                contentScale = if (index == 1) ContentScale.Fit else ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()

            )
        } else {

            if (status is MovieRandomStatus.Success) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxSize()
                )
            } else if (status is MovieRandomStatus.Error) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(140.dp),
                        painter = painterResource(R.drawable.empty_result),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        stringResource(R.string.empty_result_random),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

    }

}


