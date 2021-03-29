package com.tanmay.quotes.ui.quotesFragment


import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.tanmay.quotes.R
import com.tanmay.quotes.data.QuotesData
import com.tanmay.quotes.databinding.FragmentQuotesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuotesFragment : Fragment(R.layout.fragment_quotes) {

//    private val TAG = "MainActivity1"

    private var _binding: FragmentQuotesBinding? = null
    private val binding get() = _binding!!


    private val viewModel: QuotesFragmentViewModel by activityViewModels()

    private lateinit var adapter: QuotesAdapter


    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentQuotesBinding.bind(view)


        adapter = QuotesAdapter(
            onBookMarkClick = { fetchedQuotes ->
                val qData = QuotesData(
                    id = fetchedQuotes.id,
                    _id = fetchedQuotes._id,
                    quoteAuthor = fetchedQuotes.quoteAuthor,
                    quoteText = fetchedQuotes.quoteText,
                    quoteGenre = fetchedQuotes.quoteGenre,
                    isBookmarked = fetchedQuotes.isBookmarked
                )
                viewModel.isBookmarked(qData,fetchedQuotes)
            },
            onCopyClick = { quoteText ->
                viewModel.copyQuote(quoteText)
            }
        )

        binding.apply {
            recyclerViewQuote.setHasFixedSize(true)

            recyclerViewQuote.adapter = adapter.withLoadStateFooter(
               footer = QuotesLoadStateAdapter{adapter.retry()}
            )

            btnRetry.setOnClickListener {
                adapter.retry()
            }
        }



        lifecycleScope.launch {
        viewModel.getFetchedQuotes().distinctUntilChanged().collectLatest {
            adapter.submitData(it)
        }
    }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            adapter.addLoadStateListener { loadState ->
                binding.apply {

                    shimmerLayout.isVisible = loadState.mediator?.refresh is LoadState.Loading
                    recyclerViewQuote.isVisible = loadState.mediator?.refresh is LoadState.NotLoading

                    btnRetry.isVisible = loadState.mediator?.refresh is LoadState.Error
                    txtViewError.isVisible = loadState.mediator?.refresh is LoadState.Error

                    if (recyclerViewQuote.isVisible) {
                        shimmerLayout.stopShimmer()
                    }

                }
            }
        }

    }

    override fun onResume() {
        binding.shimmerLayout.startShimmer()
        super.onResume()
    }

    override fun onPause() {
        binding.shimmerLayout.stopShimmer()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}
