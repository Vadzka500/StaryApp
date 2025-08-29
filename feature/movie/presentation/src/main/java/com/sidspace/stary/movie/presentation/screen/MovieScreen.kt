package com.sidspace.stary.movie.presentation.screen


import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sidspace.stary.movie.presentation.R
import com.sidspace.stary.movie.presentation.util.TimeManager
import com.sidspace.stary.ui.HorizontalList
import com.sidspace.stary.ui.ShowCollectionList
import com.sidspace.stary.ui.mapper.toMoviePreviewUi
import com.sidspace.stary.ui.model.MovieUi
import com.sidspace.stary.ui.model.PersonUi
import com.sidspace.stary.ui.model.ResultData
import com.sidspace.stary.ui.model.TrailerUi
import com.sidspace.stary.ui.shimmerEffect
import com.sidspace.stary.ui.uikit.Purple40
import com.sidspace.stary.ui.uikit.poppinsFort
import com.sidspace.stary.ui.utils.ScoreManager
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.PI
import kotlin.math.sin

private const val VIEWED_CYCLES_COUNT = 4
private const val VIEWED_AMPLITUDE = 35F
private const val VIEWED_CYCLE_DURATION = 300

@Composable
fun MovieScreen(
    id: Long,
    modifier: Modifier = Modifier,
    movieProfileViewModel: MovieProfileViewModel = hiltViewModel(),
    onSelectMovie: (Long) -> Unit,
    onSelectPerson: (Long) -> Unit,
    onClickReviews: (Long) -> Unit,
    toErrorScreen: () -> Unit,
) {

    val state = movieProfileViewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(id) {

        movieProfileViewModel.onIntent(MovieIntent.LoadMovie(id))

        movieProfileViewModel.effect.collectLatest { effect ->
            when (val data = effect) {
                is MovieEffect.ToMovieScreen -> {
                    onSelectMovie(data.id)
                }

                is MovieEffect.ToPersonScreen -> {
                    onSelectPerson(data.id)
                }

                is MovieEffect.ToReviewScreen -> {
                    onClickReviews(data.id)
                }

                is MovieEffect.PlayTrailer -> {
                    Intent(Intent.ACTION_VIEW).also {
                        it.data = data.url.toUri()
                        context.startActivity(it)
                    }
                }

                MovieEffect.ToErrorScreen -> {
                    toErrorScreen()
                }
            }
        }

    }


    AnimatedContent(
        targetState = state.value.movie, transitionSpec = { fadeIn() togetherWith fadeOut() }) { result ->

        when (result) {


            ResultData.Error -> {
                movieProfileViewModel.onIntent(MovieIntent.OnError)
            }

            ResultData.Loading -> {
                ShimmerScreen(modifier = modifier)
            }

            ResultData.None -> {

            }

            is ResultData.Success -> {
                InitMovie(
                    state = state, modifier = modifier
                )
            }
        }
    }

}

@Composable
fun ShimmerScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp), contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .width(160.dp)
                    .height(240.dp)
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .shimmerEffect()
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(20.dp)
                .align(Alignment.CenterHorizontally)
                .clip(
                    RoundedCornerShape(6.dp)
                )
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(20.dp)
                .align(Alignment.CenterHorizontally)
                .clip(
                    RoundedCornerShape(6.dp)
                )
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(10.dp))


        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(20.dp)
                .align(Alignment.CenterHorizontally)
                .clip(
                    RoundedCornerShape(6.dp)
                )
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .height(20.dp)
                .align(Alignment.CenterHorizontally)
                .clip(
                    RoundedCornerShape(6.dp)
                )
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(15.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = 16.dp)
                .clip(
                    RoundedCornerShape(6.dp)
                )
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .padding(horizontal = 16.dp)
                .clip(
                    RoundedCornerShape(6.dp)
                )
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 16.dp)
                .clip(
                    RoundedCornerShape(6.dp)
                )
                .shimmerEffect()

        )

    }
}

