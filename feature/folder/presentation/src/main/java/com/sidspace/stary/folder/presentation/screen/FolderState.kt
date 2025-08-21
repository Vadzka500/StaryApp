package com.sidspace.stary.folder.presentation.screen

import com.sidspace.stary.domain.model.Folder
import com.example.ui.enum.SortDirection
import com.example.ui.enum.SortType
import com.example.ui.enum.ViewMode
import com.example.ui.model.MovieData
import com.example.ui.model.ResultData



data class FolderState(
    val list: ResultData<List<MovieData>> = ResultData.Loading,
    val countMovies: Int = 0,
    val folder: Folder? = null,
    val isShowDialog: Boolean = false,
    val viewMode: ViewMode = ViewMode.GRID,
    val isShowFilter: Boolean = false,
    val sortType: SortType = SortType.NONE,
    val sortDirection: SortDirection = SortDirection.DESCENDING
)