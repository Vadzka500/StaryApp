package com.sidspace.stary.person.presentation.screen


sealed class PersonEffect {
    data class OnSelectMovie(val id: Long): PersonEffect()
    object ToErrorScreen: PersonEffect()
}