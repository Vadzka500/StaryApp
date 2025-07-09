package com.example.navwithapinothing_2.features.screen.FolderScreen

/**
 * @Author: Vadim
 * @Date: 03.07.2025
 */
sealed class FolderEffect {
    data object OnBack: FolderEffect()
    data class OnSelectedMovie(val id: Long): FolderEffect()
}