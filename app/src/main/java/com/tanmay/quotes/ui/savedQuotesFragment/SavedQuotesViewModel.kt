package com.tanmay.quotes.ui.savedQuotesFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanmay.quotes.data.QuotesData
import com.tanmay.quotes.data.repository.QuotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedQuotesViewModel @Inject constructor(
    private val repository: QuotesRepository
):ViewModel(){


    fun getSavedQuotes() =
        repository.getAllSavedQuotes()


    fun deleteQuote(quote : QuotesData){
        viewModelScope.launch {
            repository.deleteQuote(quote.quoteText)
        }
    }
}