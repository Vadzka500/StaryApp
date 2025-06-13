package com.example.navwithapinothing_2.ui.screen

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
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.ui.theme.Purple40
import com.example.navwithapinothing_2.ui.theme.poppinsFort
import com.example.navwithapinothing_2.utils.ScoreManager

/**
 * @Author: Vadim
 * @Date: 05.06.2025
 */


@Composable
fun InitRatingView(item: MovieDTO, modifier: Modifier = Modifier) {
    DrawView(item.rating?.kp ?: item.rating?.imdb ?: 0.0)
}

@Composable
fun DrawView(it: Double, modifier: Modifier = Modifier) {
    if (it == 0.0) return
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .then(
                if (it < 5) {
                    Modifier.background(color = Color.Red)
                } else if (it < 7) {
                    Modifier.background(color = Color.Gray)
                } else Modifier.background(color = Purple40)
            )
    ) {
        Text(
           ScoreManager.ratingToFormat(it).toString(),
            //"%.1f".format(it).replace(",","."),
            //String.format(java.util.Locale.ENGLISH, "%.1f", it),
            modifier = Modifier.padding(horizontal = 6.dp),
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )
    }
}
