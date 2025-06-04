package com.example.navwithapinothing_2.ui.screen.MoviesListScreen


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.R
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.models.collection.CollectionMovie
import com.example.navwithapinothing_2.ui.screen.MovieScreen.shimmerEffect
import com.example.navwithapinothing_2.ui.screen.MovieViewModel
import com.example.navwithapinothing_2.ui.screen.PersonScreen.ShimmerMovies
import com.example.navwithapinothing_2.ui.screen.slider.listOfPoster
import com.example.navwithapinothing_2.ui.theme.Purple40
import com.example.navwithapinothing_2.ui.theme.Purple80
import com.example.navwithapinothing_2.ui.theme.poppinsFort
import kotlin.math.absoluteValue

@Composable
@Preview
fun ListScreen(
    modifier: Modifier = Modifier,
    movieViewModel: MovieViewModel = hiltViewModel(),
    toRandomScreen: () -> Unit,
    onSelectMovie: (Long) -> Unit,
    onSelectListMovies: (String, String) -> Unit,
    toErrorScreen: () -> Unit,
    toCollectionScreen: () -> Unit
) {
    val state = movieViewModel.state.collectAsState()
    val articles = movieViewModel.getAllPaging().collectAsLazyPagingItems()
    val collection = movieViewModel.state_collection.collectAsState()
    val movie_ = movieViewModel.state_movies_collection.collectAsState()
    val movie_list_ = movieViewModel.state_movie_home.collectAsState()

    println("movie = " + movie_.value)


    //LoadingScreen(modifier = modifier)

    var isVisibleTop by remember {
        mutableStateOf(movie_.value is Result.Loading)
    }

    var isVisibleList by remember {
        mutableStateOf(movie_list_.value is Result.Loading)
    }
    //val nav = rememberNavController()
    //val navController1 = rememberNavController()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        item {

            val configuration = LocalConfiguration.current
            val height = (configuration.screenWidthDp - 90) * 1.5

            val heightBox = height + 30

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(heightBox.dp)
            ) {

                AnimatedVisibility(
                    modifier = Modifier.fillMaxSize(),
                    visible = isVisibleTop,
                    enter = fadeIn(animationSpec = tween(200))
                ) {
                    ShimmerTop()
                }

                when (val data = movie_.value) {


                    Result.Loading -> {

                    }

                    is Result.Success<*> -> {

                        println("visible = " + movie_.value)
                        AnimatedVisibility(
                            modifier = Modifier.fillMaxSize(),
                            visible = !isVisibleTop,
                            enter = fadeIn(animationSpec = tween(200))
                        ) {

                            Column {

                                val pagerState =
                                    rememberPagerState(
                                        initialPage = 0,
                                        pageCount = { listOfPoster.size })


                                Text(
                                    "Топ ожидаемых фильмов",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .padding(start = 20.dp)
                                        .padding(bottom = 12.dp)
                                )

                                HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(height.dp),
                                    contentPadding = PaddingValues(end = 89.dp, start = 16.dp)
                                ) { page ->
                                    InitPagerCard(
                                        index = page,
                                        pagerState = pagerState,
                                        item = (data.data as List<MovieDTO>)[page],
                                        onSelectMovie = onSelectMovie
                                    )
                                }
                            }

                        }

                        isVisibleTop = false

                    }

                    is Result.Error<*> -> {
                        toErrorScreen()
                    }
                }


            }



            AnimatedVisibility(
                visible = isVisibleList,
                exit = fadeOut(animationSpec = tween(200))
            ) {
                Column {
                    ShimmerMovies(visible = false)
                    ShimmerMovies(visible = false)
                    ShimmerMovies(visible = false)
                    ShimmerMovies(visible = false)

                }
            }





            when (val data = movie_list_.value) {
                is Result.Error<*> -> {

                }

                is Result.Loading -> {

                }

                is Result.Success<*> -> {

                    val d = (data.data as Map<*, *>)

                    d.forEach { result ->

                        println("d = " + d.toString())

                        when (val collection = result.value as Result) {

                            Result.Loading -> {

                            }

                            is Result.Success<*> -> {

                                println("collection = " + collection.toString())




                                InitRow(
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

                                    items((collection.data as List<MovieDTO>).size) { index ->

                                        MovieCard(
                                            item = (collection.data as List<MovieDTO>)[index]!!,
                                            index = index,
                                            onSelectMovie = onSelectMovie
                                        )

                                    }
                                }

                            }

                            is Result.Error<*> -> {

                            }
                        }


                    }


                    isVisibleList = false
                }
            }
            InitCollections(
                state = collection,
                toCollectionScreen = toCollectionScreen,
                onSelectCollection = onSelectListMovies
            )


        }
    }

    movieViewModel.getCollections()

    //movieViewModel.getMoviesByCollectionTopBanned("planned-to-watch-films")

    //movieViewModel.getMoviesByCollection()

    /*LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .padding(20.dp)
                        .size(50.dp)
                        .align(Alignment.CenterEnd)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .clickable { toRandomScreen() }) {
                    Icon(
                        painter = painterResource(R.drawable.icn_cards), contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center), tint = Color.Blue
                    )
                }
            }


            val configuration = LocalConfiguration.current
            val height = (configuration.screenWidthDp - 90) * 1.5

            val pagerState = rememberPagerState(initialPage = 0, pageCount = { listOfPoster.size })

            when (val data = movie_.value) {
                Result.Error -> {

                }

                Result.Loading -> {

                }

                is Result.Success<*> -> {

                    Text(
                        "Топ ожидаемых фильмов",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .padding(bottom = 12.dp)
                    )

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height.dp),
                        contentPadding = PaddingValues(end = 89.dp, start = 16.dp)
                    ) { page ->
                        InitPagerCard(
                            index = page,
                            pagerState = pagerState,
                            item = (data.data as Response<MovieDTO>).docs[page]
                        )
                    }
                }
            }


            *//*Text(
                "Рекомендации",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .padding(vertical = 10.dp)
            )

            LazyRow(
                modifier = Modifier.padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {

                items(articles.itemCount) { index ->

                    MovieCard(
                        item = articles[index]!!,
                        index = index,
                        onSelectMovie = onSelectMovie
                    )

                }
            }*//*

            InitCollections(state = collection)

        }
    }*/
}

