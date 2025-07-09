package com.example.navwithapinothing_2.features.screen.SearchScreen

import com.example.navwithapinothing_2.features.screen.CollectionMoviesScreen.ListMoviesIntent

/**
 * @Author: Vadim
 * @Date: 08.07.2025
 */
sealed interface SearchIntent {
    data class UpdateSearchStr(val str: String): SearchIntent
    data class OnSelectMovie(val id: Long): SearchIntent
    data class IsShowFilter(val isShow: Boolean): SearchIntent
    object SetGridViewMode: SearchIntent
    object SetListViewMode: SearchIntent
}