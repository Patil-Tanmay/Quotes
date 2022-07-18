package com.tanmay.quotes.ui.detailedQuotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

import javax.inject.Inject

class DetailedQuotesViewModel @Inject constructor(): ViewModel() {

    private val _shareQuote = Channel<ShareQuoteType>()
    val shareQuote get() = _shareQuote.receiveAsFlow()

    fun setShareQuoteType(type: ShareQuoteType){
        viewModelScope.launch {
            _shareQuote.send(type)
        }
    }


}
enum class ShareQuoteType{
    IMAGE,
    TEXT
}