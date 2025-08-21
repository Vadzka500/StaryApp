package com.sidspace.stary.data.model.api.movie

import kotlinx.serialization.Serializable

@Serializable
data class Fees(
    val russia: Russia? = null,
    val usa: Usa? = null,
    val world: World? = null
)