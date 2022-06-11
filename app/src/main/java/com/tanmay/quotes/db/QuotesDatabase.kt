package com.tanmay.quotes.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tanmay.quotes.data.QuotesData
import com.tanmay.quotes.data.FetchedQuotesData

@Database(
    entities = [QuotesData::class,FetchedQuotesData::class],
    version = 1
)
abstract class QuotesDatabase : RoomDatabase(){

    abstract fun quotesDao() : QuotesDataDao

}