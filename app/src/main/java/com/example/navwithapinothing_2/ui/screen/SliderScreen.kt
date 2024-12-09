package com.example.navwithapinothing_2.ui.screen

import android.annotation.SuppressLint
import android.util.DisplayMetrics
import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.TargetedFlingBehavior
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingState
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.R
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.ui.screen.MovieScreen.MovieViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.random.Random


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

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
@Preview
fun SliderScreen(modifier: Modifier = Modifier, movieViewModel: MovieViewModel = hiltViewModel()) {


    val targetUrl = remember {
        mutableStateOf("")
    }

    val state = movieViewModel.state_random.collectAsState()
    when (val data = state.value) {
        is Result.Error -> {

        }

        is Result.Loading -> {

        }

        is Result.Success<*> -> {
            targetUrl.value = (data.data as MovieDTO).poster!!.previewUrl!!
        }
    }

    InitView(
        targetUrl = targetUrl.value,
        random = { movieViewModel.getRandom() })


}

@Composable
fun InitView(
    modifier: Modifier = Modifier,
    targetUrl: String,
    random: () -> Unit
) {

    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { Int.MAX_VALUE },
        initialPageOffsetFraction = 0f
    )


    var isProgressAnim by remember { mutableStateOf(false) }

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).size(coil.size.Size.ORIGINAL)
            .data(targetUrl).build()
    )
    val coroutineScope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    var showFilter by remember { mutableStateOf(false) }

    Column(modifier = Modifier.verticalScroll(state = scrollState)) {

        val fling = PagerDefaults.flingBehavior(
            state = pagerState,
            pagerSnapDistance = PagerSnapDistance.atMost(50),
        )

        val configuration = LocalConfiguration.current
        val height = (configuration.screenWidthDp - 140) * 1.5

        HorizontalPager(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp)
                .height(height.dp),
            state = pagerState,
            //beyondViewportPageCount = 100,
            //flingBehavior = fling,
            contentPadding = PaddingValues(horizontal = 70.dp)
        ) { page ->
            if (page > 100 && page % 100 == 1) {
                println("state = " + painter.state)
                PagerCardImage(posterUrl = targetUrl, pagerState, page, painter = painter)
            } else {
                PagerCardImage(posterUrl = listOfPoster[page % 24], pagerState, page)
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        val density = LocalDensity.current
        val displayMetrics = LocalContext.current.resources.displayMetrics
        val widthPx = displayMetrics.widthPixels
        val screenWidthPx = with(density) {
            widthPx - 140.dp.roundToPx()
        }

        val scrollWidth = screenWidthPx * 100f

        var targetPage by remember { mutableIntStateOf(101) }



        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (button, filters) = createRefs()
            Button(onClick = {

                coroutineScope.launch {
                    isProgressAnim = true
                    pagerState.animateScrollBy(
                        scrollWidth,
                        animationSpec = tween(5000/*, easing = LinearOutSlowInEasing*/)
                    ).let {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(targetPage)
                            targetPage += 100
                        }
                        isProgressAnim = false
                    }

                }
                coroutineScope.launch {
                    delay(1000)
                    random()
                }


            }, enabled = !isProgressAnim, modifier = Modifier.constrainAs(button) {
                start.linkTo(anchor = parent.start)
                end.linkTo(anchor = parent.end)
                bottom.linkTo(anchor = parent.bottom)
                top.linkTo(anchor = parent.top)
            }) {
                Text(text = "Поехали!")
            }

            Box(modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable {
                    showFilter = true
                }
                .constrainAs(filters) {
                    start.linkTo(anchor = button.end, margin = 30.dp)
                    top.linkTo(anchor = parent.top)
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

    if (showFilter) {
        FiltersPopup(hideFilters = { showFilter = false })

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersPopup(modifier: Modifier = Modifier, hideFilters: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        sheetState = sheetState,
        onDismissRequest = {
            hideFilters()
        }) {
        ListFilters()
    }
}

val listOfTypes = listOf("Все", "Фильмы", "Сериалы", "Мульфильмы", "Аниме")
val listOfGenres = listOf(
    "Любой жанр",
    "Комедия",
    "Ужасы",
    "Триллер",
    "Детектив",
    "Боевик",
    "Вестерн",
    "Драма",
    "Мелодрама",
    "Приключения",
    "Фантастика"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListFilters(modifier: Modifier = Modifier) {

    Column {
        Text("Тип")

        FlowRow() {
            listOfTypes.forEachIndexed { index, it ->


                var selected by remember { mutableStateOf(false) }
                if (index == 0) selected = true

                FilterChip(
                    modifier = Modifier.padding(5.dp),
                    onClick = { selected = !selected },
                    label = {
                        Text(it)
                    },
                    selected = selected,

                    )
            }
        }

        Text("Жанр")

        val listG = remember {
            mutableStateMapOf(
                *listOfGenres.mapIndexed { index, it -> if (index == 0) it to true else it to false }
                    .toTypedArray()
            )
        }


        FlowRow() {
            listOfGenres.forEachIndexed { index, it ->

                FilterChip(
                    modifier = Modifier.padding(5.dp),
                    onClick = {

                        if (index != 0) {
                            listG[it] = !listG[it]!!

                            if(!listG[it]!!){
                                checkListGenresOnEmpty(listG)
                            }else listG[listOfGenres[0]] = false

                        }else{
                            uncheckListGenres(listG)
                        }
                        checkListGenresOnFull(listG)
                    },
                    label = {
                        Text(it)
                    },
                    selected = listG[it]!!,

                    )
            }
        }
    }
}

private fun checkListGenresOnEmpty(list: MutableMap<String, Boolean>){
    if(!list.any { it.value }){
        uncheckListGenres(list)
    }
}

private fun checkListGenresOnFull(list: MutableMap<String, Boolean>) {
    if (list.filterValues { it }.size == list.size - 1) {
        uncheckListGenres(list)
    }
}

private fun uncheckListGenres(list: MutableMap<String, Boolean>){
    listOfGenres.forEachIndexed { index, _ ->
        list[listOfGenres[index]] = index == 0
    }
}

@Composable
fun PagerCardImage(
    posterUrl: String,
    pagesState: PagerState,
    index: Int,
    painter: Painter? = null
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
            }, elevation = CardDefaults.cardElevation(12.dp), shape = RoundedCornerShape(32.dp)
    ) {
        if (painter == null) {
            AsyncImage(
                model = if (index == 1) R.drawable.question_sign_icon_icons_com_73445 else posterUrl,
                contentDescription = null,
                contentScale = if (index == 1) ContentScale.Fit else ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()

            )
        } else {

            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

    }

}


