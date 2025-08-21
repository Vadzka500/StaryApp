package com.sidspace.stary.folders.presentation.screen



sealed class FoldersEffect {
    data class ToFolderScreen(val id: Long): FoldersEffect()
    object OnBack: FoldersEffect()
    data class ErrorToast(val str: String): FoldersEffect()
    object ToErrorScreen: FoldersEffect()
}