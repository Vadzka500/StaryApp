package com.sidspace.stary.folders.presentation.screen

import androidx.compose.ui.graphics.Color
import com.sidspace.stary.domain.model.Folder

import com.example.ui.model.ResultData
import com.sidspace.stary.folders.presentation.FiltersUtil


data class FoldersState(
    val filters: ResultData<List<Folder>> = ResultData.Loading,
    var isShowBottomSheet: Boolean = false,
    val textFieldFolderValue: String = "",
    val selectColor: Int = 0,
    val selectImage: Int = 0,
    val selectImageName: String? = null,
    val listOfColors: List<Color> = FiltersUtil.listOfColors,
    val listOfImages: List<Int?> = FiltersUtil.listOfImage,
    val isErrorEmptyName: Boolean = false
)