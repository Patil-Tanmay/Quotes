package com.tanmay.quotes


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.tanmay.quotes.databinding.ActivityMainBinding
import com.tanmay.quotes.ui.quotesFragment.QuotesFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    private val TAG = "MainActivity1"

    private lateinit var navController: NavController

    private lateinit var binding : ActivityMainBinding

//    private val viewmodel by viewModels<QuotesFragmentViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //attaching my Fragment container to the bottom navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()

        binding.bottomNavView.setupWithNavController(navController)

//        binding.bottomNavView.setOnNavigationItemSelectedListener{
//            when(it.itemId) {
//                R.id.savedQuoteFragment -> {
//                    Toast.makeText(this,"SavedQuotesFragment",Toast.LENGTH_SHORT).show()
//                    navController.navigate(R.id.savedQuoteFragment)
//                }
//                R.id.quotesFragment -> {
//                    navController.navigate(R.id.quotesFragment)
//                }
//            }
//            true
//        }



    }
}


