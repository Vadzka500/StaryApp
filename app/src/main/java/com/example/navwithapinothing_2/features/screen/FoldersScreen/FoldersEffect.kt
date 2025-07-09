package com.example.navwithapinothing_2.features.screen.FoldersScreen

/**
 * @Author: Vadim
 * @Date: 27.06.2025
 */

sealed class FoldersEffect {
    data class ToFolderScreen(val id: Long): FoldersEffect()
    object OnBack: FoldersEffect()
    data class ErrorToast(val str: String): FoldersEffect()
}