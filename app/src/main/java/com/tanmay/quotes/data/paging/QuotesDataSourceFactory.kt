package com.tanmay.quotes.data.paging

import androidx.paging.DataSource
import com.tanmay.quotes.api.QuotesApi
import com.tanmay.quotes.data.FetchedQuotesData
import com.tanmay.quotes.db.QuotesDatabase
import com.tanmay.quotes.utils.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

class QuotesDataSourceFactory(
    private val scope: CoroutineScope,
    private val category: String,
    private val quotesApi: QuotesApi,
    private val db: QuotesDatabase
) : DataSource.Factory<Int, FetchedQuotesData>() {

    val initLoadState = MutableStateFlow(NetworkState.IDLE)
    val loadMoreState = MutableStateFlow(NetworkState.IDLE)

    var source: QuotesDataSource? = null
        private set

    var genre= category

    override fun create(): DataSource<Int, FetchedQuotesData> {
        val genreInternal = genre
        val quotesDataSource = QuotesDataSource(
            scope = scope,
            category = genreInternal,
            quotesApi = quotesApi,
            db = db,
            initLoadState = initLoadState,
            loadMoreState = loadMoreState
        )
        source = quotesDataSource
        return quotesDataSource
    }

}