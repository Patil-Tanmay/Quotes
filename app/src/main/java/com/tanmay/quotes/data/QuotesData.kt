package com.tanmay.quotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "SavedQuotes"
)
data class QuotesData(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val _id: String,
    val quoteAuthor: String,
    val quoteGenre: String,
    val quoteText: String,
    var isBookmarked: Boolean?=null
)


@Entity(
    tableName = "fetched_quotes"
)
data class FetchedQuotesData(

    @PrimaryKey(autoGenerate = true)
    val id: Int?=null,

    val _id: String,
    val quoteAuthor: String,
    val quoteGenre: String,
    val quoteText: String,
    var isBookmarked: Boolean?=null
)