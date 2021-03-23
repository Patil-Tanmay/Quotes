package com.tanmay.quotes.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.tanmay.quotes.api.QuotesApi
import com.tanmay.quotes.data.QuotesData
import com.tanmay.quotes.data.QuotesPagingSource
import com.tanmay.quotes.db.SavedQuotesDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuotesRepository @Inject constructor(
    private val quotesApi: QuotesApi,
    private val savedQuotesDatabase: SavedQuotesDatabase
) {

    fun getAllQuotes() =
        Pager(
            config = PagingConfig(
                pageSize = 4,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { QuotesPagingSource(quotesApi, savedQuotesDatabase) }
        ).liveData

    suspend fun insertQuote(quote: QuotesData) =
        savedQuotesDatabase.QuotesDataDao().insertQuote(quote)

      suspend fun deleteQuote(quoteText: String) =
        savedQuotesDatabase.QuotesDataDao().deleteQuote(quoteText)

     fun getAllSavedQuotes()  =
        savedQuotesDatabase.QuotesDataDao().getSavedQuotes()


}