@Composable
fun InitMovie(
    state: State<MovieState>,
    modifier: Modifier = Modifier,
    movieProfileViewModel: MovieProfileViewModel = hiltViewModel()
) {

    var scale by remember { mutableStateOf(ContentScale.Crop) }
    val movie = (state.value.movie as ResultData.Success).data

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp), contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(movie.backdrop).crossfade(true).build(),
                contentDescription = "",

                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .blur(radiusX = 3.dp, radiusY = 3.dp)
                    .graphicsLayer { alpha = 0.99f }
                    .drawWithContent {
                        val colors = listOf(
                            Color.Black, Color.Transparent
                        )
                        drawContent()
                        drawRect(
                            brush = Brush.verticalGradient(colors), blendMode = BlendMode.DstIn
                        )
                    },
                contentScale = ContentScale.Crop,
                alpha = 0.8f
            )

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(movie.previewUrl).listener(
                    onSuccess = { _, _ ->
                        scale = ContentScale.FillBounds
                    }).crossfade(true).build(),
                contentDescription = "",
                modifier = Modifier
                    .padding(top = 40.dp)
                    .width(160.dp)
                    .height(240.dp)
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .shimmerEffect(),
                contentScale = scale,
                error = painterResource(com.sidspace.stary.ui.R.drawable.ic_placeholder_4)
            )
        }

        Text(
            text = movie.name ?: movie.enName ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 24.sp
        )

        RowPrimaryData(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp), movie = movie
        )

        RowGenres(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 5.dp), movie = movie
        )

        RowScore(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 5.dp), movie = movie
        )

        Spacer(modifier = Modifier.height(15.dp))

        RowButtons(movie = movie, state = state)

        if (movie.description != null) {
            Spacer(modifier = Modifier.height(16.dp))
            RowDescription(movie = movie)
        }

        if (movie.persons != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.actors),
                modifier = Modifier.padding(start = 16.dp),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(5.dp))


            RowActors(
                modifier = Modifier,
                persons = movie.persons!!.filter { (it.profession?.contains("актеры"))?.and(it.name != null) == true })

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.creators),
                modifier = Modifier.padding(start = 16.dp),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(5.dp))


            RowCreators(
                modifier = Modifier, persons = movie.persons!!.filter {
                    (it.profession?.contains("актеры") == false && !it.profession!!.contains("актеры дубляжа")).and(
                        it.name != null
                    )
                }.sortedWith(
                    compareByDescending {
                        it.profession?.contains(
                            "режиссеры"
                        )
                    })
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(50.dp)
                .clickable { movieProfileViewModel.onIntent(MovieIntent.ToReviewScreen(movie.id!!)) }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.review),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = stringResource(R.string.show_review),
                modifier = Modifier.scale(0.8f)
            )
        }

        if (!movie.sequelsAndPrequels.isNullOrEmpty()) {

            HorizontalList(
                movie.sequelsAndPrequels!!.map { it.toMoviePreviewUi() },
                stringResource(R.string.related_movies),
                onSelectMovie = {
                    movieProfileViewModel.onIntent(MovieIntent.ToMovieScreen(it))
                })

        }

        if (!movie.similarMovies.isNullOrEmpty()) {

            HorizontalList(movie.similarMovies!!.map { it.toMoviePreviewUi() }, "Похожие фильмы", onSelectMovie = {
                movieProfileViewModel.onIntent(MovieIntent.ToMovieScreen(it))
            })

        }
    }


}


