package com.tanmay.quotes.ui.quotesFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tanmay.quotes.R
import com.tanmay.quotes.data.FetchedQuotesData
import com.tanmay.quotes.databinding.ItemQuoteBinding
import kotlinx.coroutines.launch

class QuotesAdapter(
    val onBookMarkClick: (FetchedQuotesData) -> Unit,
    val onCopyClick : (String) -> Unit,
    val onRootClick : (String) -> Unit
) : PagedListAdapter<FetchedQuotesData, QuotesAdapter.QuotesViewHolder>(QUOTE_COMPARATOR) {


    override fun onBindViewHolder(holder: QuotesViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotesViewHolder {
        val binding = ItemQuoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuotesViewHolder(
            binding,
            onBookMarkClick = { position ->
                val qData = getItem(position)
                if (qData != null) {
                    onBookMarkClick(qData)
                }
            },
            onCopyClick = { position ->
                val qData = getItem(position)
                if (qData != null){
                    onCopyClick(qData.quoteText)
                }
            })
    }

    inner class QuotesViewHolder(
        private val binding: ItemQuoteBinding,
        val onBookMarkClick: (Int) -> Unit,
        val onCopyClick : (Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: FetchedQuotesData) {
            binding.apply {
                quoteText.text = data.quoteText

                savedQuoteEmpty.setImageResource(
                    if (data.isBookmarked == true) {
                        R.drawable.ic_favourite_quote_filled
                    } else {
                        R.drawable.ic_favourite_quote_empty
                    }
                )
                savedQuoteEmpty.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onBookMarkClick(position)
                        bookmarkClickImageChange(position)
                        notifyItemChanged(position)
                    }
                }

                binding.root.setOnClickListener {
                    onRootClick(data.quoteText)
                }

                copyText.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onCopyClick(position)
                    }
                }
            }
        }
    }

    private fun bookmarkClickImageChange(position: Int){
        val qData = getItem(position)
        if (qData?.isBookmarked == null) {
            qData?.isBookmarked = true
        } else {
            qData.isBookmarked = qData.isBookmarked == false
        }
    }

    //to compare the data using diffUtil
    companion object {
        private val QUOTE_COMPARATOR = object : DiffUtil.ItemCallback<FetchedQuotesData>() {
            override fun areItemsTheSame(oldItem: FetchedQuotesData, newItem: FetchedQuotesData): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: FetchedQuotesData, newItem: FetchedQuotesData): Boolean {
                return oldItem == newItem
            }
        }
    }
}