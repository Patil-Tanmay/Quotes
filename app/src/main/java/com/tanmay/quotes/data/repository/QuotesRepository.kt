package com.tanmay.quotes.data.repository

import androidx.paging.*
import com.tanmay.quotes.api.QuotesApi
import com.tanmay.quotes.data.FetchedQuotesData
import com.tanmay.quotes.data.QuotesData
import com.tanmay.quotes.data.QuotesRemoteMediator
import com.tanmay.quotes.db.QuotesDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuotesRepository @Inject constructor(
    private val quotesApi: QuotesApi,
    private val quotesDatabase: QuotesDatabase
) {

    @ExperimentalPagingApi
    fun getAllFetchedQuotes(): Flow<PagingData<FetchedQuotesData>> =
        Pager(
            config = PagingConfig(pageSize = 4, maxSize = 300, enablePlaceholders = false),
            remoteMediator = QuotesRemoteMediator(quotesApi, quotesDatabase),
            pagingSourceFactory = { quotesDatabase.quotesDao().getAllFetchedQuotes() }
        ).flow


    suspend fun insertSavedQuote(quote: QuotesData) =
        quotesDatabase.quotesDao().insertQuote(quote)

    suspend fun deleteSavedQuote(quoteText: String) =
        quotesDatabase.quotesDao().deleteQuote(quoteText)

    fun getAllSavedQuotes() =
        quotesDatabase.quotesDao().getSavedQuotes()

    suspend fun updateFetchedQuote(quote: FetchedQuotesData) =
        quotesDatabase.quotesDao().updateFetchedQuote(quote)

}