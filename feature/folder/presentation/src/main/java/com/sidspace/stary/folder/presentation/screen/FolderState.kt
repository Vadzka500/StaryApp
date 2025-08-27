package com.sidspace.stary.folder.presentation.screen

import com.sidspace.stary.domain.model.Folder

import com.sidspace.stary.ui.enum.SortDirection
import com.sidspace.stary.ui.enum.SortType
import com.sidspace.stary.ui.enum.ViewMode
import com.sidspace.stary.ui.model.MovieUi
import com.sidspace.stary.ui.model.ResultData


data class FolderState(
    val list: ResultData<List<MovieUi>> = ResultData.Loading,
    val countMovies: Int = 0,
    val folder: Folder? = null,
    val isShowDialog: Boolean = false,
    val viewMode: ViewMode = ViewMode.GRID,
    val isShowFilter: Boolean = false,
    val sortType: SortType = SortType.NONE,
    val sortDirection: SortDirection = SortDirection.DESCENDING
)