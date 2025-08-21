package com.sidspace.stary.collections.presentation.screen


import com.sidspace.stary.ui.model.CollectionUi
import com.sidspace.stary.ui.model.ResultData


data class CollectionsState(
    val collectionResult: ResultData<List<CollectionUi>> = ResultData.Loading,
    val countCollection: Int = 0,
)