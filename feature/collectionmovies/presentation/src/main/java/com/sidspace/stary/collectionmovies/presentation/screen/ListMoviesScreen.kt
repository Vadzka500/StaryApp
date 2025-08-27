package com.sidspace.stary.collectionmovies.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi

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

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sidspace.stary.ui.FilterSection
import com.sidspace.stary.ui.InitList
import com.sidspace.stary.ui.MovieCardGridShimmer
import com.sidspace.stary.ui.R
import com.sidspace.stary.ui.model.ResultData
import com.sidspace.stary.ui.uikit.poppinsFort
import com.sidspace.stary.ui.utils.getSystemBarHeight

import kotlinx.coroutines.flow.collectLatest


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
        if (state.value.list == ResultData.Loading)
            listMoviesViewModel.getMoviesByCollection(slug, limit = 250)
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

        when (val data = state.value.list) {

            is ResultData.Error -> {
                listMoviesViewModel.onIntent(ListMoviesIntent.ToErrorScreen)
            }

            ResultData.Loading -> {
                LoadList(modifier = Modifier.padding(top = 54.dp))
            }

            is ResultData.Success -> {
                InitList(
                    modifier = Modifier.padding(top = 54.dp),
                    list = data.data,
                    onClick = { id ->
                        listMoviesViewModel.onIntent(ListMoviesIntent.OnSelectMovie(id))

                    },
                    viewType = state.value.viewMode
                )
            }

            ResultData.None -> {

            }
        }



        Box(
            modifier = Modifier
                .height(topHeight)
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
                imageVector = Icons.AutoMirrored.Default.Notes,
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
        modifier = Modifier.padding(top = getSystemBarHeight() + topHeight),
        isVisibleFilter = state.value.isShowFilter,
        viewType = state.value.viewMode,
        isShowSort = true,
        onHideFilter = {
            listMoviesViewModel.onIntent(ListMoviesIntent.IsShowFilter(false))
        },
        setGridView = {
            listMoviesViewModel.onIntent(ListMoviesIntent.SetGridViewMode)
        },
        setListView = {
            listMoviesViewModel.onIntent(ListMoviesIntent.SetListViewMode)
        },
        sortType = state.value.sortType,
        sortDirection = state.value.sortDirection,
        toggleSortDirection = {
            listMoviesViewModel.onIntent(ListMoviesIntent.ToggleSortDirection)
        },
        setSortType = {
            listMoviesViewModel.onIntent(ListMoviesIntent.SetSortType(it))
        },
        sortList = {
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