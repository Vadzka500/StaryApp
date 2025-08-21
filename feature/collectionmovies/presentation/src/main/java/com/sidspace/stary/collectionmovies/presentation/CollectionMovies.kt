package com.sidspace.stary.collectionmovies.presentation

import kotlinx.serialization.Serializable

@Serializable
data class CollectionMovies(val label: String, val slug: String)