package com.tanmay.quotes.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tanmay.quotes.data.QuotesData


@Database(
    entities = [QuotesData::class],
    version = 1
)
abstract class SavedQuotesDatabase : RoomDatabase(){

    abstract fun QuotesDataDao() : QuotesDataDao

}