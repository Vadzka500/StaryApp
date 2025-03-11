package com.example.navwithapinothing_2.utils

/**
 * @Author: Vadim
 * @Date: 24.12.2024
 */
class TimeManager {

    companion object{
        fun getTimeByMinutes(data: Int): String{
            val hours = data / 60
            val minutes = data % 60
            return "$hours ч $minutes мин"
        }
    }


}