package com.example.navwithapinothing_2.features.screen.FolderScreen

/**
 * @Author: Vadim
 * @Date: 03.07.2025
 */
sealed interface FolderIntent {

    data class LoadFolder(val id: Long): FolderIntent
}