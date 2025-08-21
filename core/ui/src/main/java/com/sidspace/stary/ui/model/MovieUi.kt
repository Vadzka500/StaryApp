package com.sidspace.stary.ui.model


data class MovieUi(
    val id: Long,
    val name: String?,
    val enName: String?,
    val previewUrl: String?,
    val score: Double?,
    val isSeries: Boolean,
    val year: Int?,
    val countOfSeasons: Int,
    val releaseStart: Int?,
    val releaseEnd: Int?,
    val listOfGenres: List<String>?,

)