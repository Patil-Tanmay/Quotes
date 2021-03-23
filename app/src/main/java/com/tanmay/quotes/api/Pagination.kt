package com.tanmay.quotes.api

data class Pagination(
    val currentPage: Int,
    val nextPage: Int,
    val totalPages: Int
)