package com.sidspace.stary.search.presentation.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sidspace.stary.search.presentation.screen.SearchIntent.IsShowFilter
import com.sidspace.stary.search.presentation.screen.SearchIntent.OnError
import com.sidspace.stary.search.presentation.screen.SearchIntent.SetGridViewMode
import com.sidspace.stary.search.presentation.screen.SearchIntent.SetListViewMode
import com.sidspace.stary.search.presentation.screen.SearchIntent.UpdateSearchStr
import com.sidspace.stary.ui.FilterSection
import com.sidspace.stary.ui.FilterStateCallback
import com.sidspace.stary.ui.InitList
import com.sidspace.stary.ui.ShimmerGridList
import com.sidspace.stary.ui.enums.ViewMode
import com.sidspace.stary.ui.model.MovieUi
import com.sidspace.stary.ui.model.ResultData
import com.sidspace.stary.ui.utils.getSystemBarHeight
import kotlinx.coroutines.flow.collectLatest


@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = hiltViewModel(),
    onSelectMovie: (Long) -> Unit,
    toErrorScreen: () -> Unit,
) {

    val state = searchViewModel.state.collectAsState()

    val topHeight = 90.dp

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        searchViewModel.effect.collectLatest { effect ->
            when (effect) {
                is SearchEffect.OnSelectMovie -> onSelectMovie(effect.id)
                SearchEffect.ToErrorScreen -> {
                    toErrorScreen()
                }
            }
        }
    }

    Box {
        Column(modifier = modifier) {

            SearchToolbar(
                topHeight = topHeight,
                searchStr = state.value.searchStr,
                valueChanged = {
                    searchViewModel.onIntent(UpdateSearchStr(it))
                },
                visibleFilter = {
                    searchViewModel.onIntent(IsShowFilter(!state.value.isVisibleFilter))
                },
                onSearch = {
                    keyboardController!!.hide()
                }
            )


            SearchContent(result = state.value.list, viewMode = state.value.viewMode, onError = {
                searchViewModel.onIntent(OnError)
            }, onSelectMovie = {
                searchViewModel.onIntent(
                    SearchIntent.OnSelectMovie(it)
                )
            })

        }

        SearchFilterSection(state = state.value, searchViewModel = searchViewModel, topHeight = topHeight)
    }

}

@Composable
fun SearchFilterSection(state: SearchState, searchViewModel: SearchViewModel, topHeight: Dp) {
    val filterStateCallback = FilterStateCallback(
        onHideFilter = {
            searchViewModel.onIntent(IsShowFilter(false))
        },
        setGridView = {
            searchViewModel.onIntent(SetGridViewMode)
        },
        setListView = {
            searchViewModel.onIntent(SetListViewMode)
        },
        setSortType = {

        },
        sortList = {

        },
        toggleSortDirection = {

        }
    )

    FilterSection(
        modifier = Modifier.padding(top = getSystemBarHeight() + topHeight),
        isVisibleFilter = state.isVisibleFilter,
        viewType = state.viewMode,
        isShowSort = false,
        filterStateCallback = filterStateCallback
    )
}

@Composable
fun SearchContent(
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
            ShimmerGridList(count = 16)
        }

        is ResultData.Success -> {
            InitList(modifier = Modifier, list = data.data, onClick = { id ->
                onSelectMovie(id)
            }, viewType = viewMode)
        }

        ResultData.None -> {

        }
    }
}


@Composable
fun SearchToolbar(
    topHeight: Dp,
    searchStr: String,
    onSearch: () -> Unit,
    valueChanged: (String) -> Unit,
    visibleFilter: () -> Unit
) {
    Row(
        modifier = Modifier.height(topHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {


        TextField(
            value = searchStr,
            singleLine = true,
            onValueChange = { valueChanged(it) },
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
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch()
                }
            ),

            modifier = Modifier
                .weight(1f)
                .background(color = Color.Transparent)
                .padding(16.dp),
            placeholder = { Text(text = "Фильмы, сериалы") })

        Icon(
            imageVector = Icons.AutoMirrored.Default.Notes,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 16.dp)
                .size(40.dp)

                .clip(
                    CircleShape
                )
                .clickable {
                    visibleFilter()

                }
                .padding(8.dp)
        )

    }
}
