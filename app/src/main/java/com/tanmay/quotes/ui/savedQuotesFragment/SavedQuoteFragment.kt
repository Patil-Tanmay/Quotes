package com.tanmay.quotes.ui.savedQuotesFragment


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tanmay.quotes.R
import com.tanmay.quotes.data.toFetchedQuotes
import com.tanmay.quotes.databinding.FragmentSavedQuoteBinding
import com.tanmay.quotes.ui.quotesFragment.QuotesFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class SavedQuoteFragment : Fragment(R.layout.fragment_saved_quote) {

//    private val TAG = "savedQuote"

    private lateinit var adapter: SavedQuotesAdapter

    private var _binding: FragmentSavedQuoteBinding? = null
    private val binding get() = _binding!!


    private val viewModel by viewModels<SavedQuotesViewModel>()
    private val qViewModel by activityViewModels<QuotesFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSavedQuoteBinding.bind(view)

        setUpRecyclerView()

        viewModel.getSavedQuotes().observe(viewLifecycleOwner) { listQuotesData ->
            if (listQuotesData.isEmpty()) {
                binding.noSavedQuoteText.isVisible = true
                adapter.differ.submitList(listQuotesData)
            } else {
                binding.noSavedQuoteText.isVisible = false
                adapter.differ.submitList(listQuotesData)
            }
        }

    }

    private fun setUpRecyclerView() {
        adapter = SavedQuotesAdapter(
            onBookmarkClick = { quote ->
                viewModel.deleteQuote(quote)
//                qViewModel.updatePagedList(quote.toFetchedQuotes())
//                qViewModel.isBookmarked(quote,quote.toFetchedQuotes())
                qViewModel.updateQuotesState(quote.toFetchedQuotes())
            },
            onCopyClick = { quoteText ->
                viewModel.copyQuote(quoteText)
            }
        )

        binding.apply {
            savedRecyclerViewQuotes.setHasFixedSize(true)
            savedRecyclerViewQuotes.adapter = adapter
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}