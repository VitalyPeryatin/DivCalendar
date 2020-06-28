package com.infinity_coder.divcalendar.presentation.calendar.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.domain.models.EditPaymentParams
import com.infinity_coder.divcalendar.presentation._common.BottomDialog
import com.infinity_coder.divcalendar.presentation.calendar.CalendarFragment
import com.infinity_coder.divcalendar.presentation.calendar.CalendarViewModel
import com.infinity_coder.divcalendar.presentation.calendar.models.PaymentPresentationModel
import kotlinx.android.synthetic.main.bottom_dialog_edit_payment.*

class ChangePaymentDialog : BottomDialog() {

    private lateinit var parentViewModel: CalendarViewModel

    private lateinit var securityIsin: String
    private lateinit var securityName: String
    private lateinit var paymentDate: String
    private var portfolioId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialogStyle)

        securityIsin = requireArguments().getString(SECURITY_ISIN, "")
        securityName = requireArguments().getString(SECURITY_NAME, "")
        paymentDate = requireArguments().getString(PAYMENT_DATE, "")
        portfolioId = requireArguments().getLong(PORTFOLIO_ID, 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_dialog_edit_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameTextView.text = securityName

        changePaymentsButton.setOnClickListener {
            if (countEditText.text.toString().isNotEmpty()) {
                val count = countEditText.text.toString().toBigDecimal()
                val editPaymentParams = EditPaymentParams(portfolioId, securityIsin, paymentDate, count)
                parentViewModel.updatePastPayment(editPaymentParams)
                dismiss()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val parentFragment = parentFragment
        if (parentFragment is CalendarFragment) {
            parentViewModel = parentFragment.viewModel
        }
    }

    companion object {

        private const val SECURITY_ISIN = "isin"
        private const val SECURITY_NAME = "name"
        private const val PAYMENT_DATE = "date"
        private const val PORTFOLIO_ID = "portfolio_id"

        const val TAG = "ChangePaymentDialog"

        fun newInstance(payment: PaymentPresentationModel): ChangePaymentDialog {
            val dialog = ChangePaymentDialog()
            dialog.arguments = bundleOf(
                SECURITY_ISIN to payment.isin,
                SECURITY_NAME to payment.name,
                PAYMENT_DATE to payment.databaseFieldDate,
                PORTFOLIO_ID to payment.portfolioId
            )
            return dialog
        }
    }
}