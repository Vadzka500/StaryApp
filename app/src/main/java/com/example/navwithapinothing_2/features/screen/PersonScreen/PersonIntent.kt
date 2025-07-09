package com.example.navwithapinothing_2.features.screen.PersonScreen

/**
 * @Author: Vadim
 * @Date: 09.07.2025
 */
sealed interface PersonIntent {
    data class OnSelectMovie(val id: Long): PersonIntent
    data class LoadPersonData(val id: Long): PersonIntent
}