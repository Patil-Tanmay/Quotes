package com.tanmay.quotes.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tanmay.quotes.api.Data
import com.tanmay.quotes.api.QuotesApi
import com.tanmay.quotes.db.QuotesDatabase
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

private const val QUOTES_STARTING_PAGE_INDEX = 1


@ExperimentalPagingApi
class QuotesRemoteMediator(
    val api : QuotesApi,
    private val quotesDatabase: QuotesDatabase
): RemoteMediator<Int,FetchedQuotesData>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FetchedQuotesData>
    ): MediatorResult {

        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {
            val response = api.getQuotes(page, state.config.pageSize)
            val quotes = response.data
            val isEndOfList = quotes.isEmpty()
            quotesDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    quotesDatabase.getRepoDao().clearRemoteKeys()
                    quotesDatabase.quotesDao().deleteFetchedQuote()
                }
                val prevKey = if (page == QUOTES_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = quotes.map {
                    RemoteKeys(repoId = it._id, prevKey = prevKey, nextKey = nextKey)
                }

                val savedQuotes = quotesDatabase.quotesDao().getAllSavedQuotes()
                val fetchedQuotesData = quotes.map { qData ->

                    if (checkQuoteBookMarked(qData, savedQuotes)) {
                        FetchedQuotesData(
                            _id = qData._id,
                            quoteAuthor = qData.quoteAuthor,
                            quoteGenre = qData.quoteGenre,
                            quoteText = qData.quoteText,
                            isBookmarked = true
                        )
                    } else {
                        FetchedQuotesData(
                            _id = qData._id,
                            quoteAuthor = qData.quoteAuthor,
                            quoteGenre = qData.quoteGenre,
                            quoteText = qData.quoteText
                        )
                    }
                }
                quotesDatabase.getRepoDao().insertAll(keys)
                quotesDatabase.quotesDao().insertFetchedQuote(fetchedQuotesData)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
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


    /**
     * this returns the page key or the final end of list success result
     */
    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, FetchedQuotesData>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: QUOTES_STARTING_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                    ?: throw InvalidObjectException("Remote key should not be null for $loadType")
                remoteKeys.nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                    ?: throw InvalidObjectException("Invalid state, key should not be null")
                //end of list condition reached
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
        }
    }


private suspend fun getFirstRemoteKey(state: PagingState<Int, FetchedQuotesData>): RemoteKeys? {
    return state.pages
        .firstOrNull {
            it.data.isNotEmpty()
        }
        ?.data?.firstOrNull()
        ?.let {
            quotesDatabase.getRepoDao().remoteKeysQuotesId(it._id)
        }
    }

    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, FetchedQuotesData>): RemoteKeys? {
        return state.pages
            .lastOrNull {
                it.data.isNotEmpty()
            }
            ?.data?.lastOrNull()
            ?.let{
                quotesDatabase.getRepoDao().remoteKeysQuotesId(it._id)
            }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, FetchedQuotesData>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?._id?.let {
                quotesDatabase.getRepoDao().remoteKeysQuotesId(it)
            }
        }
    }

}