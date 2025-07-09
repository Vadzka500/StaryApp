package com.example.navwithapinothing_2.features.screen.SearchScreen

/**
 * @Author: Vadim
 * @Date: 08.07.2025
 */
sealed class SearchEffect {
    data class OnSelectMovie(val id: Long): SearchEffect()
}