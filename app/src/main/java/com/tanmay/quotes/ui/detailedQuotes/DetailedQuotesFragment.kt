package com.tanmay.quotes.ui.detailedQuotes

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tanmay.quotes.BuildConfig
import com.tanmay.quotes.R
import com.tanmay.quotes.databinding.DetailQuotesBinding
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class DetailedQuotesFragment : Fragment(R.layout.detail_quotes) {

    private var _binding: DetailQuotesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<DetailedQuotesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DetailQuotesBinding.bind(view)

        val args = this.arguments
        binding.quoteText.text = args?.getString("QuoteText")

//        binding.imgLayout.drawToBitmap()

        //setting up the bottomsheet
        binding.icShare.setOnClickListener {
            ShareQuoteBottomSheetFragment().show(
                childFragmentManager,
                "BottomSheetFrag"
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.shareQuote.collect {
                        when (it) {
                            ShareQuoteType.IMAGE -> {
                                createImageFromView(binding.imgLayout)
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

    private fun createImageFromView(v: View) {
        val bitmap = Bitmap.createBitmap(
            v.measuredWidth,
            v.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(bitmap)
        v.layout(v.left, v.top, v.right, v.bottom)
        v.draw(c)
        shareImage(bitmap)
    }

    private fun shareImage(bitmap: Bitmap) {
        // save bitmap to cache directory
        try {
            val cachePath = File(requireContext().cacheDir, "images")
            cachePath.mkdirs() // making the directory
            val stream =
                FileOutputStream("$cachePath/image.png") // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
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

    private fun createSharableIntent(contentUri: Uri) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        val chooser = Intent.createChooser(shareIntent, "Choose an app")
        val resInfoList =
            requireActivity().packageManager.queryIntentActivities(
                chooser,
                PackageManager.MATCH_DEFAULT_ONLY
            )

        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            requireActivity().grantUriPermission(
                packageName,
                Uri.parse(contentUri.toString()),
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(contentUri.toString()))

        shareIntent.type = "image/jpeg"
        startActivity(chooser, null)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}