package com.tanmay.quotes.ui.quotesFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.tanmay.quotes.api.QuotesApi
import com.tanmay.quotes.data.FetchedQuotesData
import com.tanmay.quotes.data.QuotesData
import com.tanmay.quotes.data.QuotesListing
import com.tanmay.quotes.data.models.GenreStatus
import com.tanmay.quotes.data.paging.QuotesDataSourceFactory
import com.tanmay.quotes.data.repository.QuotesRepository
import com.tanmay.quotes.db.QuotesDatabase
import com.tanmay.quotes.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuotesFragmentViewModel @Inject constructor(
    private val repository: QuotesRepository,
    private val api: QuotesApi,
    private val db: QuotesDatabase
) : ViewModel() {

//    private val _fetchedQuotes = MutableSharedFlow<PagedList<FetchedQuotesData>>()
//    val fetchedQuotes get() = _fetchedQuotes

    private val _quotesGenres = MutableStateFlow<Resource<List<GenreStatus>>>(Resource.Loading())
    val quotesGenres get() = _quotesGenres

    private var genreList: List<GenreStatus> = emptyList()
    private lateinit var quotesListing: QuotesListing

    private val _articles = MutableLiveData<PagedList<FetchedQuotesData>>()
    val articles: LiveData<PagedList<FetchedQuotesData>> get() = _articles

    init {
        viewModelScope.launch {
            _quotesGenres.emit(Resource.Loading())
        }
    }

    fun getQuotesGenres() = viewModelScope.launch {
        repository.getQuotesGenres().collect {
            _quotesGenres.emit(it)
            if (it is Resource.Success) {
                genreList = it.dataFetched
            }
        }
    }

    fun updateGenreStatus(genreName: String, position: Int?) {
        if (position != null) {
            genreList.map {
                it.isChecked = it.genre == genreName
            }
            viewModelScope.launch {
                _quotesGenres.emit(Resource.Success(genreList))
            }
        }
    }

    fun getQuotes(): QuotesListing {
        val sourceFactory = QuotesDataSourceFactory(
            scope = viewModelScope,
            category = "All",
            quotesApi = api,
            db = db
        )

        val pageConfig = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .setPrefetchDistance(10)
            .build()


        return QuotesListing(
            articles = sourceFactory.toLiveData(pageConfig),
            refreshState = sourceFactory.initLoadState,
            loadMoreState = sourceFactory.loadMoreState,
            onRefresh = { genreName, isRefresh ->
                if (isRefresh) {
                    sourceFactory.source?.invalidate()
                } else {
                    sourceFactory.genre = genreName ?: "All"
                    sourceFactory.source?.invalidate()
                }
            }
        )
    }

    fun updatePagedList(item: FetchedQuotesData){
        val pagedList = _articles.value
        if (pagedList?.contains(item) == true){
            pagedList[pagedList.indexOf(item)]?.isBookmarked = false
            _articles.value = pagedList!!
        }
    }

    fun setQuotesdata(list : PagedList<FetchedQuotesData>){
        _articles.value = list
    }


    private val mutableCopyQuote = MutableLiveData<String>()
    val copyQuote: LiveData<String> get() = mutableCopyQuote

    fun copyQuote(quoteText: String) {
        mutableCopyQuote.value = quoteText
    }

    fun isBookmarked(qData: QuotesData, fetchedQuote: FetchedQuotesData) {
        if (qData.isBookmarked == null) {
            qData.isBookmarked = true
            viewModelScope.launch {
                repository.insertSavedQuote(qData)

                repository.updateFetchedQuote(fetchedQuote.copy(isBookmarked = true))
            }
        } else {
            qData.isBookmarked = qData.isBookmarked == false
            if (qData.isBookmarked == false) {
                viewModelScope.launch {
                    repository.deleteSavedQuote(qData.quoteText)

                    repository.updateFetchedQuote(fetchedQuote.copy(isBookmarked = false))

                }
            } else {
                viewModelScope.launch {
                    repository.insertSavedQuote(qData)

                    repository.updateFetchedQuote(fetchedQuote.copy(isBookmarked = true))
                }
            }
        }
    }

}