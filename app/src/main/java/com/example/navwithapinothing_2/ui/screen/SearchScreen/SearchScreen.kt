package com.example.navwithapinothing_2.ui.screen.SearchScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moviesapi.models.Response
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.ui.screen.InitList
import com.example.navwithapinothing_2.ui.screen.MovieViewModel

/**
 * @Author: Vadim
 * @Date: 24.04.2025
 */

@Composable
fun SearchScreen(modifier: Modifier = Modifier, movieViewModel: MovieViewModel = hiltViewModel(), onSelectMovie:(Long) -> Unit) {
    //var text by remember { mutableStateOf("") }

    val state = movieViewModel.state_movie_search.collectAsState()
    val text = movieViewModel.searchStr

    Column(modifier = modifier) {
        TextField(
            value = text.value,
            onValueChange = { text.value = it },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.KeyboardVoice,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent)
                .padding(16.dp),
            placeholder = { Text(text = "Фильмы, сериалы") })


            when (val data = state.value) {
                is Result.Error<*> -> {
                    println("error")
                }

                Result.Loading -> {
                    println("loading")
                }

                is Result.Success<*> -> {
                    println("success")
                    InitList(list = (data.data as Response<*>).docs as List<MovieDTO>, onClick = onSelectMovie)

                }
            }


    }

    LaunchedEffect(text.value) {
        movieViewModel.getSearchMovies(text.value)
    }

}