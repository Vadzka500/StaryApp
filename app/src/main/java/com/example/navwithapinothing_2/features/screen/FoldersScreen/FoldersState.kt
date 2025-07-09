package com.example.navwithapinothing_2.features.screen.FoldersScreen

import androidx.compose.ui.graphics.Color
import com.example.navwithapinothing_2.database.models.Folder
import com.example.navwithapinothing_2.database.models.FolderWithMovies
import com.example.navwithapinothing_2.models.Filter
import com.example.navwithapinothing_2.utils.FiltersUtil

/**
 * @Author: Vadim
 * @Date: 27.06.2025
 */
data class FoldersState(
    val filters: ResultFilterData = ResultFilterData.Loading,
    var isShowBottomSheet: Boolean = false,
    val textFieldFolderValue: String = "",
    val selectColor: Int = 0,
    val selectImage: Int = 0,
    val selectImageName: String? = null,
    val listOfColors: List<Color> = FiltersUtil.listOfColors,
    val listOfImages: List<Int?> = FiltersUtil.listOfImage,
    val isErrorEmptyName: Boolean = false
)

sealed class ResultFilterData {
    object Loading : ResultFilterData()
    data class Error(val message: String) : ResultFilterData()
    data class Success(val data: List<FolderWithMovies>) : ResultFilterData()
}