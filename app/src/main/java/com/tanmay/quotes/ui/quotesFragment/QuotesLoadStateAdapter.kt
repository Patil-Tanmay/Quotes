package com.tanmay.quotes.ui.quotesFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tanmay.quotes.databinding.QuotesLoadFooterBinding

class QuotesLoadStateAdapter(private val retry : () -> Unit) :
    LoadStateAdapter<QuotesLoadStateAdapter.QuotesLoadStateViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): QuotesLoadStateViewHolder {
        val binding =
            QuotesLoadFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuotesLoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuotesLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class QuotesLoadStateViewHolder(private val binding: QuotesLoadFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState !is LoadState.Loading
                textViewError.isVisible = loadState !is LoadState.Loading
            }
        }
    }

}