package com.example.navwithapinothing_2.features.screen.MovieScreen

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.Animatable
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviesapi.models.movie.MovieDTO
import com.example.moviesapi.models.movie.Trailer

import com.example.navwithapinothing_2.R
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.database.models.MovieDb
import com.example.navwithapinothing_2.models.movie.PersonOfMovie
import com.example.navwithapinothing_2.features.screen.MoviesListScreen.ListMovies
import com.example.navwithapinothing_2.features.screen.MovieViewModel
import com.example.navwithapinothing_2.features.theme.Purple40
import com.example.navwithapinothing_2.features.theme.ShimmerColorShades
import com.example.navwithapinothing_2.features.theme.poppinsFort
import com.example.navwithapinothing_2.utils.ScoreManager
import com.example.navwithapinothing_2.utils.TimeManager
import kotlin.math.PI
import kotlin.math.sin
import androidx.core.net.toUri
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.FolderWithMovies
import com.example.navwithapinothing_2.features.screen.FoldersScreen.FoldersViewModel
import com.example.navwithapinothing_2.features.screen.FoldersScreen.ResultFilterData
import com.example.navwithapinothing_2.features.screen.FoldersScreen.ShowCollectionList
import com.example.navwithapinothing_2.features.screen.shimmerEffect
import kotlinx.coroutines.flow.collectLatest

/**
 * @Author: Vadim
 * @Date: 18.12.2024
 */


