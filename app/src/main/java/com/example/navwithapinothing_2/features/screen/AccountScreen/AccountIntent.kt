package com.example.navwithapinothing_2.features.screen.AccountScreen

/**
 * @Author: Vadim
 * @Date: 25.06.2025
 */
sealed interface AccountIntent {
    object ToFoldersScreen: AccountIntent
    data class ToMovieScreen(val id: Long): AccountIntent
}