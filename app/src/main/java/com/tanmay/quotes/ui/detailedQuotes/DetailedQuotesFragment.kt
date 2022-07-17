package com.tanmay.quotes.ui.detailedQuotes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import com.tanmay.quotes.R
import com.tanmay.quotes.databinding.DetailQuotesBinding

class DetailedQuotesFragment : Fragment(R.layout.detail_quotes) {

    private var _binding: DetailQuotesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DetailQuotesBinding.bind(view)

        val args = this.arguments
        binding.quoteText.text =  args?.getString("QuoteText")

//        binding.imgLayout.drawToBitmap()

        binding.icShare.setOnClickListener {
            ShareQuoteFragment().show(
                childFragmentManager,
                "BottomSheetFrag"
            )
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}