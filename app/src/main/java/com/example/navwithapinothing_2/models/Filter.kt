package com.example.navwithapinothing_2.models

import com.example.navwithapinothing_2.features.screen.slider.listOfYears

class Filter {
    var listOfCollection: List<String>? = null
    var listOfType : List<String>? = null
    var year : List<String>? = listOf(listOfYears.random())
    var listOfGenres: List<String>? = null
}