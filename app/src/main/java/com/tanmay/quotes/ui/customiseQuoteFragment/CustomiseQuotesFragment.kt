package com.tanmay.quotes.ui.customiseQuoteFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tanmay.quotes.R
import com.tanmay.quotes.databinding.FragmentCustomiseQuoteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomiseQuotesFragment : Fragment(R.layout.fragment_customise_quote) {

    private var _binding : FragmentCustomiseQuoteBinding ? =null
    private val binding : FragmentCustomiseQuoteBinding get() = _binding!!

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

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}