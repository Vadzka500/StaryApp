package com.example.navwithapinothing_2.features.screen.AccountScreen

/**
 * @Author: Vadim
 * @Date: 25.06.2025
 */
sealed class AccountEffect {
    object ToFoldersScreen: AccountEffect()
    data class ToMovieScreen(val id: Long) : AccountEffect()
    object ToBookmarkScreen: AccountEffect()
    object ToViewedScreen: AccountEffect()
}