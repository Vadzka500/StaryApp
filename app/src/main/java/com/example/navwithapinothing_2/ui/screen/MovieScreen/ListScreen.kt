package com.example.navwithapinothing_2.ui.screen.MovieScreen


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.R
import com.example.navwithapinothing_2.data.Result

@Composable
fun ListScreen(modifier: Modifier = Modifier, movieViewModel: MovieViewModel) {
    val state = movieViewModel.state.collectAsState()
    val articles = movieViewModel.getAllPaging().collectAsLazyPagingItems()

    LazyColumn {

        items(articles.itemCount) { index ->

            MovieCard(item = articles.get(index)!!)

        }

        /*items(items = articles) { item ->
            item?.let {
                MovieCard(item = it)
            }

        }*/
    }

    //InitList(list = articles)
    /* when (val data = state.value) {
         is Result.Success -> {
             InitList(list = articles)
         }

         is Result.Loading -> {

         }

         is Result.Error -> {

         }
     }*/


}

@Composable
fun InitList(modifier: Modifier = Modifier, list: LazyPagingItems<MovieDTO>) {
    LazyColumn {
        items(list.itemSnapshotList) { item ->
            item?.let {
                MovieCard(item = it)
            }

        }
    }
}

@Composable
fun MovieCard(modifier: Modifier = Modifier, item: MovieDTO) {
    //println("name = " + item.name)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.poster?.previewUrl,
            contentDescription = null,
            contentScale = ContentScale.FillHeight, error = painterResource(R.drawable.error)
        )
        Text(text = item.name ?: item.alternativeName ?: "null")
    }
}