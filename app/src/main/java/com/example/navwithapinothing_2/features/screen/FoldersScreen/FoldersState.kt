package com.example.navwithapinothing_2.features.screen.FoldersScreen

import com.example.navwithapinothing_2.database.models.Folder
import com.example.navwithapinothing_2.database.models.FolderWithMovies
import com.example.navwithapinothing_2.models.Filter

/**
 * @Author: Vadim
 * @Date: 27.06.2025
 */
data class FoldersState(
    val filters: ResultFilterData = ResultFilterData.Loading,
    var isShowBottomSheet: Boolean = false,
    val textFieldFolderValue: String = "",
    val selectColor: Int = 0,
    val selectImage: Int = 0
)

sealed class ResultFilterData {
    object Loading : ResultFilterData()
    data class Error(val message: String) : ResultFilterData()
    data class Success(val data: List<FolderWithMovies>) : ResultFilterData()
}