@Composable
fun ShimmerTop(modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val height = (configuration.screenWidthDp - 90) * 1.5

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    Column {
        Box(
            modifier = Modifier
                .padding(start = 16.dp, bottom = 14.dp)
                .width(200.dp)
                .height(22.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect()
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp),
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
@Preview
fun LoadingScreen(modifier: Modifier = Modifier) {

}

@Composable
@Preview
fun InitCollections(
    modifier: Modifier = Modifier,
    state: State<Result>,
    toCollectionScreen: () -> Unit,
    onSelectCollection: (String, String) -> Unit
) {
    when (val data = state.value) {


        Result.Loading -> {

        }

        is Result.Success<*> -> {
            ShowCollection(
                list = data.data as List<CollectionMovie>,
                toCollectionScreen = toCollectionScreen,
                onSelectCollection = onSelectCollection
            )
        }

        is Result.Error<*> -> {

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
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
        )



        Text(
            "Все❭",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable {
                onClick()
            }
        )
    }
}

@Composable
fun ShowCollection(
    modifier: Modifier = Modifier,
    list: List<CollectionMovie>,
    toCollectionScreen: () -> Unit,
    onSelectCollection: (String, String) -> Unit
) {


    InitRow(label = "Коллекции", onClick = toCollectionScreen)


    LazyRow(
        modifier = Modifier
            .padding(top = 10.dp)
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
fun ListMovies(modifier: Modifier = Modifier, list: List<MovieDTO>, onSelectMovie: (Long) -> Unit) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {

        itemsIndexed(list) { index, item ->

            MovieCard(item = item, index = index, onSelectMovie = onSelectMovie)

        }
    }

}


@Composable
fun InitPagerCard(
    modifier: Modifier = Modifier,
    index: Int,
    pagerState: PagerState,
    item: MovieDTO,
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
                model = item.poster?.url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()

            )
        }
        Column(modifier = Modifier.padding(start = 5.dp, top = 10.dp)) {
            Text(
                text = item.name ?: "null",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Color.Black
            )

            /* Text(
                 "Боевик, драма, фантастика",
                 fontSize = 14.sp,
                 color = Color.Black.copy(alpha = 0.5f)
             )*/
        }
    }
}

@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    item: MovieDTO,
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
                onSelectMovie(item.id!!)
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
                model = ImageRequest.Builder(LocalContext.current).data(item.poster?.previewUrl)

                    .listener(onStart = {
                        scale = ContentScale.Crop
                    }, onSuccess = { _, _ ->
                        isShimmer = false
                        scale = ContentScale.FillBounds
                    })
                    .crossfade(true).build(),
                contentScale = scale,
                contentDescription = null, error = painterResource(R.drawable.ic_placeholder_4)
            )
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = item.name ?: item.alternativeName ?: "null",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
        }

        item.rating?.kp?.let {
            if(it == 0.0) return

            Box(modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color = Purple40)) {
                Text(
                    String.format(java.util.Locale.ENGLISH,"%.1f", item.rating.kp), modifier = Modifier.padding(horizontal = 6.dp),  color = Color.White, fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFort,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
        }

    }
}

