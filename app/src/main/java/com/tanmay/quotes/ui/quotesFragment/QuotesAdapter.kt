package com.tanmay.quotes.ui.quotesFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tanmay.quotes.R
import com.tanmay.quotes.data.QuotesData
import com.tanmay.quotes.databinding.ItemQuoteBinding

class QuotesAdapter(
    val onBookMarkClick: (QuotesData) -> Unit,
) : PagingDataAdapter<QuotesData, QuotesAdapter.QuotesViewHolder>(QUOTE_COMPARATOR) {


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
            })
    }

    inner class QuotesViewHolder(
        private val binding: ItemQuoteBinding,
        val onBookMarkClick: (Int) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: QuotesData) {
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
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onBookMarkClick(position)
                        notifyItemChanged(position)
                    }
                }
            }
        }


    }

    //to compare the data using diffUtil
    companion object {
        private val QUOTE_COMPARATOR = object : DiffUtil.ItemCallback<QuotesData>() {
            override fun areItemsTheSame(oldItem: QuotesData, newItem: QuotesData): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: QuotesData, newItem: QuotesData): Boolean {
                return oldItem == newItem
            }
        }
    }
}