@Composable
fun MovieScreen(
    modifier: Modifier = Modifier,
    id: Long,
    movieProfileViewModel: MovieProfileViewModel = hiltViewModel(),
    onSelectMovie: (Long) -> Unit,
    onSelectPerson: (Long) -> Unit,
    onClickReviews: (Long) -> Unit
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
            }
        }

    }


    AnimatedContent(
        targetState = state.value.movie,
        transitionSpec = { fadeIn() togetherWith fadeOut() }) { result ->

        when (result) {
            is ResultMovie.Error -> {

            }

            ResultMovie.Loading -> {
                ShimmerScreen(modifier = modifier)
            }

            is ResultMovie.Success -> {
                InitMovie(
                    state = state,
                    modifier = modifier
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
        /*


         RowButtons(movie = movie)

         if (movie.description != null) {
             RowDescription(movie = movie)
         }

         if (movie.persons != null) {
             Spacer(modifier = Modifier.height(16.dp))
             Text(
                 text = "Актеры",
                 modifier = Modifier.padding(start = 16.dp),
                 fontWeight = FontWeight.SemiBold,
                 fontFamily = poppinsFort,
                 fontSize = 18.sp
             )
             Spacer(modifier = Modifier.height(5.dp))
             RowActors(
                 modifier = Modifier,
                 persons = movie.persons.filter { it.profession.equals("актеры") })
         }

         if (!movie.sequelsAndPrequels.isNullOrEmpty()) {
             Spacer(modifier = Modifier.height(16.dp))
             Text(
                 text = "Связанные фильмы",
                 modifier = Modifier.padding(start = 16.dp),
                 fontWeight = FontWeight.SemiBold,
                 fontFamily = poppinsFort,
                 fontSize = 18.sp
             )
             Spacer(modifier = Modifier.height(5.dp))
             ListMovies(list = movie.sequelsAndPrequels, onSelectMovie = onSelectMovie)
         }

         if (!movie.similarMovies.isNullOrEmpty()) {
             Spacer(modifier = Modifier.height(15.dp))
             Text(
                 text = "Похожие фильмы",
                 modifier = Modifier.padding(start = 16.dp),
                 fontWeight = FontWeight.SemiBold,
                 fontFamily = poppinsFort,
                 fontSize = 18.sp
             )
             Spacer(modifier = Modifier.height(5.dp))
             ListMovies(list = movie.similarMovies, onSelectMovie = onSelectMovie)
         }*/

    }
}

@Composable
fun InitMovie(
    state: State<MovieState>,
    modifier: Modifier = Modifier,
    movieProfileViewModel: MovieProfileViewModel = hiltViewModel()
) {

    var scale by remember { mutableStateOf(ContentScale.Crop) }
    val movie = (state.value.movie as ResultMovie.Success).movie

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
                model = ImageRequest.Builder(LocalContext.current).data(movie.backdrop?.url)
                    .crossfade(true).build(),
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
            //  Column(modifier = Modifier.fillMaxWidth().padding(top = 20.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.poster?.previewUrl)
                    .listener(
                        onSuccess = { _, _ ->
                            scale = ContentScale.FillBounds
                        }
                    ).crossfade(true).build(),
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
                error = painterResource(R.drawable.ic_placeholder_4)
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
                .padding(top = 8.dp),
            movie = movie
        )

        RowGenres(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 5.dp),
            movie = movie
        )

        RowScore(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 5.dp),
            movie = movie
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
                text = "Актеры",
                modifier = Modifier.padding(start = 16.dp),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(5.dp))


            RowActors(
                modifier = Modifier,
                persons = movie.persons.filter { (it.profession == "актеры").and(it.name != null) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Создатели",
                modifier = Modifier.padding(start = 16.dp),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(5.dp))


            RowCreators(
                modifier = Modifier,
                persons = movie.persons.filter {
                    (it.profession != "актеры" && it.profession != "актеры дубляжа").and(
                        it.name != null
                    )
                }.sortedWith(
                    compareByDescending<PersonOfMovie> {
                        it.profession.equals(
                            "режиссеры",
                            true
                        )
                    }
                        .thenBy { it.profession }
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(50.dp)
                .clickable { movieProfileViewModel.onIntent(MovieIntent.ToReviewScreen(movie.id!!)) }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Рецензии",
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Show reviews",
                modifier = Modifier.scale(0.8f)
            )
        }

        if (!movie.sequelsAndPrequels.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Связанные фильмы",
                modifier = Modifier.padding(start = 16.dp),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(5.dp))
            ListMovies(
                list = movie.sequelsAndPrequels,
                onSelectMovie = { id -> movieProfileViewModel.onIntent(MovieIntent.ToMovieScreen(id)) },
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        if (!movie.similarMovies.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Похожие фильмы",
                modifier = Modifier.padding(start = 16.dp),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(5.dp))
            ListMovies(
                list = movie.similarMovies,
                onSelectMovie = { id -> movieProfileViewModel.onIntent(MovieIntent.ToMovieScreen(id)) },
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }


}


@Composable
fun RowDescription(modifier: Modifier = Modifier, movie: MovieDTO) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {

        Text(
            text = "Описание", fontWeight = FontWeight.SemiBold,
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
    modifier: Modifier = Modifier,
    listOfTrailers: List<Trailer>,
    movieProfileViewModel: MovieProfileViewModel = hiltViewModel()
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
           movieProfileViewModel.onIntent(MovieIntent.HideTrailerSheet)
        }) {
        TrailersList(listOfTrailers = listOfTrailers)

    }

}

@Composable
fun TrailersList(modifier: Modifier = Modifier, listOfTrailers: List<Trailer>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {

        items(listOfTrailers) { item ->
            TrailerItem(trailer = item)
        }

    }
}

fun openCustomTab(context: Context, url: String) {
    val customTabsIntent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .build()

    customTabsIntent.launchUrl(context, url.toUri())
}

