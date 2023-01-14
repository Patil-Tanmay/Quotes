package com.tanmay.quotes.utils

fun Any.getType(): GenreType {
    return when (this) {
        is String -> GenreType.GenreSTRING
        is List<*> -> GenreType.GenreList
        else -> GenreType.GenreUnknown
    }
}

enum class GenreType{
    GenreSTRING,
    GenreList,
    GenreUnknown
}