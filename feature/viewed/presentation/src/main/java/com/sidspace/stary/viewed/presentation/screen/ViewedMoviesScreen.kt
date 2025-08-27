package com.sidspace.stary.viewed.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sidspace.stary.ui.FilterSection
import com.sidspace.stary.ui.InitList
import com.sidspace.stary.ui.R
import com.sidspace.stary.ui.ShimmerGridList
import com.sidspace.stary.ui.model.ResultData
import com.sidspace.stary.ui.uikit.poppinsFort


import kotlinx.coroutines.flow.collectLatest


@Composable
fun ViewedMoviesScreen(
    modifier: Modifier = Modifier,
    viewedMoviesViewModel: ViewedMoviesViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSelectMovie: (Long) -> Unit,
    toErrorScreen: () -> Unit,
) {

    val state = viewedMoviesViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewedMoviesViewModel.effect.collectLatest { effect ->
            when (effect) {
                ViewedMovieEffect.OnBack -> {
                    onBack()
                }

                is ViewedMovieEffect.OnSelectMovie -> {
                    onSelectMovie(effect.id)
                }

                ViewedMovieEffect.ToErrorScreen -> {
                    toErrorScreen()
                }
            }
        }
    }

    val topHeight = 50.dp

    Box(modifier = modifier.padding(top = 5.dp)) {


        Row(
            modifier = Modifier
                .height(topHeight)
                .padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically

        ) {

            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(
                        CircleShape
                    )
                    .clickable {

                        viewedMoviesViewModel.onIntent(ViewedMovieIntent.OnBack)
                    }
                    .padding(8.dp))

            Text(
                stringResource(com.sidspace.stary.viewed.presentation.R.string.viewed),
                modifier = Modifier
                    .padding(start = 16.dp),
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFort,
                fontSize = 24.sp,
            )

            Text(
                state.value.countMovies.toString(),
                modifier = Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 24.sp,
            )


            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.AutoMirrored.Default.Notes,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(
                        CircleShape
                    )
                    .clickable {
                        viewedMoviesViewModel.onIntent(ViewedMovieIntent.IsShowFilters(!state.value.isShowFilter))
                    }
                    .padding(8.dp))

        }

        when (val data = state.value.list) {

            is ResultData.Error -> {
                viewedMoviesViewModel.onIntent(ViewedMovieIntent.OnError)
            }

            ResultData.Loading -> {
                ShimmerGridList(modifier = Modifier.padding(top = 60.dp), state.value.countMovies)
            }

            is ResultData.Success -> {
                if (data.data.isNotEmpty()) {
                    InitList(
                        modifier = Modifier.padding(top = 60.dp),
                        list = data.data,
                        onClick = { id ->
                            viewedMoviesViewModel.onIntent(ViewedMovieIntent.OnSelectMovie(id))

                        },
                        viewType = state.value.viewMode
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 60.dp), contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                modifier = Modifier.size(48.dp),
                                painter = painterResource(R.drawable.empty_result),
                                contentDescription = ""
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                stringResource(com.sidspace.stary.viewed.presentation.R.string.empty_movies),
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = poppinsFort,
                                fontSize = 16.sp,
                            )
                        }
                    }
                }
            }

            ResultData.None -> {

            }
        }



        FilterSection(
            modifier = Modifier.padding(top = topHeight + 5.dp),
            isVisibleFilter = state.value.isShowFilter,
            viewType = state.value.viewMode,
            isShowSort = true,
            onHideFilter = {
                viewedMoviesViewModel.onIntent(ViewedMovieIntent.IsShowFilters(false))
            },
            setGridView = {
                viewedMoviesViewModel.onIntent(ViewedMovieIntent.SetGridView)
            },
            setListView = {
                viewedMoviesViewModel.onIntent(ViewedMovieIntent.SetListView)
            }, sortType = state.value.sortType, sortDirection = state.value.sortDirection,
            toggleSortDirection = {
                viewedMoviesViewModel.onIntent(ViewedMovieIntent.ToggleSortDirection)
            },
            sortList = {
                viewedMoviesViewModel.onIntent(ViewedMovieIntent.SortMovies)
            }, setSortType = {
                viewedMoviesViewModel.onIntent(ViewedMovieIntent.SetSortType(it))
            }
        )
    }

}