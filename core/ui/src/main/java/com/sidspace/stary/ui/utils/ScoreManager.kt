package com.sidspace.stary.ui.utils

import java.util.Locale

class ScoreManager {

    companion object{
        fun getRoundScore(score : Double): Double{
            return String.format(Locale.ENGLISH,"%.1f", score).toDouble()
        }


        fun ratingToFormat(value: Double): Double {
            return (value * 10).toInt() / 10.0
        }
    }
}