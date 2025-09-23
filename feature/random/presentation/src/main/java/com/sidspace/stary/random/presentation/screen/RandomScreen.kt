package com.sidspace.stary.random.presentation.screen


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
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.sidspace.stary.random.presentation.FiltersPopup
import com.sidspace.stary.random.presentation.R
import com.sidspace.stary.ui.model.MoviePreviewUi
import com.sidspace.stary.ui.model.ResultData
import com.sidspace.stary.ui.uikit.Dimens
import com.sidspace.stary.ui.uikit.Purple40
import com.sidspace.stary.ui.uikit.poppinsFort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
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

enum class RandomStatus {
    SUCCESS, ERROR, LOADING, NONE
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable

fun SliderScreen(
    modifier: Modifier = Modifier,
    randomViewModel: RandomViewModel = hiltViewModel(),
    onSelectMovie: (Long) -> Unit
) {

    val state by randomViewModel.state.collectAsState()

    val status = remember {
        derivedStateOf { state.randomMovie }
    }

    InitView(
        status = status,
        isSearch = state.isSearch,
        isBadgeShown = state.isBadgeShown,
        setShowFilters = {
            randomViewModel.onIntent(RandomIntent.SetShowFilters(it))
        },
        setSearch = {
            randomViewModel.onIntent(RandomIntent.IsSearch(it))
        },
        random = {
            randomViewModel.onIntent(RandomIntent.Random)
        },
        updateCurrentPage = { page, offset ->
            randomViewModel.onIntent(RandomIntent.UpdateCurrentPage(page, offset))
        },
        onSelectMovie = onSelectMovie, initialPage = state.initialPage, modifier = modifier,
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
    status: State<ResultData<MoviePreviewUi>>,
    initialPage: Int,
    isSearch: Boolean,
    isBadgeShown: Boolean,
    modifier: Modifier = Modifier,
    random: () -> Unit,
    onSelectMovie: (Long) -> Unit,
    setSearch: (Boolean) -> Unit,
    setShowFilters: (Boolean) -> Unit,
    updateCurrentPage: (Int, Float) -> Unit,
) {

    var posterUrl by remember {
        mutableStateOf("")
    }

    var nameOfMovie by remember { mutableStateOf("") }

    var movieId by remember {
        mutableLongStateOf(0L)
    }

    val statusFlow = remember {
        mutableStateOf(RandomStatus.NONE)
    }

    when (val data = status.value) {

        is ResultData.Success -> {
            posterUrl = data.data.previewUrl!!
            nameOfMovie = data.data.name + " (" + data.data.year + ")"
            movieId = data.data.id
            statusFlow.value = RandomStatus.SUCCESS
        }

        ResultData.None -> {
            nameOfMovie =
                stringResource(R.string.text_random_none)

        }

        ResultData.Error -> {
            nameOfMovie = ""
            statusFlow.value = RandomStatus.ERROR
        }

        ResultData.Loading -> {
            statusFlow.value = RandomStatus.LOADING
        }
    }


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
        ImageRequest.Builder(LocalContext.current).size(Size.ORIGINAL)
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

        val scrollWidth = screenWidthPx * RandomState.PAGER_OFFSET

        var targetPage = remember { mutableIntStateOf(pagerState.currentPage + RandomState.PAGER_OFFSET.toInt()) }



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
                        delay(RandomState.DELAY_API_REQUEST)
                        random()
                    }


                },
                elevation = ButtonDefaults.elevatedButtonElevation(8.dp),
                enabled = !isSearch,
                colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                modifier = Modifier
                    .size(120.dp)
                    .constrainAs(button) {
                        start.linkTo(anchor = parent.start)
                        end.linkTo(anchor = parent.end)
                        bottom.linkTo(anchor = parent.bottom, margin = 25.dp)
                        top.linkTo(anchor = parent.top, margin = 90.dp)
                    }) {
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = stringResource(R.string.search),
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFort,
                    fontSize = 18.sp
                )
            }

            BadgedBox(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .clickable {
                        setShowFilters(true)
                    }
                    .constrainAs(filters) {
                        start.linkTo(anchor = button.end, margin = 30.dp)
                        top.linkTo(anchor = parent.top, margin = 65.dp)
                        bottom.linkTo(anchor = parent.bottom)
                    }, badge = {
                    if (isBadgeShown) {
                        Badge(
                            containerColor = Color.Red
                        )
                    }
                }
            ) {
                Icon(
                    modifier = Modifier
                        .size(25.dp)
                        .align(Alignment.Center),
                    painter = painterResource(com.sidspace.stary.ui.R.drawable.settings),

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

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun RandomCardPager(
    pagerState: PagerState,
    painter: AsyncImagePainter,
    posterUrl: String,
    status: State<ResultData<MoviePreviewUi>>,
    movieId: Long,
    modifier: Modifier = Modifier,
    onSelectMovie: (Long) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val height = (configuration.screenWidthDp - RandomState.PAGER_HORIZONTAL_PADDING) * Dimens.ImageHeightScaleByWidth

    HorizontalPager(
        modifier = modifier
            .padding(top = 20.dp, bottom = 20.dp)
            .height(height.dp),
        state = pagerState,
        userScrollEnabled = false,
        contentPadding = PaddingValues(horizontal = 70.dp)
    ) { page ->
        if (page > RandomState.PAGER_OFFSET && page % RandomState.PAGER_OFFSET.toInt() == 1) {

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


@Suppress("LongParameterList")
fun anim(
    coroutineScope: CoroutineScope,
    setSearch: (Boolean) -> Unit,
    scrollWidth: Float,
    pagerState: PagerState,
    targetPage: MutableState<Int>,
    random: () -> Unit,
    randomStatus: State<ResultData<MoviePreviewUi>>,
    widthItem: Int,
    easy: Easing,
) {

    coroutineScope.launch {
        setSearch(true)
        targetPage.value += RandomState.PAGER_OFFSET.toInt() //
        pagerState.animateScrollBy(
            scrollWidth,
            animationSpec = tween<Float>(
                RandomState.PAGER_DURATION,
                easing = easy
            )
        ).let {
            setSearch(false)
        }

    }


    coroutineScope.launch {
        delay(RandomState.DELAY_REPEAT_ANIMATION_SCROLL)

        val width = pagerState.getOffsetDistanceInPages(targetPage.value + RandomState.PAGER_OFFSET.toInt()) * widthItem
        if (randomStatus.value is ResultData.Loading) {

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
    status: ResultData<MoviePreviewUi>,
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
            },
        elevation = CardDefaults.cardElevation(12.dp),
        shape = RoundedCornerShape(32.dp)
    ) {
        if (painter == null) {
            AsyncImage(
                model = if (index == 1) R.drawable.question_sign_icon_icons_com_73445 else posterUrl,
                contentDescription = null,
                contentScale = if (index == 1) ContentScale.Fit else ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize(),

                )
        } else {

            if (status is ResultData.Success) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onSelectMovie(movieId) }
                )
            } else if (status is ResultData.Error) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(140.dp),
                        painter = painterResource(com.sidspace.stary.ui.R.drawable.empty_result),
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


