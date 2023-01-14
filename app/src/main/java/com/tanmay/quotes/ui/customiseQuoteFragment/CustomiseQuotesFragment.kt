package com.tanmay.quotes.ui.customiseQuoteFragment

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.tanmay.quotes.BuildConfig
import com.tanmay.quotes.R
import com.tanmay.quotes.data.models.ColorPalleteModel
import com.tanmay.quotes.databinding.BottomsheetColorPickerBinding
import com.tanmay.quotes.databinding.FragmentCustomiseQuoteBinding
import com.tanmay.quotes.ui.detailedQuotes.ActionType
import com.tanmay.quotes.ui.detailedQuotes.DetailedQuotesFragment.Companion.QUOTETEXT
import com.tanmay.quotes.utils.saveMediaToStorage
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.delay
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class CustomiseQuotesFragment : Fragment(R.layout.fragment_customise_quote) {

    private var _binding: FragmentCustomiseQuoteBinding? = null
    private val binding: FragmentCustomiseQuoteBinding get() = _binding!!

    private var quoteText: String? = null

    private lateinit var bottomSheetDialog: BottomSheetDialog

    private lateinit var bottomSheetBinding: BottomsheetColorPickerBinding

    private lateinit var colorAdapter: ColorAdapter

    private var colorList = arrayListOf<ColorPalleteModel>()

    private var backGroundColorPosition: Int = 0

    private var textColorPosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomiseQuoteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //apply colors
        colorAdapter = ColorAdapter({ textColor ->
            textColorPosition = colorList.map { it.color }.indexOf(textColor.color)
            binding.quoteText.setTextColor(ColorStateList.valueOf(textColor.color))
//            DrawableCompat.setTint(binding.quoteS.drawable, textColor.color)
//            DrawableCompat.setTint(binding.quoteE.drawable, textColor.color)
            binding.quoteE.setTextColor(ColorStateList.valueOf(textColor.color))
            binding.quoteS.setTextColor(ColorStateList.valueOf(textColor.color))

            bottomSheetDialog.dismiss()
        }, { backgroundColor ->
            backGroundColorPosition = colorList.map { it.color }.indexOf(backgroundColor.color)
            binding.imgLayout.backgroundTintList = ColorStateList.valueOf(backgroundColor.color)
            bottomSheetDialog.dismiss()
        })

        setUpColorList()
        setUpBottomSheetDialog()

        quoteText = arguments?.getString(QUOTETEXT)

        binding.quoteText.text = quoteText
        setUpOnClickListeners()
    }

    private fun setUpColorList() {
        val colorsTypedArray = resources.obtainTypedArray(R.array.default_colors)
        for (i in 0 until colorsTypedArray.length()) {
            colorList.add(ColorPalleteModel(colorsTypedArray.getColor(i, 0), false))
        }
        colorsTypedArray.recycle()
    }

    private fun setUpBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetBinding = BottomsheetColorPickerBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.icClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        setUpBottomSheetRecView()
    }

    private fun setUpBottomSheetRecView() {
//        colorAdapter.setColorList(colorList)
        bottomSheetBinding.recyclerViewColorPallete.layoutManager =
            GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
        bottomSheetBinding.recyclerViewColorPallete.adapter = colorAdapter
    }

    private fun setUpOnClickListeners() {
        binding.changeBackground.setOnClickListener {
            colorAdapter.setType(ColorSelectedType.Background)
            colorAdapter.setColorList(colorList, backGroundColorPosition)
            bottomSheetDialog.show()
        }

        binding.changeQuoteColor.setOnClickListener {
            colorAdapter.setType(ColorSelectedType.Text)
            colorAdapter.setColorList(colorList, textColorPosition)
            bottomSheetDialog.show()
        }

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.icSaveToGallery.setOnClickListener {
            val v = binding.imgLayout
            val bitmap = Bitmap.createBitmap(
                v.measuredWidth,
                v.measuredHeight,
                Bitmap.Config.ARGB_8888
            )
            val c = Canvas(bitmap)
            v.layout(v.left, v.top, v.right, v.bottom)
            v.draw(c)
            bitmap.saveMediaToStorage(requireContext(),requestForPermission, quoteText?.get(0).toString())
        }

        binding.icShare.setOnClickListener { createImageFromView(binding.imgLayout, ActionType.SHARE) }
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
//                saveMediaToStorage(bitmap)
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

    companion object {
        const val CUSTOMISEQUOTEFRAG = "CustomiseFrag"
    }

}

enum class ColorSelectedType {
    Background,
    Text
}