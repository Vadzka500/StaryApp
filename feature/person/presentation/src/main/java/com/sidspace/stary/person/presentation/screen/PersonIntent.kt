package com.sidspace.stary.person.presentation.screen


sealed interface PersonIntent {
    data class OnSelectMovie(val id: Long): PersonIntent
    data class LoadPersonData(val id: Long): PersonIntent
    object OnError: PersonIntent
}