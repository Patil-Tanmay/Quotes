package com.tanmay.quotes.ui.customiseQuoteFragment

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tanmay.quotes.data.models.ColorPalleteModel
import com.tanmay.quotes.databinding.ItemColorBinding

class ColorAdapter(
    private val onTextChangeColorClick : (ColorPalleteModel) -> Unit,
    private val onBackGroundChangeColorClick: (ColorPalleteModel) -> Unit
): RecyclerView.Adapter<ColorAdapter.ColorViewModel>() {

    private var colorPalleteModelList = arrayListOf<ColorPalleteModel>()

    private var currentPosition : Int =0

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

    fun setColorList(colorList : List<ColorPalleteModel>, position: Int){
        colorPalleteModelList.clear()
        this.currentPosition = position
        colorList[currentPosition].isChecked = true
        colorPalleteModelList.addAll(colorList)
        notifyDataSetChanged()
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

            if (adapterPosition == currentPosition){
                binding.selectedColor.visibility = View.VISIBLE
            }else{
                binding.selectedColor.visibility = View.GONE
            }

            binding.root.setOnClickListener {

                //inline function
                fun notifySelectedItem(){
                    if (colorPalleteModelList.indexOf(item)!=currentPosition){
                        binding.selectedColor.visibility = View.VISIBLE
                        colorPalleteModelList[currentPosition].isChecked = false
                        notifyItemChanged(currentPosition)
                    }
                }

                when(type){
                    ColorSelectedType.Text -> {
                        onTextChangeColorClick(item)
//                        notifySelectedItem()
//                        binding.selectedColor.visibility = View.VISIBLE
                    }

                    ColorSelectedType.Background -> {
                        onBackGroundChangeColorClick(item)
//                        notifySelectedItem()
//                        binding.selectedColor.visibility = View.VISIBLE
                    }
                }
            }

        }
    }
}