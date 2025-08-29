package com.sidspace.stary.folders.presentation.screen


sealed interface FoldersIntent {
    data class ToFolderScreen(val id: Long) : FoldersIntent
    object ClickCreateFolder : FoldersIntent
    object HideBottomSheet : FoldersIntent
    object OnBack : FoldersIntent
    data class UpdateNameFolder(val name: String) : FoldersIntent
    data class UpdateColor(val colorIndex: Int) : FoldersIntent
    data class UpdateImage(val imageIndex: Int, val selectImageName: String?) : FoldersIntent
    object AddFolder : FoldersIntent
    object OnError : FoldersIntent
}
