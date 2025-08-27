package com.sidspace.stary.person.presentation.screen

import com.sidspace.stary.ui.model.MovieUi
import com.sidspace.stary.ui.model.PersonUi
import com.sidspace.stary.ui.model.ResultData


data class PersonState(
    val listOfMovie: ResultData<List<MovieUi>> = ResultData.Loading,
    val listOfSerials: ResultData<List<MovieUi>> = ResultData.Loading,
    val person: PersonUi? = null
)
