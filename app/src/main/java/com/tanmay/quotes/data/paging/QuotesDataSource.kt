package com.tanmay.quotes.data.paging

import androidx.paging.PageKeyedDataSource
import androidx.room.withTransaction
import com.tanmay.quotes.api.Data
import com.tanmay.quotes.api.QuotesApi
import com.tanmay.quotes.data.FetchedQuotesData
import com.tanmay.quotes.data.QuotesData
import com.tanmay.quotes.db.QuotesDatabase
import com.tanmay.quotes.utils.GenreType
import com.tanmay.quotes.utils.NetworkState
import com.tanmay.quotes.utils.getType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class QuotesDataSource(
    private val scope: CoroutineScope,
    private val initLoadState: MutableStateFlow<NetworkState>,
    private val loadMoreState: MutableStateFlow<NetworkState>,
    private val category: String,
    private val db: QuotesDatabase,
    private val quotesApi: QuotesApi
) : PageKeyedDataSource<Int, FetchedQuotesData>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, FetchedQuotesData>
    ) {
        scope.launch {
            initLoadState.emit(NetworkState.LOADING)
            try {
                    val quotes = if (category == "All") {
                        quotesApi.getQuotes(
                            page = 1,
                            limit = 50
                        )
                    } else {
                        quotesApi.getQuotesByGenre(
                            tag = category,
                            page = 1,
                            limit = 50
                        )
                    }

                    if (quotes.statusCode != 200) throw IllegalStateException("Cannot be empty")

                    val savedQuotes = db.quotesDao().getAllSavedQuotes()
                    val fetchedQuotesData = quotes.data.map { qData ->

                        if (checkQuoteBookMarked(qData, savedQuotes)) {
                            FetchedQuotesData(
                                _id = qData._id,
                                quoteAuthor = qData.quoteAuthor,
                                quoteGenre = checkItISArrayOrNot(qData.quoteGenre),
                                quoteText = qData.quoteText,
                                isBookmarked = true
                            )
                        } else {
                            FetchedQuotesData(
                                _id = qData._id,
                                quoteAuthor = qData.quoteAuthor,
                                quoteGenre = checkItISArrayOrNot(qData.quoteGenre),
                                quoteText = qData.quoteText
                            )
                        }
                    }
                    callback.onResult(fetchedQuotesData, null, 2)
                    initLoadState.emit(NetworkState.IDLE)
            } catch (e: Exception) {
                initLoadState.emit(NetworkState.ERROR)
            }
        }
    }

    private fun checkQuoteBookMarked(qData: Data, saved: List<QuotesData>): Boolean {
        for (q in saved) {
            if (qData.quoteText == q.quoteText) {
                return true
            }
        }
        return false
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, FetchedQuotesData>
    ) {
        scope.launch {
            if (loadMoreState.value == NetworkState.LOADING_NEXT_PAGE) return@launch
            initLoadState.emit(NetworkState.LOADING_NEXT_PAGE)
            try {
                    val quotes = if (category == "All") {
                        quotesApi.getQuotes(
                            page = params.key,
                            limit = 50
                        )
                    } else {
                        quotesApi.getQuotesByGenre(
                            tag = category,
                            page = params.key,
                            limit = 50
                        )
                    }
                    if (quotes.statusCode != 200) throw IllegalStateException("Cannot be empty")

                    val savedQuotes = db.quotesDao().getAllSavedQuotes()
                    val fetchedQuotesData = quotes.data.map { qData ->

                        if (checkQuoteBookMarked(qData, savedQuotes)) {
                            FetchedQuotesData(
                                _id = qData._id,
                                quoteAuthor = qData.quoteAuthor,
                                quoteGenre = checkItISArrayOrNot(qData.quoteGenre),
                                quoteText = qData.quoteText,
                                isBookmarked = true
                            )
                        } else {
                            FetchedQuotesData(
                                _id = qData._id,
                                quoteAuthor = qData.quoteAuthor,
                                quoteGenre = checkItISArrayOrNot(qData.quoteGenre),
                                quoteText = qData.quoteText
                            )
                        }
                    }
                    val nextPageKey = quotes.pagination.nextPage
                    callback.onResult(fetchedQuotesData, nextPageKey)
                    initLoadState.emit(NetworkState.IDLE)
            } catch (e: Exception) {
                initLoadState.emit(NetworkState.ERROR)
            }
        }
    }

    private fun checkItISArrayOrNot(quoteGenre : Any) : String{
        return if (quoteGenre.getType() == GenreType.GenreList){
            if ((quoteGenre as List<*>)[0].toString() == "") "all" else (quoteGenre as List<*>)[0].toString()
        }else{
            quoteGenre as String
        }
    }

        override fun loadBefore(
            params: LoadParams<Int>,
            callback: LoadCallback<Int, FetchedQuotesData>
        ) {
            // we don't need this
        }
}