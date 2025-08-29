package com.sidspace.stary.folder.presentation.screen


sealed class FolderEffect {
    data object OnBack : FolderEffect()
    data class OnSelectedMovie(val id: Long) : FolderEffect()
    object ToErrorScreen : FolderEffect()
}
