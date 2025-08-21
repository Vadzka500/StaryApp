package com.sidspace.stary.search.presentation.screen


sealed interface SearchIntent {
    data class UpdateSearchStr(val str: String): SearchIntent
    data class OnSelectMovie(val id: Long): SearchIntent
    data class IsShowFilter(val isShow: Boolean): SearchIntent
    object SetGridViewMode: SearchIntent
    object SetListViewMode: SearchIntent
    object OnError: SearchIntent
}