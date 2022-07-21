package com.tanmay.quotes.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


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

fun QuotesData.toFetchedQuotes() : FetchedQuotesData{
    return FetchedQuotesData(
        id = this.id,
        _id = this._id,
        quoteAuthor = this.quoteAuthor,
        quoteGenre = this.quoteGenre,
        quoteText = this.quoteText,
        isBookmarked = this.isBookmarked
    )
}

@Entity(
    tableName = "fetched_quotes"
)
@Parcelize
data class FetchedQuotesData(

    @PrimaryKey(autoGenerate = true)
    val id: Int?=null,
    val _id: String,
    val quoteAuthor: String,
    val quoteGenre: String,
    val quoteText: String,
    var isBookmarked: Boolean?=null
): Parcelable