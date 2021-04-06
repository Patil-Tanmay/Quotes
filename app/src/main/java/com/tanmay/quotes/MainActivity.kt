package com.tanmay.quotes


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.tanmay.quotes.databinding.ActivityMainBinding
import com.tanmay.quotes.ui.quotesFragment.QuotesFragmentViewModel
import com.tanmay.quotes.ui.savedQuotesFragment.SavedQuotesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    private val TAG = "MainActivity1"

    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding

    private val viewModelQuotesFragment by viewModels<QuotesFragmentViewModel>()

    private val viewModelSavedQuotes by viewModels<SavedQuotesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //attaching my Fragment container to the bottom navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()

        binding.bottomNavView.setupWithNavController(navController)


        viewModelQuotesFragment.copyQuote.observe(this, { quoteText ->
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            Toast.makeText(this, "Quote Copied", Toast.LENGTH_SHORT).show()
            val clip: ClipData = ClipData.newPlainText("Quote Text", quoteText)
            clipboard.setPrimaryClip(clip)
        })

        viewModelSavedQuotes.copyQuote.observe(this,{ quoteText ->
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            Toast.makeText(this, "Quote Copied", Toast.LENGTH_SHORT).show()
            val clip: ClipData = ClipData.newPlainText("Quote Text", quoteText)
            clipboard.setPrimaryClip(clip)
        })
        
    }
}