@Composable
fun RowDescription(modifier: Modifier = Modifier, movie: MovieUi) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {

        Text(
            text = stringResource(R.string.description),
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        var rowCount by remember {
            mutableIntStateOf(2)
        }
        Column(modifier = Modifier.animateContentSize()) {
            Text(
                text = movie.description!!,
                overflow = TextOverflow.Ellipsis,
                maxLines = rowCount,
                fontWeight = FontWeight.Medium,
                fontFamily = poppinsFort,
                fontSize = 14.sp,
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = if (rowCount == 2) "Развернуть" else "Свернуть",
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.clickable(indication = null, interactionSource = null) {
                rowCount = if (rowCount == 2) Int.MAX_VALUE else 2
            })
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailersBottomSheet(
    listOfTrailers: List<TrailerUi>, movieProfileViewModel: MovieProfileViewModel = hiltViewModel()
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    ModalBottomSheet(
        sheetState = sheetState, onDismissRequest = {
            movieProfileViewModel.onIntent(MovieIntent.HideTrailerSheet)
        }) {
        TrailersList(listOfTrailers = listOfTrailers)

    }

}

@Composable
fun TrailersList(modifier: Modifier = Modifier, listOfTrailers: List<TrailerUi>) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {

        items(listOfTrailers) { item ->
            TrailerItem(trailer = item)
        }

    }
}

fun openCustomTab(context: Context, url: String) {
    val customTabsIntent = CustomTabsIntent.Builder().setShowTitle(true).build()

    customTabsIntent.launchUrl(context, url.toUri())
}

@Composable
fun TrailerItem(modifier: Modifier = Modifier, trailer: TrailerUi) {
    val context = LocalContext.current
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable {
            openCustomTab(context, trailer.url!!)
        }
        .height(64.dp)
        .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(R.drawable.img_youtube_2),
            contentDescription = "Youtube",
            modifier = Modifier.size(48.dp)
        )

        Text(
            text = trailer.name ?: "Трейлер",
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontFamily = poppinsFort,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 16.dp)
        )

    }
}



