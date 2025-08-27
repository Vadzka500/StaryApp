package com.sidspace.stary.ui.utils

class ScoreManager {

    companion object{

        fun ratingToFormat(value: Double): Double {
            return (value * 10).toInt() / 10.0
        }
    }
}