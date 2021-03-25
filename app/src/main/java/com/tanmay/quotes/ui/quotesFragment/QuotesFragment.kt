package com.tanmay.quotes.ui.quotesFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.tanmay.quotes.R
import com.tanmay.quotes.data.QuotesData
import com.tanmay.quotes.databinding.FragmentQuotesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuotesFragment : Fragment(R.layout.fragment_quotes){

    private  val TAG = "MainFragment"

    private var _binding : FragmentQuotesBinding?= null
    private val binding get() = _binding!!

    private val viewModel by viewModels<QuotesFragmentViewModel>()

    private lateinit var adapter : QuotesAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentQuotesBinding.bind(view)


         adapter = QuotesAdapter(onBookMarkClick = { qData ->
            viewModel.isBookmarked(qData)
        })


        binding.apply {
            recyclerViewQuote.setHasFixedSize(true)
            recyclerViewQuote.adapter = adapter.withLoadStateHeaderAndFooter(
                header = QuotesLoadStateAdapter{ adapter.retry() },
                footer = QuotesLoadStateAdapter{ adapter.retry() }
            )

            btnRetry.setOnClickListener {
                adapter.retry()
            }
        }


      viewModel.getQuotes().observe(viewLifecycleOwner){
          adapter.submitData(viewLifecycleOwner.lifecycle,it)
      }


        adapter.addLoadStateListener { loadState ->
            binding.apply {
                shimmerLayout.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerViewQuote.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                txtViewError.isVisible = loadState.source.refresh is LoadState.Error

                if (recyclerViewQuote.isVisible){
                    Log.d(TAG, "onViewCreated: Shimmering Stopped")
                    shimmerLayout.stopShimmer()
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
