package com.tanmay.quotes


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tanmay.quotes.databinding.ActivityMainBinding
import com.tanmay.quotes.ui.customiseQuoteFragment.CustomiseQuotesFragment
import com.tanmay.quotes.ui.customiseQuoteFragment.CustomiseQuotesFragment.Companion.CUSTOMISEQUOTEFRAG
import com.tanmay.quotes.ui.detailedQuotes.DetailedQuotesFragment
import com.tanmay.quotes.ui.detailedQuotes.DetailedQuotesFragment.Companion.DETAILQUOTESFRAG
import com.tanmay.quotes.ui.quotesFragment.QuotesFragment
import com.tanmay.quotes.ui.quotesFragment.QuotesFragment.Companion.QUOTESFRAG
import com.tanmay.quotes.ui.quotesFragment.QuotesFragmentViewModel
import com.tanmay.quotes.ui.savedQuotesFragment.SavedQuoteFragment
import com.tanmay.quotes.ui.savedQuotesFragment.SavedQuoteFragment.Companion.SAVEDQUOTESFRAG
import com.tanmay.quotes.ui.savedQuotesFragment.SavedQuotesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    private val TAG = "MainActivity_test2"

    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding

    private val viewModelQuotesFragment by viewModels<QuotesFragmentViewModel>()

    private val viewModelSavedQuotes by viewModels<SavedQuotesViewModel>()

    private lateinit var ft: FragmentManager

    private lateinit var savedFragment: SavedQuoteFragment

    private lateinit var quotesFrag: QuotesFragment

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
        ft.beginTransaction().add(R.id.fragment_container, quotesFrag, QUOTESFRAG).commit()
        ft.beginTransaction().add(R.id.fragment_container, savedFragment, SAVEDQUOTESFRAG)
            .hide(savedFragment).commit()


        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.quotes -> {
                    ft.beginTransaction().show(quotesFrag).hide(savedFragment).commit()
                    true
                }

                R.id.savedQuotes -> {
                    ft.beginTransaction().hide(quotesFrag).show(savedFragment).commit()
//                    ft.beginTransaction().show(SavedQuoteFragment()).commit()
                    true
                }
                else -> {
                    true
                }
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

        supportFragmentManager.addFragmentOnAttachListener { fragmentManager, fragment ->
            if (fragment is DetailedQuotesFragment || fragment is CustomiseQuotesFragment) {
                binding.bottomNavView.visibility = View.GONE
            } else {
                binding.bottomNavView.visibility = View.VISIBLE
            }
        }

    }

    override fun onBackPressed() {
//        supportFragmentManager.fragment
//        Log.e("CheckBackStackEntry", "onBackPressed: ${supportFragmentManager.backStackEntryCount}", )
        val d = supportFragmentManager.findFragmentByTag(DETAILQUOTESFRAG)
        val c = supportFragmentManager.findFragmentByTag(CUSTOMISEQUOTEFRAG)
        if (savedFragment.isVisible) {
            binding.bottomNavView.selectedItemId = R.id.quotes
            ft.beginTransaction().show(quotesFrag).hide(savedFragment).commit()
        } else if (d?.isVisible == true) {
            binding.bottomNavView.visibility = View.VISIBLE
            super.onBackPressed()
        } else if (c?.isVisible == true) {
            MaterialAlertDialogBuilder(this)
                .setBackground(ResourcesCompat.getDrawable(resources, R.drawable.bg_alert_dialog, null))
                .setTitle(
                    Html.fromHtml(
                        "<font color='#FFFFFF''>Discard Changes</font>",
                        Html.FROM_HTML_MODE_LEGACY
                    )
                )
                .setMessage(
                    Html.fromHtml(
                        "<font color='#FFFFFF'>All the changes will be discarded.\n" +
                                "Make Sure to save image to the Gallery.</font>",
                        Html.FROM_HTML_MODE_LEGACY
                    )
                )
                .setPositiveButton(
                    Html.fromHtml(
                        "<font color='#FFFFFF'>Yes</font>",
                        Html.FROM_HTML_MODE_LEGACY
                    )
                ) { dialog, which ->
                    super.onBackPressed()
                }
                .setNegativeButton(
                    Html.fromHtml(
                        "<font color='#FFFFFF'>No</font>",
                        Html.FROM_HTML_MODE_LEGACY
                    )
                ) { dialog, which ->
                    dialog.dismiss()
                }.show()
            binding.bottomNavView.visibility = View.GONE
        } else {
            binding.bottomNavView.visibility = View.VISIBLE
            super.onBackPressed()
        }
    }
}


