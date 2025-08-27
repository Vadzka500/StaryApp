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
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sidspace.stary.search.presentation.screen.SearchIntent.*
import com.sidspace.stary.ui.FilterSection
import com.sidspace.stary.ui.InitList
import com.sidspace.stary.ui.R
import com.sidspace.stary.ui.ShimmerGridList
import com.sidspace.stary.ui.model.ResultData
import com.sidspace.stary.ui.utils.getSystemBarHeight


import kotlinx.coroutines.flow.collectLatest



@Composable
fun SearchScreen(
    onSelectMovie: (Long) -> Unit,
    toErrorScreen:() -> Unit,
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = hiltViewModel(),

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

    Box() {
        Column(modifier = modifier) {
            Row(modifier = Modifier.height(topHeight), verticalAlignment = Alignment.CenterVertically) {


                TextField(
                    value = state.value.searchStr,
                    singleLine = true,
                    onValueChange = { searchViewModel.onIntent(UpdateSearchStr(it)) },
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
                            keyboardController!!.hide()
                        }
                    ),


                  /*  trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.KeyboardVoice,
                            contentDescription = null
                        )
                    },*/
                    modifier = Modifier
                        .weight(1f)
                        .background(color = Color.Transparent)
                        .padding(16.dp),
                    placeholder = { Text(text = "Фильмы, сериалы") })

                Icon(
                    imageVector = Icons.AutoMirrored.Default.Notes ,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(40.dp)

                        .clip(
                            CircleShape
                        )
                        .clickable {
                            searchViewModel.onIntent(IsShowFilter(!state.value.isVisibleFilter))

                        }
                        .padding(8.dp)) // внутренний отступ, чтобы иконка была не впритык)

            }


            when (val data = state.value.list) {


                is ResultData.Error -> {
                    searchViewModel.onIntent(OnError)
                }

                ResultData.Loading -> {
                    ShimmerGridList(count = 16)
                }

                is ResultData.Success -> {
                    InitList(modifier = Modifier, list = data.data, onClick = { id ->
                        searchViewModel.onIntent(
                            SearchIntent.OnSelectMovie(id)
                        )
                    }, viewType = state.value.viewMode)
                }

                ResultData.None -> {

                }
            }

        }

        val context = LocalContext.current

        val resourcesHeight = remember(context) {
            context.resources.getIdentifier(
                "status_bar_height",
                "dimen",
                "android"
            ).takeIf { it > 0 }?.let {
                context.resources.getDimensionPixelSize(it).dp
            } ?: 0.dp
        }




        FilterSection(
            modifier = Modifier.padding(top = getSystemBarHeight() + topHeight),
            isVisibleFilter = state.value.isVisibleFilter,
            viewType = state.value.viewMode,
            false,
            onHideFilter = {
                searchViewModel.onIntent(IsShowFilter(false))
            },
            setGridView = {
                searchViewModel.onIntent(SetGridViewMode)
            },
            setListView = {
                searchViewModel.onIntent(SetListViewMode)
            }, toggleSortDirection = {

            }, setSortType = {

            }, sortList = {

            }
        )
    }



}
