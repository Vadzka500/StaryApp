package com.example.navwithapinothing_2.utils

import java.util.Locale

/**
 * @Author: Vadim
 * @Date: 25.12.2024
 */
class ScoreManager {

    companion object{
        fun getRoundScore(score : Double): Double{
            return String.format(Locale.ENGLISH,"%.1f", score).toDouble()
        }
    }
}