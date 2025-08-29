package com.sidspace.stary.domain

data class RandomFiltersOption(
    var listOfCollection: List<String>? = null,
    var listOfType: Map<String, String>? = null,
    var years: List<String>? = null,
    var listOfGenres: List<String>? = null,
    var listOfScore: List<String>? = null
) {

    fun hasActiveFilters(): Boolean =
        listOf(listOfType, listOfGenres, listOfCollection, listOfScore, years).any { it != null }

    companion object {
        val listOfTypes = mapOf<String, String>(
            Pair("Все", ""),
            Pair("Фильмы", "movie"),
            Pair("Сериалы", "tv-series"),
            Pair("Мультфильмы", "cartoon"),
            Pair("Мультсериалы", "animated-series"),
            Pair("Аниме", "anime")
        )

        val listOfGenres = listOf(
            "Любой жанр",
            "Комедия",
            "Ужасы",
            "Триллер",
            "Детектив",
            "Боевик",
            "Вестерн",
            "Драма",
            "Мелодрама",
            "Приключения",
            "Фантастика",
            "Криминал",
            "Биография",
            "Военный",
            "Документальный"
        )
    }
}
