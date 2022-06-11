package com.tanmay.quotes.ui.quotesFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tanmay.quotes.data.models.QuotesGenres
import com.tanmay.quotes.databinding.GenreChipBinding

class GenreAdapter : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    private var generes: List<String> = emptyList()

    fun submitGenres(quotesGenre: QuotesGenres){
        generes = quotesGenre.data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(
            GenreChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(generes[position])
    }

    override fun getItemCount(): Int {
        return generes.size
    }

    inner class GenreViewHolder(val binding: GenreChipBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: String) {
            binding.chipText.text = genre
        }
    }
}