@Composable
fun RowButtons(
    movie: MovieUi,
    state: State<MovieState>,
    movieProfileViewModel: MovieProfileViewModel = hiltViewModel()
) {


    var isAminVisible by remember { mutableStateOf(false) }

    val t = remember { Animatable(0f) }
    val totalCycles = VIEWED_CYCLES_COUNT
    val amplitude = VIEWED_AMPLITUDE
    val cycleDuration = VIEWED_CYCLE_DURATION

    val rotation = remember(t.value) {
        val maxT = totalCycles * 2 * PI
        val damping = 1f - (t.value / maxT.toFloat()).coerceIn(0f, 1f)
        amplitude * damping * sin(t.value)
    }


    LaunchedEffect(isAminVisible) {

        if (isAminVisible) {
            t.snapTo(0f)
            t.animateTo(
                targetValue = (totalCycles * 2 * PI).toFloat(),
                animationSpec = tween(durationMillis = totalCycles * cycleDuration)
            )
        }

    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.padding(horizontal = 6.dp)
    ) {
        if (!movie.trailers.isNullOrEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .clickable {

                            if (movie.trailers!!.size == 1) {
                                movieProfileViewModel.onIntent(MovieIntent.PlayTrailer(movie.trailers!![0].url!!))

                            } else movieProfileViewModel.onIntent(MovieIntent.ShowTrailerSheet)


                        }
                        .padding(0.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.cameravideo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp)
                            .align(Alignment.Center),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Text(
                    text = stringResource(R.string.trailer),
                    fontWeight = FontWeight.Normal,
                    fontFamily = poppinsFort,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        val colorMaterial = MaterialTheme.colorScheme.onSecondaryContainer

        var color by remember {
            mutableStateOf(colorMaterial)
        }

        var colorBookmark by remember {
            mutableStateOf(colorMaterial)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clickable {
                        if (state.value.isExistMovieDb?.isViewed == true) {
                            movieProfileViewModel.onIntent(
                                MovieIntent.ViewedToMovie(
                                    id = movie.id, collections = movie.collections, isViewed = false
                                )
                            )

                            isAminVisible = false
                            color = colorMaterial
                        } else {
                            movieProfileViewModel.onIntent(
                                MovieIntent.ViewedToMovie(
                                    id = movie.id, collections = movie.collections, isViewed = true
                                )
                            )
                            isAminVisible = true
                            color = Purple40
                        }
                    }
                    .padding(0.dp)
            ) {
                Icon(
                    painter = if (state.value.isExistMovieDb?.isViewed == true) {
                        color = Purple40
                        painterResource(R.drawable.ic_visibility_fill)
                    } else {
                        color = colorMaterial
                        painterResource(
                            R.drawable.ic_visibility_outlined

                        )
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp)
                        .align(Alignment.Center)
                        .graphicsLayer {
                            rotationZ = rotation
                        },
                    tint = color
                )
            }
            Text(
                text = stringResource(R.string.viewed),
                fontWeight = FontWeight.Normal,
                fontFamily = poppinsFort,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clickable {

                        if (state.value.isExistMovieDb?.isBookmark == true) {

                            movieProfileViewModel.onIntent(
                                MovieIntent.BookmarkToMovie(
                                    id = movie.id!!, collections = movie.collections, isBookmark = false
                                )
                            )
                            colorBookmark = colorMaterial
                        } else {
                            movieProfileViewModel.onIntent(
                                MovieIntent.BookmarkToMovie(
                                    id = movie.id!!, collections = movie.collections, isBookmark = true
                                )
                            )
                            colorBookmark = Purple40
                        }
                    }
                    .padding(0.dp)
            ) {
                Icon(
                    painter = if (state.value.isExistMovieDb?.isBookmark == true) {
                        colorBookmark = Purple40
                        painterResource(
                            R.drawable.ic_bookmark_added_fill
                        )
                    } else {
                        colorBookmark = colorMaterial
                        painterResource(R.drawable.ic_bookmark_add_outlined)
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp)
                        .align(Alignment.Center),
                    tint = colorBookmark
                )
            }
            Text(
                text = stringResource(R.string.in_bookmark),
                fontWeight = FontWeight.Normal,
                fontFamily = poppinsFort,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clickable {
                        movieProfileViewModel.onIntent(MovieIntent.ShowFoldersSheet)
                    }
                    .padding(0.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_folder_add_outlined),
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp)
                        .align(Alignment.Center),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Text(
                text = stringResource(R.string.in_collection),
                fontWeight = FontWeight.Normal,
                fontFamily = poppinsFort,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }


    }

    if (state.value.isShowTrailerSheet) {
        TrailersBottomSheet(
            listOfTrailers = movie.trailers!!
        )
    }

    if (state.value.isShowSheetFolders) {
        CollectionBottomSheet(
            movie = movie
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionBottomSheet(
    movie: MovieUi, modifier: Modifier = Modifier, movieProfileViewModel: MovieProfileViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    val state = movieProfileViewModel.state.collectAsState()


    ModalBottomSheet(
        sheetState = sheetState, onDismissRequest = {
            movieProfileViewModel.onIntent(MovieIntent.HideFoldersSheet)
        }) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp / 2)
        ) {

            ShowCollectionList(
                list = state.value.filters, onSelectFolder = { idFolder ->
                    movieProfileViewModel.onIntent(
                        MovieIntent.OnSelectFolder(
                            idFolder, movie
                        )
                    )
                }, onError = {
                    movieProfileViewModel.onIntent(MovieIntent.OnError)
                }, movieId = movie.id
            )

        }


    }
}


@Composable
fun RowActors(
    persons: List<PersonUi>,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(persons) {
            CardActor(
                person = it,
                modifier = modifier.width(70.dp)
            )
        }
    }
}

@Composable
fun CardActor(
    person: PersonUi, modifier: Modifier = Modifier, movieProfileViewModel: MovieProfileViewModel = hiltViewModel()
) {

    Column(modifier = modifier.clickable {
        movieProfileViewModel.onIntent(
            MovieIntent.ToPersonScreen(
                person.id
            )
        )
    }) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(person.photo).crossfade(true).build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(70.dp)
                .clip(
                    RoundedCornerShape(14.dp)
                )
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 13.sp,
            text = person.name!!.replace(" ", "\n"),
            fontWeight = FontWeight.Medium,
            fontFamily = poppinsFort,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun RowCreators(
    persons: List<PersonUi>,
    modifier: Modifier = Modifier,

    ) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(15.dp), contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(persons) {
            CardCreator(
                person = it,
                modifier = modifier.width(70.dp)
            )
        }
    }
}

