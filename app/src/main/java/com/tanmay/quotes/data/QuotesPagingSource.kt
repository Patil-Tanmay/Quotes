package com.tanmay.quotes.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tanmay.quotes.api.Data
import com.tanmay.quotes.api.QuotesApi
import com.tanmay.quotes.db.SavedQuotesDatabase
import retrofit2.HttpException
import java.io.IOException


private const val QUOTES_STARTING_PAGE_INDEX = 1

class QuotesPagingSource(
    private val quotesApi: QuotesApi,
    private val savedQuotesDatabase: SavedQuotesDatabase
) : PagingSource<Int, QuotesData>() {

    override fun getRefreshKey(state: PagingState<Int, QuotesData>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, QuotesData> {
        val position = params.key?: QUOTES_STARTING_PAGE_INDEX

        return try {
            val response = quotesApi.getQuotes(position,params.loadSize)
            val quotes = response.data

            val savedQuotes = savedQuotesDatabase.QuotesDataDao().getAllSavedQuotes()


            val quotesData = quotes.map { qData ->

                    if (checkQuoteBookMarked(qData,savedQuotes)) {
                        QuotesData(
                            _id = qData._id,
                            quoteAuthor = qData.quoteAuthor,
                            quoteGenre = qData.quoteGenre,
                            quoteText = qData.quoteText,
                            isBookmarked = true
                        )
                    } else {
                        QuotesData(
                    _id = qData._id,
                    quoteAuthor = qData.quoteAuthor,
                    quoteGenre = qData.quoteGenre,
                    quoteText = qData.quoteText
                )
            }
            }


            LoadResult.Page(
                data = quotesData,
                prevKey = if (position == QUOTES_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (quotes.isEmpty()) null else position + 1
            )
        }catch (exception:IOException){
            LoadResult.Error(exception)
        }catch (exception : HttpException){
            LoadResult.Error(exception)
        }
    }

    private fun  checkQuoteBookMarked(qData : Data, saved :List<QuotesData>) : Boolean{
        for (q in saved){
            if (qData.quoteText == q.quoteText){
                return true
            }
        }
        return false
    }

}