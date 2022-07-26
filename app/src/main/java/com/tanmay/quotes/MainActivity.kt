package com.tanmay.quotes


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.tanmay.quotes.databinding.ActivityMainBinding
import com.tanmay.quotes.ui.quotesFragment.QuotesFragment
import com.tanmay.quotes.ui.quotesFragment.QuotesFragmentViewModel
import com.tanmay.quotes.ui.savedQuotesFragment.SavedQuoteFragment
import com.tanmay.quotes.ui.savedQuotesFragment.SavedQuotesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    private val TAG = "MainActivity"

    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding

    private val viewModelQuotesFragment by viewModels<QuotesFragmentViewModel>()

    private val viewModelSavedQuotes by viewModels<SavedQuotesViewModel>()

    private lateinit var ft: FragmentManager

    private lateinit var savedFragment : SavedQuoteFragment

    private lateinit var quotesFrag : QuotesFragment

//    private var isSavedQuotesFrag : Boolean = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //attaching my Fragment container to the bottom navigation
//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
//        navController = navHostFragment.findNavController()
//
//        binding.bottomNavView.setupWithNavController(navController)

        savedFragment = SavedQuoteFragment()
        quotesFrag = QuotesFragment()


        ft = supportFragmentManager
        ft.beginTransaction().add(R.id.fragment_container, quotesFrag, "Quotes").commit()
        ft.beginTransaction().add(R.id.fragment_container, savedFragment, "Saved")
            .hide(savedFragment).commit()


        binding.bottomNavView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.quotes -> {
                    ft.beginTransaction().show(quotesFrag).hide(savedFragment).commit()
                    true
                }

                R.id.savedQuotes ->{
                    ft.beginTransaction().hide(quotesFrag).show(savedFragment).commit()
//                    ft.beginTransaction().show(SavedQuoteFragment()).commit()
                    true
                }
                else -> {true}
            }
        }

        viewModelQuotesFragment.copyQuote.observe(this) { quoteText ->
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            Toast.makeText(this, "Quote Copied", Toast.LENGTH_SHORT).show()
            val clip: ClipData = ClipData.newPlainText("Quote Text", quoteText)
            clipboard.setPrimaryClip(clip)
        }

        viewModelSavedQuotes.copyQuote.observe(this) { quoteText ->
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            Toast.makeText(this, "Quote Copied", Toast.LENGTH_SHORT).show()
            val clip: ClipData = ClipData.newPlainText("Quote Text", quoteText)
            clipboard.setPrimaryClip(clip)
        }

    }

    override fun onBackPressed() {
        if (savedFragment.isVisible){
            binding.bottomNavView.selectedItemId = R.id.quotes
            ft.beginTransaction().show(quotesFrag).hide(savedFragment).commit()
        }else{
            binding.bottomNavView.visibility = View.VISIBLE
            super.onBackPressed()
        }
    }
}


