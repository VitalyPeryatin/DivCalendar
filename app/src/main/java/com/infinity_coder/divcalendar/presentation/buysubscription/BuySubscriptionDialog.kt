package com.infinity_coder.divcalendar.presentation.buysubscription

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.infinity_coder.divcalendar.R
import kotlinx.android.synthetic.main.dialog_buy_subscription.*

class BuySubscriptionDialog : DialogFragment(), PremiumSubscriptionObserver {

    private var subscriptionObservable: PremiumSubscriptionObservable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_buy_subscription, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is PremiumSubscriptionObservable) {
            subscriptionObservable = context
            subscriptionObservable?.observeSubscription(this)
        }
    }

    override fun onDetach() {
        super.onDetach()
        subscriptionObservable?.removeSubscriptionObserver(this)
        subscriptionObservable = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancelButton.setOnClickListener {
            dismiss()
        }
        buySubscriptionButton.setOnClickListener {
            subscriptionObservable?.subscribe()
        }
    }

    override fun onSuccessfulSubscription() {
        dismiss()
    }

    companion object {
        fun newInstance(): BuySubscriptionDialog {
            return BuySubscriptionDialog()
        }
    }
}