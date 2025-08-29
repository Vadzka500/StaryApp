package com.sidspace.stary.collections.presentation.screen

sealed interface CollectionsIntent {
    data class OnSelectCollection(val name: String, val slug: String) : CollectionsIntent
    object OnBack : CollectionsIntent
    object OnError : CollectionsIntent
}
