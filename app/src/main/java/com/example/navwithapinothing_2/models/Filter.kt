package com.example.navwithapinothing_2.models

import com.example.navwithapinothing_2.utils.RandomFiltersOption.Companion.listOfYears


class Filter {
    var listOfCollection: List<String>? = null
    var listOfType : List<String>? = null
    var year : List<String>? = listOfYears
    var listOfGenres: List<String>? = null
}