@Composable
fun MovieCardGrid(
    modifier: Modifier = Modifier,
    item: MovieDTO,
    index: Int,
    onSelectMovie: (Long) -> Unit
) {
    //println("name = " + item.name)

    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp / 2 - 22
    val height = (configuration.screenWidthDp / 2 - 22) * 1.5
    val boxHeight = height + 30


    var scale by remember { mutableStateOf(ContentScale.Crop) }
    var isShimmer by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            /*.then(
                if (index == 0) Modifier.padding(start = 16.dp)
                else Modifier
            )*/
            .fillMaxWidth()
            .height(boxHeight.dp)
            .width(width.dp)
            .clickable { onSelectMovie(item.id!!) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(width.dp)
                .height(height.dp), contentAlignment = Alignment.BottomEnd
        ) {
            AsyncImage(
                modifier = Modifier
                    .width(width.dp)
                    .height(height.dp) // 255
                    .clip(RoundedCornerShape(16.dp))
                    .then(
                        if (isShimmer) Modifier.shimmerEffect()
                        else Modifier
                    ),
                model = ImageRequest.Builder(LocalContext.current).data(item.poster?.previewUrl)
                    .listener(onStart = {
                        scale = ContentScale.Crop
                    }, onSuccess = { request, result ->
                        scale = ContentScale.FillBounds
                        isShimmer = false
                    })
                    .crossfade(true).build(),
                contentDescription = null,
                contentScale = scale, error = painterResource(R.drawable.ic_placeholder_4)
            )

            /*Image(
                painter = painterResource(R.drawable.ic_visibility_fill),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    Purple40
                )
            )*/
        }
        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = item.name ?: item.alternativeName ?: "null",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 16.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp
        )
    }
}

@Composable
fun MovieCardGridShimmer(
    modifier: Modifier = Modifier
) {
    //println("name = " + item.name)

    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp / 2 - 22
    val height = (configuration.screenWidthDp / 2 - 22) * 1.5
    val boxHeight = height + 30

    Column(
        modifier = Modifier
            /*.then(
                if (index == 0) Modifier.padding(start = 16.dp)
                else Modifier
            )*/
            .fillMaxWidth()
            .height(boxHeight.dp)
            .width(width.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(width.dp)
                .height(height.dp) // 255
                .clip(RoundedCornerShape(16.dp))
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .height(14.dp)
                .width(70.dp)
                .clip(
                    RoundedCornerShape(3.dp)
                )
                .shimmerEffect(),
        )
    }
}