package com.sidspace.stary.domain.model

data class Collection(
    val id: String,
    val name: String,
    val slug:String,
    val category: String,
    val createdAt: String,
    val moviesCount: Int?,
    var viewedCount:Int
)
