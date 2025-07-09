package com.example.navwithapinothing_2.features.screen.PersonScreen

/**
 * @Author: Vadim
 * @Date: 09.07.2025
 */
sealed class PersonEffect {
    data class OnSelectMovie(val id: Long): PersonEffect()
}