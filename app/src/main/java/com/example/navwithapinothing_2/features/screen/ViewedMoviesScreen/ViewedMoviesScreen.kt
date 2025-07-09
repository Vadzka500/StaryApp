package com.example.navwithapinothing_2.features.screen.ViewedMoviesScreen

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
import com.example.navwithapinothing_2.R

import com.example.navwithapinothing_2.features.screen.CollectionMoviesScreen.InitList
import com.example.navwithapinothing_2.features.screen.CollectionMoviesScreen.ListMoviesIntent
import com.example.navwithapinothing_2.features.screen.CollectionMoviesScreen.LoadList
import com.example.navwithapinothing_2.features.screen.FilterSection
import com.example.navwithapinothing_2.features.screen.FoldersScreen.FoldersIntent
import com.example.navwithapinothing_2.features.screen.FoldersScreen.ResultFilterData
import com.example.navwithapinothing_2.features.screen.FoldersScreen.ShowCollectionList
import com.example.navwithapinothing_2.features.theme.poppinsFort
import com.example.navwithapinothing_2.models.common.ListMoviesResult
import kotlinx.coroutines.flow.collectLatest

/**
 * @Author: Vadim
 * @Date: 08.07.2025
 */

@Composable
fun ViewedMoviesScreen(
    onBack: () -> Unit,
    onSelectMovie: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewedMoviesViewModel: ViewedMoviesViewModel = hiltViewModel()
) {

    val state = viewedMoviesViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewedMoviesViewModel.effect.collectLatest { effect ->
            when(effect){
                ViewedMovieEffect.OnBack -> {
                    onBack()
                }
                is ViewedMovieEffect.OnSelectMovie -> {
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
                LoadList(modifier = Modifier.padding(top = 60.dp))
            }

            is ListMoviesResult.Success -> {
                InitList(
                    modifier = Modifier.padding(top = 60.dp),
                    list = data.list,
                    onClick = { id ->
                        viewedMoviesViewModel.onIntent(ViewedMovieIntent.OnSelectMovie(id))

                    },
                    viewType = state.value.viewMode
                )
            }
        }

        Row(
            modifier = Modifier
                .height(50.dp)
                .padding(horizontal = 12.dp)
                , verticalAlignment = Alignment.CenterVertically

        ) {

            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "",
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
                "Просмотрено",
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
                painter = painterResource(R.drawable.settings),
                contentDescription = "",
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

        FilterSection(
            modifier = Modifier.padding(top = 55.dp),
            isVisibleFilter = state.value.isShowFilter,
            viewType = state.value.viewMode,
            true,
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