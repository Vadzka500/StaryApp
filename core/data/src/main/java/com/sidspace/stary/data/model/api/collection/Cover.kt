package com.sidspace.stary.data.model.api.collection

import kotlinx.serialization.Serializable

@Serializable
data class Cover(
    val previewUrl: String? = null,
    val url: String? = null
)
