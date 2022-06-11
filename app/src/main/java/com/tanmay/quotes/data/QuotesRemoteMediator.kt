package com.tanmay.quotes.data

//import androidx.paging.ExperimentalPagingApi
//import androidx.paging.LoadType
//import androidx.paging.PagingState
//import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tanmay.quotes.api.Data
import com.tanmay.quotes.api.QuotesApi
import com.tanmay.quotes.db.QuotesDatabase
import retrofit2.HttpException
import java.io.IOException

private const val QUOTES_STARTING_PAGE_INDEX = 1


//@ExperimentalPagingApi
class QuotesRemoteMediator(
    val api : QuotesApi,
    private val quotesDatabase: QuotesDatabase,
    val tag: String){
//): RemoteMediator<Int,FetchedQuotesData>() {

//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, FetchedQuotesData>
//    ): MediatorResult {
//
//        val page = when (loadType) {
//            LoadType.REFRESH -> {
//                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
//                remoteKeys?.nextKey?.minus(1) ?: QUOTES_STARTING_PAGE_INDEX
//            }
//            LoadType.PREPEND -> {
//                val remoteKeys = getRemoteKeyForFirstItem(state)
//                // If the previous key is null, then the list is empty so we should wait for data
//                // fetched by remote refresh and can simply skip loading this time by returning
//                // `false` for endOfPaginationReached.
//                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = false)
//                prevKey
//            }
//            LoadType.APPEND -> {
//                val remoteKeys = getRemoteKeyForLastItem(state)
//                // If the next key is null, then the list is empty so we should wait for data
//                // fetched by remote refresh and can simply skip loading this time by returning
//                // `false` for endOfPaginationReached.
//                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = false)
//                nextKey
//            }
//        }
//
//        try {
//
//            val response = if (tag == "ALL") api.getQuotes(page, state.config.pageSize) else api.getQuotesByGenre(tag, page, state.config.pageSize)
//            val quotes = response.data
//            val isEndOfList = quotes.isEmpty()
//            quotesDatabase.withTransaction {
//                // clear all tables in the database
//                if (loadType == LoadType.REFRESH) {
//                    quotesDatabase.getRepoDao().clearRemoteKeys()
//                    quotesDatabase.quotesDao().deleteFetchedQuote()
//                }
//                val prevKey = if (page == QUOTES_STARTING_PAGE_INDEX) null else page - 1
//                val nextKey = if (isEndOfList) null else page + 1
//                val keys = quotes.map {
//                    RemoteKeys(repoId = it._id, prevKey = prevKey, nextKey = nextKey)
//                }
//
//                val savedQuotes = quotesDatabase.quotesDao().getAllSavedQuotes()
//                val fetchedQuotesData = quotes.map { qData ->
//
//                    if (checkQuoteBookMarked(qData, savedQuotes)) {
//                        FetchedQuotesData(
//                            _id = qData._id,
//                            quoteAuthor = qData.quoteAuthor,
//                            quoteGenre = qData.quoteGenre,
//                            quoteText = qData.quoteText,
//                            isBookmarked = true,
//                            TAG = tag
//                        )
//                    } else {
//                        FetchedQuotesData(
//                            _id = qData._id,
//                            quoteAuthor = qData.quoteAuthor,
//                            quoteGenre = qData.quoteGenre,
//                            quoteText = qData.quoteText,
//                            TAG = tag
//                        )
//                    }
//                }
//                quotesDatabase.getRepoDao().insertAll(keys)
//                quotesDatabase.quotesDao().insertFetchedQuote(fetchedQuotesData)
//            }
//            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
//        } catch (exception: IOException) {
//            return MediatorResult.Error(exception)
//        } catch (exception: HttpException) {
//            return MediatorResult.Error(exception)
//        }
//    }
//
//
//    private fun  checkQuoteBookMarked(qData : Data, saved :List<QuotesData>) : Boolean{
//        for (q in saved){
//            if (qData.quoteText == q.quoteText){
//                return true
//            }
//        }
//        return false
//    }
//
//
//    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, FetchedQuotesData>): RemoteKeys? {
//        // Get the last page that was retrieved, that contained items.
//        // From that last page, get the last item
//        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
//            ?.let { repo ->
//                // Get the remote keys of the last item retrieved
//                quotesDatabase.getRepoDao().remoteKeysQuotesId(repo._id)
//            }
//    }
//
//    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, FetchedQuotesData>): RemoteKeys? {
//        // Get the first page that was retrieved, that contained items.
//        // From that first page, get the first item
//        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
//            ?.let { repo ->
//                // Get the remote keys of the first items retrieved
//                quotesDatabase.getRepoDao().remoteKeysQuotesId(repo._id)
//            }
//    }
//
//    private suspend fun getRemoteKeyClosestToCurrentPosition(
//        state: PagingState<Int, FetchedQuotesData>
//    ): RemoteKeys? {
//        // The paging library is trying to load data after the anchor position
//        // Get the item closest to the anchor position
//        return state.anchorPosition?.let { position ->
//            state.closestItemToPosition(position)?._id?.let { repoId ->
//                quotesDatabase.getRepoDao().remoteKeysQuotesId(repoId)
//            }
//        }
//    }


}