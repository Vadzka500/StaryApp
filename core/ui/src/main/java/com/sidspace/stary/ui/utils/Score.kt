package com.sidspace.stary.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun InitRatingView(score: Double?) {
    DrawView(score ?: 0.0)
}

const val BAD_SCORE = 5
const val MEDIUM_SCORE = 7

@Composable
fun DrawView(it: Double, modifier: Modifier = Modifier) {
    if (it == 0.0) return

    Box(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .then(
                if (it < BAD_SCORE) {
                    Modifier.background(color = Color.Red)
                } else if (it < MEDIUM_SCORE) {
                    Modifier.background(color = Color.Gray)
                } else {
                    Modifier.background(color = _root_ide_package_.com.sidspace.stary.ui.uikit.Purple40)
                }
            )
    ) {
        Text(
            ScoreManager.ratingToFormat(it).toString(),
            modifier = Modifier.padding(horizontal = 6.dp),
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontFamily = _root_ide_package_.com.sidspace.stary.ui.uikit.poppinsFort,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )
    }
}
