package com.sidspace.stary.ui

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp





@Composable
fun ShimmerGridList(modifier: Modifier = Modifier, count: Int) {
    LazyVerticalGrid(
        modifier = modifier
            .padding(horizontal = 16.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(if(count > 10) 10 else count) {
            MovieCardGridShimmer()
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MovieCardGridShimmer(
    modifier: Modifier = Modifier
) {
    //println("name = " + item.name)

    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp / 2 - 22
    val height = (configuration.screenWidthDp / 2 - 22) * 1.5
    val boxHeight = height + 30

    Column(
        modifier = Modifier
           /* .then(
                if (index == 0) Modifier.padding(start = 16.dp)
                else Modifier
            )*/
            .fillMaxWidth()
            .height(boxHeight.dp)
            .width(width.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(width.dp)
                .height(height.dp) // 255
                .clip(RoundedCornerShape(16.dp))
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .height(14.dp)
                .width(70.dp)
                .clip(
                    RoundedCornerShape(3.dp)
                )
                .shimmerEffect(),
        )
    }
}

@Composable
fun ShimmerMovies(modifier: Modifier = Modifier) {

    Column(
        modifier = Modifier

            .padding(top = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp)
                .width(150.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect()
        )
        LazyRow(modifier = Modifier.padding(top = 16.dp)) {
            items(5) { index ->
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .width(150.dp)
                        .height(240.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
            }
        }
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "")
    val offsetX by transition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
        ), label = ""
    )
    background(
        brush = Brush.horizontalGradient(
            colors = _root_ide_package_.com.sidspace.stary.ui.uikit.ShimmerColorShades,
            startX = 0f,
            endX = offsetX
        )
    )
}