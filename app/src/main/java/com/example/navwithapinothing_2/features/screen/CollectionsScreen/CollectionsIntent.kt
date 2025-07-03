package com.example.navwithapinothing_2.features.screen.CollectionsScreen

/**
 * @Author: Vadim
 * @Date: 03.07.2025
 */
sealed interface CollectionsIntent {
    data class OnSelectCollection(val name: String, val slug: String): CollectionsIntent
    object OnBack: CollectionsIntent
}