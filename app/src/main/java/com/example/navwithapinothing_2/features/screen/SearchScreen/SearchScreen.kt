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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moviesapi.models.Response
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.R
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.features.screen.FilterSection
import com.example.navwithapinothing_2.features.screen.InitList
import com.example.navwithapinothing_2.features.screen.MovieViewModel
import com.example.navwithapinothing_2.features.screen.ViewType

/**
 * @Author: Vadim
 * @Date: 24.04.2025
 */

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    movieViewModel: MovieViewModel = hiltViewModel(),
    onSelectMovie: (Long) -> Unit
) {
    //var text by remember { mutableStateOf("") }

    val state = movieViewModel.state_movie_search.collectAsState()
    val text = movieViewModel.searchStr

    val isVisibleFilter = remember {
        mutableStateOf(false)
    }

    val list = remember {
        mutableStateOf<List<MovieDTO>>(listOf())
    }

    val viewType = remember {
        mutableStateOf(ViewType.GRID)
    }

    Box() {
        Column(modifier = modifier) {
            Row(verticalAlignment = Alignment.CenterVertically) {


                TextField(
                    value = text.value,
                    onValueChange = { text.value = it },
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
                            isVisibleFilter.value = !isVisibleFilter.value
                        }
                        .padding(8.dp)) // внутренний отступ, чтобы иконка была не впритык)

            }


            when (val data = state.value) {
                is Result.Error<*> -> {
                    println("error")
                }

                Result.Loading -> {
                    println("loading")
                }

                is Result.Success<*> -> {
                    println("success")
                    list.value = (data.data as Response<*>).docs as List<MovieDTO>
                    InitList(modifier = Modifier, list = list, onClick = onSelectMovie, viewType)

                }
            }


        }
        FilterSection(modifier = Modifier.padding(top = 85.dp), list = list, isVisibleFilter = isVisibleFilter, viewType = viewType, false)
    }

    LaunchedEffect(text.value) {
        movieViewModel.getSearchMovies(text.value)
    }

}