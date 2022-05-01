package com.tanmay.quotes.data.repository

import androidx.paging.*
import com.tanmay.quotes.api.QuotesApi
import com.tanmay.quotes.data.FetchedQuotesData
import com.tanmay.quotes.data.QuotesData
import com.tanmay.quotes.data.QuotesRemoteMediator
import com.tanmay.quotes.data.models.QuotesGenres
import com.tanmay.quotes.db.QuotesDatabase
import com.tanmay.quotes.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuotesRepository @Inject constructor(
    private val quotesApi: QuotesApi,
    private val quotesDatabase: QuotesDatabase
) {

//    @ExperimentalPagingApi
//    fun getAllFetchedQuotes(tag: String, isRefresh: Boolean): Flow<PagingData<FetchedQuotesData>> =
//        Pager(
//            config = PagingConfig(pageSize = 4, maxSize = 300, enablePlaceholders = false),
//            remoteMediator = QuotesRemoteMediator(quotesApi, quotesDatabase, tag),
//            pagingSourceFactory = {
//                quotesDatabase.quotesDao().getAllFetchedQuotes(tag)
//            }
//        ).flow


    fun getQuotesGenres() = flow<Resource<QuotesGenres>> {
        try {
            val genres = quotesApi.getGenres()
            if (genres.statusCode == 200) {
                emit(Resource.Success(genres))
            } else {
                emit(Resource.Error(Throwable("Unable to Make Request")))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }


    suspend fun insertSavedQuote(quote: QuotesData) =
        quotesDatabase.quotesDao().insertQuote(quote)

    suspend fun deleteSavedQuote(quoteText: String) =
        quotesDatabase.quotesDao().deleteQuote(quoteText)

    fun getAllSavedQuotes() =
        quotesDatabase.quotesDao().getSavedQuotes()

    suspend fun updateFetchedQuote(quote: FetchedQuotesData) =
        quotesDatabase.quotesDao().updateFetchedQuote(quote)

}