@Composable
fun TrailerItem(modifier: Modifier = Modifier, trailer: Trailer) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
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
            text = trailer.name ?: "Трейлер", fontWeight = FontWeight.Medium,
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
    modifier: Modifier = Modifier,
    movie: MovieDTO,
    state: State<MovieState>,
    movieProfileViewModel: MovieProfileViewModel = hiltViewModel()
) {


    var isAminVisible by remember { mutableStateOf(false) }

    val t = remember { Animatable(0f) }
    val totalCycles = 4
    val amplitude = 35f
    val cycleDuration = 300

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
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.padding(horizontal = 6.dp)
    ) {
        //Spacer(modifier = Modifier.width(10.dp))
        if (!movie.videos?.trailers.isNullOrEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .clickable {

                            if (movie.videos.trailers.size == 1) {
                                movieProfileViewModel.onIntent(MovieIntent.PlayTrailer(movie.videos.trailers[0].url!!))

                            } else
                                movieProfileViewModel.onIntent(MovieIntent.ShowTrailerSheet)


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
                    text = "Трейлер", fontWeight = FontWeight.Normal,
                    fontFamily = poppinsFort,
                    fontSize = 12.sp, textAlign = TextAlign.Center
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
                                    id = movie.id!!,
                                    collections = movie.lists,
                                    isViewed = false
                                )
                            )

                            isAminVisible = false
                            color = colorMaterial
                        } else {
                            movieProfileViewModel.onIntent(
                                MovieIntent.ViewedToMovie(
                                    id = movie.id!!,
                                    collections = movie.lists,
                                    isViewed = true
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
                            //if (movieDbState.value?.isViewed == true){
                            rotationZ = rotation
                            //}
                        },
                    tint = color
                )
            }
            Text(
                text = "Просмотрен", fontWeight = FontWeight.Normal,
                fontFamily = poppinsFort,
                fontSize = 12.sp, textAlign = TextAlign.Center
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clickable { //bookmark = !bookmark

                        if (state.value.isExistMovieDb?.isBookmark == true) {

                            movieProfileViewModel.onIntent(
                                MovieIntent.BookmarkToMovie(
                                    id = movie.id!!,
                                    collections = movie.lists,
                                    isBookmark = false
                                )
                            )
                            colorBookmark = colorMaterial
                        } else {
                            movieProfileViewModel.onIntent(
                                MovieIntent.BookmarkToMovie(
                                    id = movie.id!!,
                                    collections = movie.lists,
                                    isBookmark = true
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
                text = "В закладки", fontWeight = FontWeight.Normal,
                fontFamily = poppinsFort,
                fontSize = 12.sp, textAlign = TextAlign.Center
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
                text = "В коллекции", fontWeight = FontWeight.Normal,
                fontFamily = poppinsFort,
                fontSize = 12.sp, textAlign = TextAlign.Center
            )
        }


    }

    if (state.value.isShowTrailerSheet) {
        TrailersBottomSheet(
            listOfTrailers = movie.videos!!.trailers!!
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
    modifier: Modifier = Modifier,
    movie: MovieDTO,
    movieProfileViewModel: MovieProfileViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    val state = movieProfileViewModel.state.collectAsState()


    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            movieProfileViewModel.onIntent(MovieIntent.HideFoldersSheet)
        }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp / 2)
        ) {

            ShowCollectionList(
                list = state.value.filters, onSelectFolder = { idFolder ->
                   movieProfileViewModel.onIntent(MovieIntent.OnSelectFolder(idFolder, movie))
                }, movieId = movie.id!!
            )

        }


    }
}


@Composable
fun RowActors(
    modifier: Modifier = Modifier,
    persons: List<PersonOfMovie>
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(persons) {
            CardActor(
                person = it,
                modifier = Modifier.width(70.dp)
            )
        }
    }
}

@Composable
fun CardActor(
    modifier: Modifier = Modifier,
    person: PersonOfMovie,
    movieProfileViewModel: MovieProfileViewModel = hiltViewModel()
) {

    Column(modifier = modifier.clickable {
        movieProfileViewModel.onIntent(
            MovieIntent.ToPersonScreen(
                person.id
            )
        )
    }) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(person.photo).crossfade(true)
                .build(),
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
            text = person.name!!.replace(" ", "\n"), fontWeight = FontWeight.Medium,
            fontFamily = poppinsFort,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            fontSize = 12.sp, textAlign = TextAlign.Center
        )
    }

}

@Composable
fun RowCreators(
    modifier: Modifier = Modifier,
    persons: List<PersonOfMovie>
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(persons) {
            CardCreator(
                person = it,
                modifier = Modifier.width(70.dp)
            )
        }
    }
}

