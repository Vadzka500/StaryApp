package com.sidspace.stary.collections.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sidspace.stary.ui.model.CollectionUi
import com.sidspace.stary.ui.model.ResultData
import com.sidspace.stary.ui.shimmerEffect
import com.sidspace.stary.ui.uikit.poppinsFort


import kotlinx.coroutines.flow.collectLatest



@Composable
fun CollectionsScreen(
    modifier: Modifier = Modifier,
    onSelectCollection: (String, String) -> Unit,
    onBack: () -> Unit,
    toErrorScreen:() -> Unit,
    collectionsViewModel: CollectionsViewModel = hiltViewModel()
) {

    val state = collectionsViewModel.state.collectAsState()


    LaunchedEffect(Unit) {
        collectionsViewModel.effect.collectLatest { effect ->

            when (effect) {
                is CollectionsEffect.OnSelectCollection -> {
                    onSelectCollection(effect.name, effect.slug)
                }

                CollectionsEffect.OnBack -> {
                    onBack()
                }

                CollectionsEffect.ToErorrScreen -> {
                    toErrorScreen()
                }
            }

        }
    }


    Column(modifier = modifier) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
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
                        collectionsViewModel.onIntent(CollectionsIntent.OnBack)
                    }
                    .padding(8.dp))


            Text(
                "Коллекции",
                modifier = Modifier.padding(start = 16.dp),
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFort,
                fontSize = 24.sp,
            )

            Text(
                state.value.countCollection.toString(),
                modifier = Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 24.sp,
            )
        }

        AnimatedContent(
            targetState = state.value.collectionResult,
            transitionSpec = { fadeIn() togetherWith fadeOut() }) { state ->

            when (val data = state) {

                is ResultData.Error -> {
                    collectionsViewModel.onIntent(CollectionsIntent.OnError)
                }

                ResultData.Loading -> {
                    ShimmerCollection()
                }

                is ResultData.Success -> {
                    ShowCollections(
                        list = data.data,
                        modifier = Modifier
                    )
                }

                ResultData.None -> {

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
    list: List<CollectionUi>,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(list) { index, item ->
            InitItem(item = item)
        }
    }
}

@Composable
private fun InitItem(
    item: CollectionUi,
    modifier: Modifier = Modifier,
    collectionsViewModel: CollectionsViewModel = hiltViewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)

            .clip(RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .clickable {
                collectionsViewModel.onIntent(
                    CollectionsIntent.OnSelectCollection(
                        item.name,
                        item.slug
                    )
                )
            }
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
        Spacer(modifier = Modifier.height(8.dp))



        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = item.viewedCount.toString() + "/" + item.moviesCount!!.toString(),
            fontFamily = poppinsFort,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )

    }
}