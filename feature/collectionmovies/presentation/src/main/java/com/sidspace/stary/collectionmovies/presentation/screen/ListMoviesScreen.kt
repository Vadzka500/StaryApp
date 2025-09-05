package com.sidspace.stary.collectionmovies.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sidspace.stary.ui.FilterSection
import com.sidspace.stary.ui.FilterStateCallback
import com.sidspace.stary.ui.InitList
import com.sidspace.stary.ui.MovieCardGridShimmer
import com.sidspace.stary.ui.enums.ViewMode
import com.sidspace.stary.ui.model.MovieUi
import com.sidspace.stary.ui.model.ResultData
import com.sidspace.stary.ui.uikit.poppinsFort
import com.sidspace.stary.ui.utils.getSystemBarHeight
import kotlinx.coroutines.flow.collectLatest


@Composable
fun ListMoviesToolbar(modifier: Modifier, label: String, onBack: () -> Unit, isShowFilter: () -> Unit) {
    Box(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp)
                .align(Alignment.Center),
            text = label,
            textAlign = TextAlign.Center,
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
                .align(Alignment.CenterStart))

        Icon(
            imageVector = Icons.AutoMirrored.Default.Notes,
            contentDescription = "",
            modifier = Modifier
                .size(40.dp)
                .clip(
                    CircleShape
                )
                .clickable {
                    isShowFilter()
                }
                .padding(8.dp)
                .align(Alignment.CenterEnd)
        )

    }
}

@Composable
fun AllMoviesScreen(
    label: String,
    slug: String,
    modifier: Modifier = Modifier,
    listMoviesViewModel: ListMoviesViewModel = hiltViewModel(),
    onSelectMovie: (Long) -> Unit,
    onBack: () -> Unit,
    toErrorScreen: () -> Unit
) {

    val state = listMoviesViewModel.state.collectAsState()

    LaunchedEffect(slug) {
        if (state.value.list == ResultData.Loading) {
            listMoviesViewModel.getMoviesByCollection(slug, limit = 250)
        }
    }
    val topHeight = 50.dp

    LaunchedEffect(Unit) {
        listMoviesViewModel.effect.collectLatest { effect ->
            when (effect) {
                is ListMoviesEffect.OnBack -> {
                    onBack()
                }

                is ListMoviesEffect.OnSelectMovie -> {
                    onSelectMovie(effect.id)
                }

                ListMoviesEffect.ToErrorScreen -> {
                    toErrorScreen()
                }
            }
        }
    }

    Box(modifier = modifier.padding(top = 5.dp)) {

        ListContent(result = state.value.list, viewMode = state.value.viewMode, onError = {
            listMoviesViewModel.onIntent(ListMoviesIntent.ToErrorScreen)
        }, onSelectMovie = {
            listMoviesViewModel.onIntent(ListMoviesIntent.OnSelectMovie(it))
        })

        ListMoviesToolbar(
            modifier = Modifier
                .height(topHeight)
                .padding(horizontal = 12.dp),
            label = label,
            onBack = {
                listMoviesViewModel.onIntent(ListMoviesIntent.OnBack)
            },
            isShowFilter = {
                listMoviesViewModel.onIntent(ListMoviesIntent.IsShowFilter(!state.value.isShowFilter))
            }
        )
    }

    MovieFilterSection(state = state.value, listMoviesViewModel = listMoviesViewModel, topHeight = topHeight)


}

@Composable
fun MovieFilterSection(state: ListMoviesState, listMoviesViewModel: ListMoviesViewModel, topHeight: Dp) {
    val filterStateCallback = FilterStateCallback(
        onHideFilter = {
            listMoviesViewModel.onIntent(ListMoviesIntent.IsShowFilter(false))
        },
        setGridView = {
            listMoviesViewModel.onIntent(ListMoviesIntent.SetGridViewMode)
        },
        setListView = {
            listMoviesViewModel.onIntent(ListMoviesIntent.SetListViewMode)
        },
        setSortType = {
            listMoviesViewModel.onIntent(ListMoviesIntent.SetSortType(it))
        },
        sortList = {
            listMoviesViewModel.onIntent(ListMoviesIntent.SortMovies)
        },
        toggleSortDirection = {
            listMoviesViewModel.onIntent(ListMoviesIntent.ToggleSortDirection)
        }
    )

    FilterSection(
        modifier = Modifier.padding(top = getSystemBarHeight() + topHeight),
        isVisibleFilter = state.isShowFilter,
        viewType = state.viewMode,
        isShowSort = true,
        filterStateCallback = filterStateCallback,
        sortType = state.sortType,
        sortDirection = state.sortDirection
    )
}

@Composable
fun ListContent(
    result: ResultData<List<MovieUi>>,
    viewMode: ViewMode,
    onError: () -> Unit,
    onSelectMovie: (Long) -> Unit
) {
    when (val data = result) {

        is ResultData.Error -> {
            onError()
        }

        ResultData.Loading -> {
            LoadList(modifier = Modifier.padding(top = 54.dp))
        }

        is ResultData.Success -> {
            InitList(
                modifier = Modifier.padding(top = 54.dp),
                list = data.data,
                onClick = { id ->
                    onSelectMovie(id)

                },
                viewType = viewMode
            )
        }

        ResultData.None -> {

        }
    }
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
        items(ListMoviesState.SHIMMER_ITEMS) {
            MovieCardGridShimmer()
        }
    }
}
