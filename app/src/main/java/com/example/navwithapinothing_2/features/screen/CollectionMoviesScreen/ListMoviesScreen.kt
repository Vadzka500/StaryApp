package com.example.navwithapinothing_2.features.screen.CollectionMoviesScreen

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

import androidx.compose.foundation.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.R
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.features.screen.FilterSection
import com.example.navwithapinothing_2.features.screen.MovieViewModel
import com.example.navwithapinothing_2.features.screen.MoviesListScreen.MovieCardGrid
import com.example.navwithapinothing_2.features.screen.MoviesListScreen.MovieCardGridShimmer
import com.example.navwithapinothing_2.features.screen.SearchScreen.SearchIntent

import com.example.navwithapinothing_2.features.screen.shimmerEffect
import com.example.navwithapinothing_2.features.theme.Purple40

import com.example.navwithapinothing_2.features.theme.poppinsFort
import com.example.navwithapinothing_2.models.common.ListMoviesResult
import com.example.navwithapinothing_2.models.common.SortDirection
import com.example.navwithapinothing_2.models.common.SortType
import com.example.navwithapinothing_2.models.common.ViewMode
import com.example.navwithapinothing_2.utils.ScoreManager
import kotlinx.coroutines.flow.collectLatest

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
    listMoviesViewModel: ListMoviesViewModel = hiltViewModel()
) {

    val state = listMoviesViewModel.state.collectAsState()

    LaunchedEffect(slug) {
        if (state.value.list == ListMoviesResult.Loading)
            listMoviesViewModel.getMoviesByCollection(slug, limit = 250)
    }


    LaunchedEffect(Unit) {
        listMoviesViewModel.effect.collectLatest { effect ->
            when (effect) {
                is ListMoviesEffect.OnBack -> {
                    onBack()
                }

                is ListMoviesEffect.OnSelectMovie -> {
                    onSelectMovie(effect.id)
                }
            }
        }
    }

    Box(modifier = modifier.padding(top = 5.dp)) {

        when (val data = state.value.list) {

            is ListMoviesResult.Error -> {

            }

            ListMoviesResult.Loading -> {
                LoadList(modifier = Modifier.padding(top = 54.dp))
            }

            is ListMoviesResult.Success -> {
                InitList(
                    modifier = Modifier.padding(top = 54.dp),
                    list = data.list,
                    onClick = { id ->
                        listMoviesViewModel.onIntent(ListMoviesIntent.OnSelectMovie(id))

                    },
                    viewType = state.value.viewMode
                )
            }
        }

        Box(
            modifier = Modifier
                .height(50.dp)
                .padding(horizontal = 12.dp)

        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
                    .align(Alignment.Center),
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
                        listMoviesViewModel.onIntent(ListMoviesIntent.OnBack)
                    }
                    .padding(8.dp)
                    .align(Alignment.CenterStart))

            Icon(
                painter = painterResource(R.drawable.settings),
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .clip(
                        CircleShape
                    )
                    .clickable {
                        listMoviesViewModel.onIntent(ListMoviesIntent.IsShowFilter(!state.value.isShowFilter))
                    }
                    .padding(8.dp)
                    .align(Alignment.CenterEnd))

        }


    }

    FilterSection(
        modifier = Modifier.padding(top = 105.dp),
        isVisibleFilter = state.value.isShowFilter,
        viewType = state.value.viewMode,
        true,
        onHideFilter = {
            listMoviesViewModel.onIntent(ListMoviesIntent.IsShowFilter(false))
        },
        setGridView = {
            listMoviesViewModel.onIntent(ListMoviesIntent.SetGridViewMode)
        },
        setListView = {
            listMoviesViewModel.onIntent(ListMoviesIntent.SetListViewMode)
        }, sortType = state.value.sortType, sortDirection = state.value.sortDirection
        , toggleSortDirection = {
            listMoviesViewModel.onIntent(ListMoviesIntent.ToggleSortDirection)
        }, setSortType = {
            listMoviesViewModel.onIntent(ListMoviesIntent.SetSortType(it))
        }, sortList = {
            listMoviesViewModel.onIntent(ListMoviesIntent.SortMovies)
        }
    )
}



@Composable
fun LoadList(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        modifier = modifier
            .padding(horizontal = 16.dp),
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
fun InitList(
    modifier: Modifier = Modifier,
    list: List<MovieDTO>,
    onClick: (Long) -> Unit,
    viewType: ViewMode
) {

    AnimatedContent(
        targetState = viewType,
        transitionSpec = { fadeIn() togetherWith ExitTransition.None }) { viewType ->

        if (viewType == ViewMode.GRID) {
            GridView(modifier, list = list, onClick = onClick)
        } else {
            ListView(modifier, list = list, onClick = onClick)
        }
    }

}

@Composable
@Preview(showBackground = true)
fun ListView(modifier: Modifier = Modifier, list: List<MovieDTO>, onClick: (Long) -> Unit) {
    LazyColumn(
        modifier = modifier,
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
        modifier = modifier
            .padding(horizontal = 16.dp),
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
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect(),
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