package com.example.navwithapinothing_2.features.screen.FoldersScreen

/**
 * @Author: Vadim
 * @Date: 27.06.2025
 */
sealed interface FoldersIntent {
    data class ToFolderScreen(val id: Long) : FoldersIntent
    object ClickCreateFolder: FoldersIntent
    object HideBottomSheet: FoldersIntent
    object OnBack: FoldersIntent
}