package com.infinity_coder.divcalendar.presentation.billing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.infinity_coder.divcalendar.R

class SubscriptionDialog : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_subscription, container, false)
    }

    companion object {
        fun newInstance(): SubscriptionDialog {
            return SubscriptionDialog()
        }
    }
}