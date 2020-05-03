package com.infinity_coder.divcalendar.presentation.billing.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.infinity_coder.divcalendar.R
import kotlinx.android.synthetic.main.dialog_subscription_purchased.*

class SubscriptionPurchasedDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_subscription_purchased, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        okButton.setOnClickListener { dismiss() }
    }

    companion object {

        const val TAG = "SubscriptionPurchasedDialog"

        fun newInstance(): SubscriptionPurchasedDialog {
            return SubscriptionPurchasedDialog()
        }
    }
}