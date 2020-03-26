package com.infinity_coder.divcalendar.presentation._common

import android.view.View
import androidx.annotation.NonNull
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BottomDialog : BottomSheetDialogFragment() {

    private val bottomSheetBehaviorCallback: BottomSheetBehavior.BottomSheetCallback =
        object : BottomSheetBehavior.BottomSheetCallback() {

            var lastOffset: Float = 0f

            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_SETTLING && lastOffset < -0.9) {
                    dismiss()
                }
                lastOffset = 0f
            }

            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
                lastOffset = slideOffset
            }
        }

    override fun onStart() {
        super.onStart()

        val params = (view!!.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior

        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.addBottomSheetCallback(bottomSheetBehaviorCallback)
        }
    }
}