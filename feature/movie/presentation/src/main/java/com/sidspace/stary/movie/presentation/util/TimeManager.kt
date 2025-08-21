package com.sidspace.stary.movie.presentation.util


class TimeManager {

    companion object{
        fun getTimeByMinutes(data: Int): String{
            val hours = data / 60
            val minutes = data % 60
            return "$hours ч $minutes мин"
        }
    }


}