package com.tanmay.quotes.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.tanmay.quotes.utils.NetworkState
import kotlinx.coroutines.flow.StateFlow

data class QuotesListing(
    val articles: LiveData<PagedList<FetchedQuotesData>>,
    val refreshState: StateFlow<NetworkState>,
    val loadMoreState: StateFlow<NetworkState>,
    val onRefresh: (genreNme: String?, isRefresh: Boolean) -> Unit
)
