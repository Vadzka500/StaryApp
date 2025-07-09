package com.example.navwithapinothing_2.features.screen.SearchScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.navwithapinothing_2.R

import com.example.navwithapinothing_2.features.screen.CollectionMoviesScreen.InitList
import com.example.navwithapinothing_2.features.screen.CollectionMoviesScreen.LoadList
import com.example.navwithapinothing_2.features.screen.FilterSection
import kotlinx.coroutines.flow.collectLatest

/**
 * @Author: Vadim
 * @Date: 24.04.2025
 */

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = hiltViewModel(),
    onSelectMovie: (Long) -> Unit
) {

    val state = searchViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        searchViewModel.effect.collectLatest { effect ->
            when (effect) {
                is SearchEffect.OnSelectMovie -> onSelectMovie(effect.id)
            }
        }
    }

    Box() {
        Column(modifier = modifier) {
            Row(verticalAlignment = Alignment.CenterVertically) {


                TextField(
                    value = state.value.searchStr,
                    onValueChange = { searchViewModel.onIntent(SearchIntent.UpdateSearchStr(it)) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.KeyboardVoice,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .background(color = Color.Transparent)
                        .padding(16.dp),
                    placeholder = { Text(text = "Фильмы, сериалы") })

                Icon(
                    painter = painterResource(R.drawable.settings),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(40.dp)

                        .clip(
                            CircleShape
                        )
                        .clickable {
                            searchViewModel.onIntent(SearchIntent.IsShowFilter(!state.value.isVisibleFilter))

                        }
                        .padding(8.dp)) // внутренний отступ, чтобы иконка была не впритык)

            }


            when (val data = state.value.list) {


                is SearchResult.Error -> {

                }

                SearchResult.Loading -> {
                    LoadList()
                }

                is SearchResult.Success -> {
                    InitList(modifier = Modifier, list = data.list, onClick = { id ->
                        searchViewModel.onIntent(
                            SearchIntent.OnSelectMovie(id)
                        )
                    }, viewType = state.value.viewMode)
                }
            }

        }
        FilterSection(
            modifier = Modifier.padding(top = 135.dp),
            isVisibleFilter = state.value.isVisibleFilter,
            viewType = state.value.viewMode,
            false,
            onHideFilter = {
                searchViewModel.onIntent(SearchIntent.IsShowFilter(false))
            },
            setGridView = {
                searchViewModel.onIntent(SearchIntent.SetGridViewMode)
            },
            setListView = {
                searchViewModel.onIntent(SearchIntent.SetListViewMode)
            }, toggleSortDirection = {

            }, setSortType = {

            }, sortList = {

            }
        )
    }

}