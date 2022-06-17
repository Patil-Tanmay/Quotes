package com.tanmay.quotes.ui.quotesFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tanmay.quotes.data.models.GenreStatus
import com.tanmay.quotes.databinding.GenreChipBinding

class GenreAdapter(
    private val onGenreClicked: (genre: String, position: Int?) -> Unit
) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    private var generes: List<GenreStatus> = emptyList()

    fun submitGenres(quotesGenre: List<GenreStatus>) {
        generes = quotesGenre
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

        fun bind(genreItem: GenreStatus) {
            binding.chipText.text = genreItem.genre

            binding.chipText.isChecked = genreItem.isChecked

            binding.chipText.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    Toast.makeText(binding.root.context, "$isChecked", Toast.LENGTH_SHORT).show()
//                    disableCheckedStatus(genre.genre)
                    onGenreClicked(genreItem.genre, generes.indexOf(genreItem))
//                    notifyDataSetChanged()
                } else {
                    Toast.makeText(binding.root.context, "$isChecked", Toast.LENGTH_SHORT).show()
                    onGenreClicked("All",null)
//                    disableCheckedStatus(null)
//                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun disableCheckedStatus(genre: String?) {
        if (genre == null) {
            generes.map {
                it.isChecked = false
            }
        } else {
            generes.map {
                it.isChecked = it.genre == genre
            }
        }
    }
}