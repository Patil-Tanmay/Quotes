package com.tanmay.quotes.ui.quotesFragment

import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tanmay.quotes.data.FetchedQuotesData
import com.tanmay.quotes.data.QuotesData
import com.tanmay.quotes.data.repository.QuotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QuotesFragmentViewModel @Inject constructor(
    private val repository: QuotesRepository
) : ViewModel() {


 @ExperimentalPagingApi
 private val fetchedQuotes = repository.getAllFetchedQuotes().cachedIn(viewModelScope)


    @ExperimentalPagingApi
    fun getFetchedQuotes() : Flow<PagingData<FetchedQuotesData>> {
        return fetchedQuotes
    }


    private val mutableCopyQuote = MutableLiveData<String>()
    val copyQuote: LiveData<String> get() = mutableCopyQuote


    fun copyQuote(quoteText : String){
        mutableCopyQuote.value = quoteText
    }

    fun isBookmarked(qData: QuotesData, fetchedQuote : FetchedQuotesData) {
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