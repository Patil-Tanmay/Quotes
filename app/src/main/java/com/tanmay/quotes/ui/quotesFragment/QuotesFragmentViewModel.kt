package com.tanmay.quotes.ui.quotesFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tanmay.quotes.data.QuotesData
import com.tanmay.quotes.data.repository.QuotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QuotesFragmentViewModel @Inject constructor(
    private val repository: QuotesRepository
) : ViewModel() {

    private val quotes = repository.getAllQuotes().cachedIn(viewModelScope)

    fun getQuotes(): LiveData<PagingData<QuotesData>> {
        return quotes
    }


    fun isBookmarked(qData: QuotesData) {
        if (qData.isBookmarked == null) {
            qData.isBookmarked = true
            viewModelScope.launch {
                repository.insertQuote(qData)
            }
        } else {
            qData.isBookmarked = qData.isBookmarked == false
            if (qData.isBookmarked == false) {
                viewModelScope.launch {
                    repository.deleteQuote(qData.quoteText)
                }
            } else {
                viewModelScope.launch {
                    repository.insertQuote(qData)
                }
            }
        }
    }

}