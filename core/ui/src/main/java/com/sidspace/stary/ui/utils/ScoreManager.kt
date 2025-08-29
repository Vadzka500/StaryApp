package com.sidspace.stary.ui.utils

object ScoreManager {

    private const val RATING_SCALE = 10.0

    fun ratingToFormat(value: Double): Double {
        return (value * RATING_SCALE).toInt() / RATING_SCALE
    }

}
