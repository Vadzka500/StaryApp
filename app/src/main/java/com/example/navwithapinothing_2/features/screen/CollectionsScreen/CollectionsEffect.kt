package com.example.navwithapinothing_2.features.screen.CollectionsScreen

/**
 * @Author: Vadim
 * @Date: 03.07.2025
 */

sealed class CollectionsEffect{
    data class OnSelectCollection(val name: String, val slug: String): CollectionsEffect()
    object OnBack: CollectionsEffect()
}