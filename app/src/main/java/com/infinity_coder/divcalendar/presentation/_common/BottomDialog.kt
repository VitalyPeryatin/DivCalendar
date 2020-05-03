package com.infinity_coder.divcalendar.presentation._common

import android.view.View
import androidx.annotation.NonNull
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BottomDialog : BottomSheetDialogFragment() {

    private val bottomSheetBehaviorCallback: BottomSheetBehavior.BottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
        }
    }

    override fun onStart() {
        super.onStart()

        getSheetBehavior().addBottomSheetCallback(bottomSheetBehaviorCallback)
    }

    private fun getSheetBehavior(): BottomSheetBehavior<View> {
        val sheetView = (requireView().parent as View)
        val sheetParams = sheetView.layoutParams as CoordinatorLayout.LayoutParams
        return sheetParams.behavior as BottomSheetBehavior<View>
    }
}