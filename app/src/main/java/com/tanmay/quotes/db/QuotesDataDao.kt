package com.tanmay.quotes.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tanmay.quotes.data.QuotesData

@Dao
interface QuotesDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote : QuotesData) : Long

    @Query("SELECT * FROM SavedQuotes")
    suspend fun getAllSavedQuotes() :List<QuotesData>


    @Query("DELETE FROM SavedQuotes WHERE quoteText = :quote")
    suspend fun deleteQuote(quote : String)

    @Query("SELECT * FROM SavedQuotes")
     fun getSavedQuotes() : LiveData<List<QuotesData>>



}