package com.tanmay.quotes.ui.quotesFragment


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tanmay.quotes.R
import com.tanmay.quotes.data.QuotesData
import com.tanmay.quotes.databinding.FragmentQuotesBinding
import com.tanmay.quotes.ui.detailedQuotes.DetailedQuotesFragment
import com.tanmay.quotes.utils.NetworkState
import com.tanmay.quotes.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuotesFragment : Fragment(R.layout.fragment_quotes) {

    private var _binding: FragmentQuotesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuotesFragmentViewModel by activityViewModels()

    private lateinit var quotesAdapter: QuotesAdapter

    private lateinit var genresAdapter: GenreAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentQuotesBinding.bind(view)

        viewModel.getQuotesGenres()

        val quotesListing = viewModel.getQuotes()

        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavView)

        genresAdapter = GenreAdapter() { genreName, position ->
            //make api call to change list of quotes according to given Genre
//            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
//            viewModel.updateGenreStatus(genreName, position)
            quotesListing.onRefresh(genreName, false)
            binding.genreText.text = genreName
        }

        quotesAdapter = QuotesAdapter(
            onBookMarkClick = { fetchedQuotes ->
                val qData = QuotesData(
                    id = fetchedQuotes.id,
                    _id = fetchedQuotes._id,
                    quoteAuthor = fetchedQuotes.quoteAuthor,
                    quoteText = fetchedQuotes.quoteText,
                    quoteGenre = fetchedQuotes.quoteGenre,
                    isBookmarked = fetchedQuotes.isBookmarked
                )
                viewModel.isBookmarked(qData, fetchedQuotes)
            },
            onCopyClick = { quoteText ->
                viewModel.copyQuote(quoteText)
            },
            onRootClick = {

                bottomNav.visibility = View.GONE

                    parentFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, DetailedQuotesFragment()).addToBackStack("Quotes")
                        .commit()
            }
        )

        setupGenreAdapter()
        setUpQuotesAdapter()

        binding.btnRetry.setOnClickListener {
            quotesListing.onRefresh.invoke(null, true)
        }

        quotesListing.articles.observe(viewLifecycleOwner) {
            quotesAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.quotesGenres.collect {
                when (it) {
                    is Resource.Success -> {
                        binding.genreText.visibility = View.VISIBLE
                        genresAdapter.submitGenres(it.data!!)
                        binding.shimmerLayoutGenre.visibility = View.GONE
                    }

                    is Resource.Loading -> {
                        binding.genreText.visibility = View.GONE
                        binding.shimmerLayoutGenre.visibility = View.VISIBLE
                    }

                    is Resource.Error -> {
                        binding.genreText.visibility = View.GONE
                        binding.shimmerLayoutGenre.visibility = View.GONE
                        Toast.makeText(context, "Unable to fetch Genres.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    quotesListing.refreshState.collect {
                        when (it) {
                            NetworkState.LOADING -> {
                                if (binding.recyclerViewQuote.canScrollVertically(1)) {
                                    binding.shimmerLayoutQuotes.visibility = View.GONE
                                    binding.recyclerViewQuote.visibility = View.VISIBLE
                                    binding.genreRec.visibility = View.VISIBLE
                                    binding.btnRetry.visibility = View.GONE
                                } else {
                                    binding.shimmerLayoutQuotes.visibility = View.VISIBLE
                                    binding.recyclerViewQuote.visibility = View.GONE
                                    binding.genreRec.visibility = View.GONE
                                    binding.btnRetry.visibility = View.GONE
                                }
                            }

                            NetworkState.IDLE -> {
                                binding.shimmerLayoutQuotes.visibility = View.GONE
                                binding.recyclerViewQuote.visibility = View.VISIBLE
                                binding.genreRec.visibility = View.VISIBLE
                                binding.btnRetry.visibility = View.GONE
                            }

                            else -> {
                                binding.btnRetry.visibility = View.VISIBLE
                                binding.shimmerLayoutQuotes.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            quotesListing.onRefresh(null, true)
        }


//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            quotesAdapter.addLoadStateListener { loadState ->
//                binding.apply {
//
//                    shimmerLayout.isVisible = loadState.mediator?.refresh is LoadState.Loading
//                    recyclerViewQuote.isVisible =
//                        loadState.mediator?.refresh is LoadState.NotLoading
//
//                    genreRec.isVisible =
//                        loadState.mediator?.refresh is LoadState.NotLoading
//
//                    btnRetry.isVisible = loadState.mediator?.refresh is LoadState.Error
//                    txtViewError.isVisible = loadState.mediator?.refresh is LoadState.Error
//
//                    if (recyclerViewQuote.isVisible) {
//                        shimmerLayout.stopShimmer()
//                    }
//
//                }
//            }
//        }

    }

    private fun setupGenreAdapter() {
        binding.apply {
            genreRec.setHasFixedSize(true)
            genreRec.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            genreRec.adapter = genresAdapter
        }
    }

    private fun setUpQuotesAdapter() {
        binding.apply {
            recyclerViewQuote.setHasFixedSize(true)
            recyclerViewQuote.adapter = quotesAdapter
        }
    }

    override fun onResume() {
        binding.shimmerLayoutQuotes.startShimmer()
        super.onResume()
    }

    override fun onPause() {
        binding.shimmerLayoutQuotes.stopShimmer()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}
