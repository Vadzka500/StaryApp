package com.example.navwithapinothing_2.models.collection

import kotlinx.serialization.Serializable

@Serializable
data class Cover(
    val previewUrl: String? = null,
    val url: String? = null
)