package com.tanmay.quotes.ui.customiseQuoteFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tanmay.quotes.R
import com.tanmay.quotes.databinding.BottomsheetColorPickerBinding
import com.tanmay.quotes.databinding.FragmentCustomiseQuoteBinding
import com.tanmay.quotes.ui.detailedQuotes.DetailedQuotesFragment.Companion.QUOTETEXT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomiseQuotesFragment : Fragment(R.layout.fragment_customise_quote) {

    private var _binding : FragmentCustomiseQuoteBinding ? =null
    private val binding : FragmentCustomiseQuoteBinding get() = _binding!!

    private var quoteText: String?= null

    private lateinit var bottomSheetDialog: BottomSheetDialog

    private lateinit var bottomSheetBinding: BottomsheetColorPickerBinding



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
        setUpBottomSheetDialog()

        val listOfColors = resources.getStringArray(R.array.default_colors)

        quoteText = arguments?.getString(QUOTETEXT)

        binding.quoteText.text = quoteText
        setUpOnClickListeners()
    }

    private fun setUpBottomSheetDialog(){
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetBinding = BottomsheetColorPickerBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)

        setUpBottomSheetRecView()
    }

    private fun setUpBottomSheetRecView(){

    }

    private fun setUpOnClickListeners(){
        binding.changeBackground.setOnClickListener {

        }

        binding.changeQuoteColor.setOnClickListener {
            bottomSheetDialog.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}