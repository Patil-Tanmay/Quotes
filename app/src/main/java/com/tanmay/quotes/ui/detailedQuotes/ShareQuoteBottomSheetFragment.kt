package com.tanmay.quotes.ui.detailedQuotes

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tanmay.quotes.R
import com.tanmay.quotes.databinding.FragmentShareQuoteBinding

class ShareQuoteBottomSheetFragment: BottomSheetDialogFragment() {

    private var _binding : FragmentShareQuoteBinding?=null
    private val binding : FragmentShareQuoteBinding get() = _binding!!

    private val viewModel by activityViewModels<DetailedQuotesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShareQuoteBinding.inflate(layoutInflater)
        return  binding.root
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
//        dialog.setOnShowListener { setupBottomSheet(it) }
//        return dialog
//    }
//
//    private fun setupBottomSheet(dialogInterface: DialogInterface) {
//        val bottomSheetDialog = dialogInterface as BottomSheetDialog
//        val bottomSheet = bottomSheetDialog.findViewById<View>(
//            com.google.android.material.R.id.design_bottom_sheet)
//            ?: return
//        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
//    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageLayout.setOnClickListener {
            viewModel.setShareQuoteType(ShareQuoteType.IMAGE)
            dismiss()
        }

        binding.textLayout.setOnClickListener {
            viewModel.setShareQuoteType(ShareQuoteType.TEXT)
            dismiss()
        }
    }


}