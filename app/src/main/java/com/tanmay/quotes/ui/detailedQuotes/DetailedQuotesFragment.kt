package com.tanmay.quotes.ui.detailedQuotes

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import com.tanmay.quotes.BuildConfig
import com.tanmay.quotes.R
import com.tanmay.quotes.data.FetchedQuotesData
import com.tanmay.quotes.data.QuotesData
import com.tanmay.quotes.databinding.DetailQuotesBinding
import com.tanmay.quotes.ui.customiseQuoteFragment.CustomiseQuotesFragment
import com.tanmay.quotes.ui.customiseQuoteFragment.CustomiseQuotesFragment.Companion.CUSTOMISEQUOTEFRAG
import com.tanmay.quotes.ui.quotesFragment.QuotesFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@AndroidEntryPoint
class DetailedQuotesFragment : Fragment(R.layout.detail_quotes) {

    private var _binding: DetailQuotesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<DetailedQuotesViewModel>()

    private val qViewModel by activityViewModels<QuotesFragmentViewModel>()

    private var args: FetchedQuotesData? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DetailQuotesBinding.bind(view)

        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_quote_transition)

        args = this.arguments?.getParcelable<FetchedQuotesData>("FetchedQuotesData")
        binding.quoteText.text = args?.quoteText

        setUpObservables()
        setUpOnClickListeners()
    }

    private fun setUpOnClickListeners() {
        binding.icFavouriteEmpty.setImageResource(
            if (args?.isBookmarked == true) {
                R.drawable.ic_favourite_quote_filled
            } else {
                R.drawable.ic_favourite_quote_empty
            }
        )

        binding.icFavouriteEmpty.setOnClickListener {
            if (binding.icFavouriteEmpty.drawable.constantState == ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_favourite_quote_filled,
                    null
                )?.constantState
            ) {
                binding.icFavouriteEmpty.setImageResource(R.drawable.ic_favourite_quote_empty)
                val qData = QuotesData(
                    id = args?.id,
                    _id = args?._id!!,
                    quoteAuthor = args?.quoteAuthor!!,
                    quoteText = args?.quoteText!!,
                    quoteGenre = args?.quoteGenre!!,
                    isBookmarked = args?.isBookmarked
                )
                qViewModel.isBookmarked(qData, args!!)
                qViewModel.updateQuotesState(args?.copy(isBookmarked = false)!!)

            } else {
                binding.icFavouriteEmpty.setImageResource(R.drawable.ic_favourite_quote_filled)
                val qData = QuotesData(
                    id = args?.id,
                    _id = args?._id!!,
                    quoteAuthor = args?.quoteAuthor!!,
                    quoteText = args?.quoteText!!,
                    quoteGenre = args?.quoteGenre!!,
                    isBookmarked = args?.isBookmarked
                )
                qViewModel.isBookmarked(qData, args!!)
                qViewModel.updateQuotesState(args?.copy(isBookmarked = true)!!)
            }
        }

        //setting up the bottomsheet
        binding.icShare.setOnClickListener {
            ShareQuoteBottomSheetFragment().show(
                childFragmentManager,
                "BottomSheetFrag"
            )
        }

        //save to downloads
        binding.icSaveToGallery.setOnClickListener {
            createImageFromView(binding.imgLayout, ActionType.GALLERY)
        }

        //moving on to customise Fragment
        binding.customiseQuote.setOnClickListener {
            val customiseQuoteFrag = CustomiseQuotesFragment()
            val quoteText = Bundle()
            quoteText.putString(QUOTETEXT,args?.quoteText)
            customiseQuoteFrag.arguments = quoteText
            parentFragmentManager.beginTransaction()
                .hide(this)
                .add(R.id.fragment_container,customiseQuoteFrag, CUSTOMISEQUOTEFRAG).addToBackStack(DETAILQUOTESFRAG)
                .commit()
        }

        //onBackStateListener
        binding.icBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setUpObservables() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.shareQuote.collect {
                        when (it) {
                            ShareQuoteType.IMAGE -> {
                                createImageFromView(binding.imgLayout, ActionType.SHARE)
                            }

                            ShareQuoteType.TEXT -> {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, binding.quoteText.text.toString())
                                    type = "text/plain"
                                }

                                val shareIntent = Intent.createChooser(sendIntent, null)
                                startActivity(shareIntent)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun createImageFromView(v: View, actionType: ActionType) {
        val bitmap = Bitmap.createBitmap(
            v.measuredWidth,
            v.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(bitmap)
        v.layout(v.left, v.top, v.right, v.bottom)
        v.draw(c)
        when (actionType) {
            ActionType.GALLERY -> {
//                saveToGallery(bitmap)
                saveMediaToStorage(bitmap)
            }

            ActionType.SHARE -> {
                shareImage(bitmap)
            }
        }
    }

    private fun shareImage(bitmap: Bitmap) {
        // save bitmap to cache directory
        try {
            val cachePath = File(requireContext().cacheDir, "images")
            cachePath.mkdirs() // making the directory
            val stream =
                FileOutputStream("$cachePath/image.png") // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
        } catch (e: Exception) {
            Toasty.error(requireContext(), "Failed to generate Image.", Toasty.LENGTH_SHORT, true)
                .show()
            e.printStackTrace()
        }
        val imagePath = File(requireContext().cacheDir, "images")
        val newFile = File(imagePath, "image.png")
        val contentUri = FileProvider.getUriForFile(
            requireContext(),
            BuildConfig.APPLICATION_ID + ".provider",
            newFile
        )
        createSharableIntent(contentUri)
    }

    private fun saveToGallery(bitmap: Bitmap) {
        try {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val downloadPath = Environment.DIRECTORY_PICTURES
            val stream =
                FileOutputStream("/storage/emulated/0/Pictures/${args?._id}.png")
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
            Toasty.success(requireContext(), "$imagesDir Saved Image Successfully !", Snackbar.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toasty.error(requireContext(), "Failed to generate Image.", Toasty.LENGTH_SHORT, true)
                .show()
            e.printStackTrace()
        }
    }

    private fun saveMediaToStorage(bitmap: Bitmap) {

        //Generating a file name
        val filename = "${args?._id}.jpg"

        //Output stream
        var fos: OutputStream? = null

        //inline Functions
        fun askForPermission() {
            val storagePermission = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
            ) {
                requestForPermission.launch(storagePermission)
            } else {
                val imagesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDir, filename)
                fos = FileOutputStream(image)
                fos?.use {
                    //Finally writing the bitmap to the output stream that we opened
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                }
                Toasty.success(requireContext(), " Saved Image Successfully to $imagesDir!", Snackbar.LENGTH_SHORT).show()
            }
        }

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //getting the contentResolver
            context?.contentResolver?.also { resolver ->

                //Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                //Inserting the contentValues to contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                //Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
            fos?.use {
                //Finally writing the bitmap to the output stream that we opened
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            Toasty.success(requireContext(), "Saved Image Successfully to /storage/emulated/0/Pictures!", Snackbar.LENGTH_SHORT).show()
        } else {
            //These for devices running on android < Q
            //So I don't think an explanation is needed here
            askForPermission()
        }

    }

    private val requestForPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            val granted = it.entries.all { entries ->
                entries.value == true
            }
            if (granted) {
                Toasty.success(requireContext(), "Permission Granted!", Snackbar.LENGTH_SHORT).show()
            } else {
                Toasty.info(
                    requireContext(),
                    "Please Allow the Permission to save the images.",
                    Toasty.LENGTH_SHORT
                ).show()
            }
        }


    private fun createSharableIntent(contentUri: Uri) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        val chooser = Intent.createChooser(shareIntent, "Choose an app")

        //commented code is to get only required apps for given content
//        val resInfoList =
//            requireActivity().packageManager.queryIntentActivities(
//                chooser,
//                PackageManager.MATCH_DEFAULT_ONLY
//            )
//
//        for (resolveInfo in resInfoList) {
//            val packageName = resolveInfo.activityInfo.packageName
//            requireActivity().grantUriPermission(
//                packageName,
//                Uri.parse(contentUri.toString()),
//                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
//            )
//        }
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(contentUri.toString()))
        shareIntent.type = "image/"
        startActivity(chooser, null)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val DETAILQUOTESFRAG = "DetaileQuotesFragment"
        const val QUOTETEXT = "QuoteText"
    }
}

enum class ActionType {
    GALLERY,
    SHARE
}