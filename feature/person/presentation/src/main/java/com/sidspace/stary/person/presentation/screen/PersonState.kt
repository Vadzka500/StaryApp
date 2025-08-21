package com.sidspace.stary.person.presentation.screen

import com.example.ui.model.MovieData
import com.example.ui.model.PersonUi
import com.example.ui.model.ResultData



data class PersonState(
    val listOfMovie: ResultData<List<MovieData>> = ResultData.Loading,
    val listOfSerials: ResultData<List<MovieData>> = ResultData.Loading,
    val person: PersonUi? = null
)