@Composable
fun CardCreator(
    person: PersonUi, modifier: Modifier = Modifier, movieProfileViewModel: MovieProfileViewModel = hiltViewModel()
) {

    Column(modifier = modifier.clickable {
        movieProfileViewModel.onIntent(
            MovieIntent.ToPersonScreen(
                person.id
            )
        )
    }) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(person.photo).crossfade(true).build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(70.dp)
                .clip(
                    RoundedCornerShape(14.dp)
                )
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 13.sp,
            text = person.name!!.replace(" ", "\n"),
            fontWeight = FontWeight.Medium,
            fontFamily = poppinsFort,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            fontSize = 12.sp,
            textAlign = TextAlign.Start
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 13.sp,
            text = person.profession?.firstOrNull()?.dropLast(1)?.replaceFirstChar { it.uppercase() } ?: "",
            fontWeight = FontWeight.Light,
            fontFamily = poppinsFort,
            fontSize = 11.sp,
            textAlign = TextAlign.Start)
    }

}

@Composable
fun RowScore(
    movie: MovieUi, modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        if (movie.scoreImdb != null && movie.scoreImdb != 0.0) {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Image(
                    painter = painterResource(R.drawable.ic_imdb_2),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = ScoreManager.ratingToFormat(movie.scoreImdb!!).toString(),
                    fontWeight = FontWeight.Normal,
                    fontFamily = poppinsFort,
                    fontSize = 14.sp
                )
            }
        }
        if (movie.scoreKp != null && movie.scoreKp != 0.0) {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Image(
                    painter = painterResource(R.drawable.ic_kinopoisk_2),
                    contentDescription = null,
                    modifier = Modifier
                        .size(14.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = ScoreManager.ratingToFormat(movie.scoreKp!!).toString(),
                    fontWeight = FontWeight.Normal,
                    fontFamily = poppinsFort,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun RowGenres(movie: MovieUi, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        movie.countries?.first().let {
            Text(
                text = it!!, fontWeight = FontWeight.Normal, fontFamily = poppinsFort, fontSize = 14.sp
            )
        }


        movie.genres?.take(2)?.forEachIndexed { index, item ->
            Text(text = "•", color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            Text(
                text = item.replaceFirstChar { it.uppercase() },
                fontWeight = FontWeight.Normal,
                fontFamily = poppinsFort,
                fontSize = 14.sp
            )
        }

    }
}

@Composable
fun RowPrimaryData(movie: MovieUi, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        if (!movie.isSeries) {
            Text(
                text = movie.year.toString(),
                fontWeight = FontWeight.Medium,
                fontFamily = poppinsFort,
                fontSize = 14.sp
            )

            if (movie.movieLength != null) {

                Text(text = "•", color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))

                Text(
                    text = TimeManager.getTimeByMinutes(movie.movieLength!!),
                    fontWeight = FontWeight.Medium,
                    fontFamily = poppinsFort,
                    fontSize = 14.sp
                )
            }
        } else {

            val seasonCount = movie.countOfSeasons


            if (movie.releaseStart != null) {
                val releaseStart = movie.releaseStart
                val releaseEnd = movie.releaseEnd

                var text = releaseStart.toString()

                if (releaseStart != null && releaseEnd != null && releaseStart != releaseEnd) {
                    text = "$releaseStart - $releaseEnd"
                } else if (releaseStart != null && releaseEnd == null) {
                    text = if (seasonCount == 1 || movie.status?.equals("completed") == true) {
                        releaseStart.toString()
                    } else {
                        "$releaseStart - н.в."
                    }
                }

                Text(
                    text = text, fontWeight = FontWeight.Medium, fontFamily = poppinsFort, fontSize = 14.sp
                )
            } else {
                Text(
                    text = movie.year.toString(),
                    fontWeight = FontWeight.Medium,
                    fontFamily = poppinsFort,
                    fontSize = 14.sp
                )
            }

            Text(text = "•", color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))

            Text(
                text = seasonCount.let { if (it == 1) "$it сезон" else if (it!! < 5) "$it сезона" else "$it сезонов" },
                fontWeight = FontWeight.Medium,
                fontFamily = poppinsFort,
                fontSize = 14.sp
            )

        }

        if (movie.ageRating != null) {

            Text(text = "•", color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))

            Text(
                text = "${movie.ageRating}+", fontWeight = FontWeight.Medium, fontFamily = poppinsFort, fontSize = 14.sp
            )
        }
    }
}
