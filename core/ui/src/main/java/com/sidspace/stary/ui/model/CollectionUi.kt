package com.sidspace.stary.ui.model

data class CollectionUi(
    val id: String,
    val name: String,
    val slug:String,
    val category: String,
    val createdAt: String,
    val moviesCount: Int?,
    val viewedCount:Int
)
