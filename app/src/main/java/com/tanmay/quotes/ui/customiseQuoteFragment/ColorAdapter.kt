package com.tanmay.quotes.ui.customiseQuoteFragment

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.tanmay.quotes.R
import com.tanmay.quotes.data.models.ColorPalleteModel
import com.tanmay.quotes.databinding.ItemColorBinding

class ColorAdapter(
    private val onTextChangeColorClick : (ColorPalleteModel) -> Unit,
    private val onBackGroundChangeColorClick: (ColorPalleteModel) -> Unit
): RecyclerView.Adapter<ColorAdapter.ColorViewModel>() {

    private var colorPalleteModelList = arrayListOf<ColorPalleteModel>()

    private var type: ColorSelectedType = ColorSelectedType.Text

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewModel {
        return ColorViewModel(ItemColorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ColorViewModel, position: Int) {
        holder.bind(colorPalleteModelList[position])
    }

    override fun getItemCount(): Int {
        return colorPalleteModelList.size
    }

    fun setColorList(colorList : List<ColorPalleteModel>){
        colorPalleteModelList.addAll(colorList)
    }

    fun setType(colorType: ColorSelectedType){
        type = colorType
    }

    inner class ColorViewModel(val binding: ItemColorBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ColorPalleteModel){
            /**
             *  So to keep our backgraund resource intact while changing color
             *  following methods are used in which View Compact one provides
             *  backwards compatibility
             */
//            ViewCompat.setBackgroundTintList(binding.colorView, ColorStateList.valueOf(item.color))
            binding.colorView.backgroundTintList = ColorStateList.valueOf(item.color)

            binding.root.setOnClickListener {
                when(type){
                    ColorSelectedType.Text -> {
                        onTextChangeColorClick(item)
                        binding.selectedColor.visibility = View.VISIBLE
                    }

                    ColorSelectedType.Background -> {
                        onBackGroundChangeColorClick(item)
                        binding.selectedColor.visibility = View.VISIBLE
                    }
                }
            }

        }
    }
}