package com.sidspace.stary.ui.enum


enum class SortDirection{
    ASCENDING,
    DESCENDING
}

fun SortDirection.toggle(): SortDirection {
    return if (this == SortDirection.DESCENDING) SortDirection.ASCENDING else SortDirection.DESCENDING
}