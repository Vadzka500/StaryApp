package com.example.navwithapinothing_2.features.screen.CollectionsScreen

import com.example.navwithapinothing_2.models.collection.CollectionMovie

/**
 * @Author: Vadim
 * @Date: 03.07.2025
 */

data class CollectionsState(
    val collectionResult: CollectionsResult = CollectionsResult.Loading,
    val countCollection: Int = 0,
    val collectionViewed: Map<String, Int> = mapOf<String, Int>()
)

sealed class CollectionsResult(){
    object Loading: CollectionsResult()
    data class Error(val message: String): CollectionsResult()
    data class Success(val list: List<CollectionMovie>): CollectionsResult()
}