@Composable
fun CardCreator(
    modifier: Modifier = Modifier,
    person: PersonOfMovie,
    movieProfileViewModel: MovieProfileViewModel = hiltViewModel()
) {

    Column(modifier = modifier.clickable {
        movieProfileViewModel.onIntent(
            MovieIntent.ToPersonScreen(
                person.id
            )
        )
    }) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(person.photo).crossfade(true)
                .build(),
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
            text = person.name!!.replace(" ", "\n"), fontWeight = FontWeight.Medium,
            fontFamily = poppinsFort,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            fontSize = 12.sp, textAlign = TextAlign.Start
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 13.sp,
            text = person.profession!!.dropLast(1).replaceFirstChar { it.uppercase() },
            fontWeight = FontWeight.Light,
            fontFamily = poppinsFort,
            fontSize = 11.sp,
            textAlign = TextAlign.Start
        )
    }

}

@Composable
fun RowScore(modifier: Modifier = Modifier, movie: MovieDTO) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        if (movie.rating?.imdb != null && movie.rating.imdb != 0.0) {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Image(
                    painter = painterResource(R.drawable.ic_imdb_2),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = ScoreManager.ratingToFormat(movie.rating.imdb).toString(),
                    fontWeight = FontWeight.Normal,
                    fontFamily = poppinsFort,
                    fontSize = 14.sp
                )
            }
        }
        if (movie.rating?.kp != null && movie.rating.kp != 0.0) {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Image(
                    painter = painterResource(R.drawable.ic_kinopoisk_2),
                    contentDescription = null,
                    modifier = Modifier
                        .size(14.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = ScoreManager.ratingToFormat(movie.rating.kp).toString(),
                    fontWeight = FontWeight.Normal,
                    fontFamily = poppinsFort,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun RowGenres(modifier: Modifier = Modifier, movie: MovieDTO) {
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        movie.countries?.first().let {
            Text(
                text = it!!.name,
                fontWeight = FontWeight.Normal,
                fontFamily = poppinsFort,
                fontSize = 14.sp
            )
        }


        movie.genres?.take(2)?.forEachIndexed { index, item ->
            Text(text = "•", color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            Text(
                text = item.name!!.replaceFirstChar { it.uppercase() },
                fontWeight = FontWeight.Normal,
                fontFamily = poppinsFort,
                fontSize = 14.sp
            )
        }

    }
}

@Composable
fun RowPrimaryData(modifier: Modifier = Modifier, movie: MovieDTO) {
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        if (!movie.isSeries!!) {
            Text(
                text = movie.year.toString(),
                fontWeight = FontWeight.Medium,
                fontFamily = poppinsFort,
                fontSize = 14.sp
            )

            if (movie.movieLength != null) {
                //Text(text = "•", color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))

                Text(text = "•", color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))

                Text(
                    text = TimeManager.getTimeByMinutes(movie.movieLength),
                    fontWeight = FontWeight.Medium,
                    fontFamily = poppinsFort,
                    fontSize = 14.sp
                )
            }
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
                    text = if (seasonCount == 1 || movie.status?.equals("completed") == true) {
                        releaseStart.toString()
                    } else {
                        "$releaseStart - н.в."
                    }
                }

                Text(
                    text = text,
                    fontWeight = FontWeight.Medium,
                    fontFamily = poppinsFort,
                    fontSize = 14.sp
                )
            } else {
                Text(
                    text = movie.year.toString(),
                    fontWeight = FontWeight.Medium,
                    fontFamily = poppinsFort,
                    fontSize = 14.sp
                )
            }

            if (movie.seasonsInfo != null) {


                Text(text = "•", color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))

                Text(
                    text = seasonCount.let { if (it == 1) "$it сезон" else if (it!! < 5) "$it сезона" else "$it сезонов" },
                    fontWeight = FontWeight.Medium,
                    fontFamily = poppinsFort,
                    fontSize = 14.sp
                )
            }
        }



        if (movie.ageRating != null) {

            Text(text = "•", color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))

            Text(
                text = "${movie.ageRating}+",
                fontWeight = FontWeight.Medium,
                fontFamily = poppinsFort,
                fontSize = 14.sp
            )
        }
    }
}
