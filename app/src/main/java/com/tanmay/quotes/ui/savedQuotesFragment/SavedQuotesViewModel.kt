package com.tanmay.quotes.ui.savedQuotesFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanmay.quotes.data.FetchedQuotesData
import com.tanmay.quotes.data.QuotesData
import com.tanmay.quotes.data.repository.QuotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SavedQuotesViewModel @Inject constructor(
    private val repository: QuotesRepository
) : ViewModel() {

    fun getSavedQuotes(): LiveData<List<QuotesData>> {
        return repository.getAllSavedQuotes()
    }

    fun deleteQuote(quote: QuotesData) {
        viewModelScope.launch {
            repository.deleteSavedQuote(quote.quoteText)

            val fetchedQuote = FetchedQuotesData(
                id = quote.id,
                _id = quote._id,
                quoteAuthor = quote.quoteAuthor,
                quoteText = quote.quoteText,
                quoteGenre = quote.quoteGenre,
                isBookmarked = false
            )
            
            repository.updateFetchedQuote(fetchedQuote)
        }
    }

    private val mutableCopyQuote = MutableLiveData<String>()
    val copyQuote: LiveData<String> get() = mutableCopyQuote


    fun copyQuote(quoteText : String){
        mutableCopyQuote.value = quoteText
    }

}