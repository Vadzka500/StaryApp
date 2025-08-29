package com.sidspace.stary.random.presentation.screen


sealed interface RandomIntent {
    data object Random : RandomIntent
    data class UpdateCurrentPage(val currentPage: Int, val currentPageOffSetFraction: Float) :
        RandomIntent

    data class IsSearch(val isSearch: Boolean) : RandomIntent
    data class SetShowFilters(val isShown: Boolean) : RandomIntent

    sealed interface FilterIntent : RandomIntent {
        object ClearTypes : FilterIntent
        object ClearGenres : FilterIntent

        data class AddType(val type: Pair<String, String>) : FilterIntent
        data class RemoveType(val type: String) : FilterIntent

        data class AddGenre(val genre: String) : FilterIntent
        data class RemoveGenre(val genre: String) : FilterIntent

        data class SetYears(val listOfYears: List<String>?) : FilterIntent
        data class SetScore(val listOfScore: List<String>?) : FilterIntent

        data class ToggleCollection(val slug: String) : FilterIntent
    }
}
