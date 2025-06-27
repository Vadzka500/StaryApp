package com.example.navwithapinothing_2.features.screen.CollectionsScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.models.collection.CollectionMovie
import com.example.navwithapinothing_2.features.screen.MovieScreen.shimmerEffect
import com.example.navwithapinothing_2.features.screen.MovieViewModel
import com.example.navwithapinothing_2.features.theme.poppinsFort

/**
 * @Author: Vadim
 * @Date: 25.04.2025
 */

@Composable
fun CollectionsScreen(
    modifier: Modifier = Modifier,
    movieViewModel: MovieViewModel = hiltViewModel(),
    onSelectCollection: (String, String) -> Unit
) {

    val collection_state = movieViewModel.state_collection.collectAsState()

    LaunchedEffect(Unit) {
        movieViewModel.getCollections()
    }

    var countOfCollection by remember {
        mutableIntStateOf(0)
    }

    Column {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Назад")

            Text(
                "Коллекции",
                modifier = Modifier.padding(start = 16.dp),
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFort,
                fontSize = 24.sp,
            )

            Text(
                countOfCollection.toString(),
                modifier = Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 24.sp,
            )
        }

        AnimatedContent(
            targetState = collection_state.value,
            transitionSpec = { fadeIn() togetherWith fadeOut() }) { state ->
            when (val data = state) {
                is Result.Error<*> -> {

                }

                Result.Loading -> {
                    ShimmerCollection()
                }

                is Result.Success<*> -> {
                    countOfCollection = (data.data as List<CollectionMovie>).size
                    ShowCollections(
                        list = data.data as List<CollectionMovie>,
                        onSelectCollection = onSelectCollection,
                        modifier = modifier
                    )
                }
            }

        }
    }

}

@Composable
fun ShimmerCollection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(8) {
            InitItemShimmer()
        }
    }
}

@Composable
private fun InitItemShimmer() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .shimmerEffect()
    ) {

    }
}

@Composable
private fun ShowCollections(
    list: List<CollectionMovie>,
    modifier: Modifier,
    onSelectCollection: (String, String) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(list) { index, item ->
            InitItem(item = item, onSelectCollection = onSelectCollection)
        }
    }
}

@Composable
private fun InitItem(
    item: CollectionMovie,
    modifier: Modifier = Modifier,
    onSelectCollection: (String, String) -> Unit,
    movieViewModel: MovieViewModel = hiltViewModel()
) {


    val state = movieViewModel.state_movie_visible_collection.collectAsState()
    var count by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        movieViewModel.getCountMoviesByCollection(item.slug)
        println("data = " + state.value)

    }

    LaunchedEffect(state.value) {
        count = state.value[item.slug]!!
        println("data3 = " + state.value)
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)

            .clip(RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .clickable { onSelectCollection(item.name, item.slug) }
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = item.name,
            fontFamily = poppinsFort,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
        Spacer(modifier = Modifier.padding(top = 8.dp))



            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = count.toString() + "/" + item.moviesCount!!.toString(),
                fontFamily = poppinsFort,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

    }
}