package com.sidspace.stary.movie.presentation.util


object TimeManager {

    private const val MINUTES_IN_HOUR = 60

    fun getTimeByMinutes(data: Int): String {
        val hours = data / MINUTES_IN_HOUR
        val minutes = data % MINUTES_IN_HOUR
        return "$hours ч $minutes мин"
    }

}
