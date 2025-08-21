package com.sidspace.stary.collections.presentation.screen


sealed class CollectionsEffect{
    data class OnSelectCollection(val name: String, val slug: String): CollectionsEffect()
    object OnBack: CollectionsEffect()
    object ToErorrScreen: CollectionsEffect()
}