package com.sidspace.stary.person.presentation.screen

import com.sidspace.stary.ui.model.MovieData
import com.sidspace.stary.ui.model.PersonUi
import com.sidspace.stary.ui.model.ResultData


data class PersonState(
    val listOfMovie: ResultData<List<MovieData>> = ResultData.Loading,
    val listOfSerials: ResultData<List<MovieData>> = ResultData.Loading,
    val person: PersonUi? = null
)
