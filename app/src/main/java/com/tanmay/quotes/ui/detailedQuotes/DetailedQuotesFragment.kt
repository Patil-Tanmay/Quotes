package com.tanmay.quotes.ui.detailedQuotes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tanmay.quotes.R
import com.tanmay.quotes.databinding.DetailQuotesBinding

class DetailedQuotesFragment : Fragment(R.layout.detail_quotes) {

    private var _binding: DetailQuotesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DetailQuotesBinding.bind(view)


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}