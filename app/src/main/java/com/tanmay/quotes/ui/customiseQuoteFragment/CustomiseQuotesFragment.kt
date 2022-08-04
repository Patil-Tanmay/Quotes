package com.tanmay.quotes.ui.customiseQuoteFragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tanmay.quotes.R
import com.tanmay.quotes.data.models.ColorPalleteModel
import com.tanmay.quotes.databinding.BottomsheetColorPickerBinding
import com.tanmay.quotes.databinding.FragmentCustomiseQuoteBinding
import com.tanmay.quotes.ui.detailedQuotes.DetailedQuotesFragment.Companion.QUOTETEXT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class CustomiseQuotesFragment : Fragment(R.layout.fragment_customise_quote) {

    private var _binding: FragmentCustomiseQuoteBinding? = null
    private val binding: FragmentCustomiseQuoteBinding get() = _binding!!

    private var quoteText: String? = null

    private lateinit var bottomSheetDialog: BottomSheetDialog

    private lateinit var bottomSheetBinding: BottomsheetColorPickerBinding

    private lateinit var colorAdapter: ColorAdapter

    private var colorList = arrayListOf<ColorPalleteModel>()

    private var backGroundColorPosition: Int = 0

    private var textColorPosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomiseQuoteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //apply colors
        colorAdapter = ColorAdapter({ textColor ->
            textColorPosition = colorList.map { it.color }.indexOf(textColor.color)
            binding.quoteText.setTextColor(ColorStateList.valueOf(textColor.color))
//            DrawableCompat.setTint(binding.quoteS.drawable, textColor.color)
//            DrawableCompat.setTint(binding.quoteE.drawable, textColor.color)
            binding.quoteE.setTextColor(ColorStateList.valueOf(textColor.color))
            binding.quoteS.setTextColor(ColorStateList.valueOf(textColor.color))

            bottomSheetDialog.dismiss()
        }, { backgroundColor ->
            backGroundColorPosition = colorList.map { it.color }.indexOf(backgroundColor.color)
            binding.imgLayout.backgroundTintList = ColorStateList.valueOf(backgroundColor.color)
            bottomSheetDialog.dismiss()
        })

        setUpColorList()
        setUpBottomSheetDialog()

        quoteText = arguments?.getString(QUOTETEXT)

        binding.quoteText.text = quoteText
        setUpOnClickListeners()
    }

    private fun setUpColorList() {
        val colorsTypedArray = resources.obtainTypedArray(R.array.default_colors)
        for (i in 0 until colorsTypedArray.length()) {
            colorList.add(ColorPalleteModel(colorsTypedArray.getColor(i, 0), false))
        }
        colorsTypedArray.recycle()
    }

    private fun setUpBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetBinding = BottomsheetColorPickerBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)

        setUpBottomSheetRecView()
    }

    private fun setUpBottomSheetRecView() {
//        colorAdapter.setColorList(colorList)
        bottomSheetBinding.recyclerViewColorPallete.layoutManager =
            GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
        bottomSheetBinding.recyclerViewColorPallete.adapter = colorAdapter
    }

    private fun setUpOnClickListeners() {
        binding.changeBackground.setOnClickListener {
            colorAdapter.setType(ColorSelectedType.Background)
            colorAdapter.setColorList(colorList, backGroundColorPosition)
            bottomSheetDialog.show()
        }

        binding.changeQuoteColor.setOnClickListener {
            colorAdapter.setType(ColorSelectedType.Text)
            colorAdapter.setColorList(colorList, textColorPosition)
            bottomSheetDialog.show()
        }

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val CUSTOMISEQUOTEFRAG = "CustomiseFrag"
    }

}

enum class ColorSelectedType {
    Background,
    Text
}