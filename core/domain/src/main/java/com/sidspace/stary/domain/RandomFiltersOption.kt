package com.sidspace.stary.domain


import java.util.Calendar


data class RandomFiltersOption(
    var listOfCollection: List<String>? = null,
    var listOfType: Map<String, String>? = null,
    var years: List<String>? = null,
    var listOfGenres: List<String>? = null,
    var listOfScore: List<String>? = null
) {


    companion object {
        val listOfTypes = mapOf<String, String>(Pair("Все",""),Pair("Фильмы","movie"), Pair("Сериалы", "tv-series"), Pair("Мультфильмы","cartoon"), Pair("Мультсериалы","animated-series"), Pair("Аниме","anime"))

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

        val listOfYears = listOf(
            "1874",
            Calendar.getInstance().get(Calendar.YEAR).toString()
        )

      /*  val listOfYears = listOf(
            "2020-" + Calendar.getInstance().get(Calendar.YEAR),
            "2010-2020",
            "2000-2010",
            "1990-2000",
            "1980-1990"
        )*/
       /* val listOfYearsOld = listOf("1970-1980", "1960-1970", "1950-1960", "1940-1950")
        val listOfYearsUi = listOf("За все время") + listOfYears + "До 1980"*/
    }
}