package com.example.navwithapinothing_2.features.screen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import com.example.navwithapinothing_2.features.theme.ShimmerColorShades

/**
 * @Author: Vadim
 * @Date: 01.07.2025
 */

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
            colors = ShimmerColorShades,
            startX = 0f,
            endX = offsetX
        )
    )
}