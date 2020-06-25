package com.infinity_coder.divcalendar.presentation.search.dialogs.addsecurity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.data.network.model.SecurityNetModel
import com.infinity_coder.divcalendar.presentation._common.BottomDialog
import com.infinity_coder.divcalendar.presentation._common.DecimalFormatStorage
import com.infinity_coder.divcalendar.presentation._common.delegate.SecurityCurrencyDelegate
import com.infinity_coder.divcalendar.presentation._common.extensions.executeIfSubscribed
import com.infinity_coder.divcalendar.presentation._common.extensions.shake
import com.infinity_coder.divcalendar.presentation._common.extensions.showSuccessfulToast
import com.infinity_coder.divcalendar.presentation._common.text_watchers.DecimalCountTextWatcher
import com.infinity_coder.divcalendar.presentation._common.text_watchers.DecimalPriceTextWatcher
import kotlinx.android.synthetic.main.bottom_dialog_add_security.*

class AddSecurityBottomDialog : BottomDialog() {

    private lateinit var security: SecurityNetModel

    val viewModel: AddSecurityViewModel by lazy {
        ViewModelProvider(this).get(AddSecurityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialogStyle)

        security = requireArguments().getParcelable(ARGUMENT_SECURITY_NET_MODEL)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_dialog_add_security, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.securityTotalPriceLiveDate.observe(viewLifecycleOwner, Observer(this::setTotalPrice))
        viewModel.addSecurityIfHasSubscription.observe(viewLifecycleOwner, Observer(this::addSecurityPackageIfHasSubscription))
        viewModel.success.observe(viewLifecycleOwner, Observer { onSuccess() })
        viewModel.shakeCountEditText.observe(viewLifecycleOwner, Observer { countEditText.shake(SHAKE_AMPLITUDE) })
        viewModel.shakePriceEditText.observe(viewLifecycleOwner, Observer { priceEditText.shake(SHAKE_AMPLITUDE) })

        initUI()
    }

    private fun setTotalPrice(price: Double) {
        val priceWithCurrency = SecurityCurrencyDelegate.getValueWithCurrency(requireContext(), price, security.currency)
        totalPriceTextView.text = resources.getString(R.string.total_price, priceWithCurrency)
    }

    private fun addSecurityPackageIfHasSubscription(securityPackage: SecurityDbModel) {
        executeIfSubscribed { viewModel.appendSecurityPackage(securityPackage) }
    }

    private fun onSuccess() {
        dismiss()
        requireContext().showSuccessfulToast(layoutInflater, R.string.add_security_successful)
    }

    private fun initUI() {
        nameTextView.text = security.name

        initPriceEditText()

        initCountEditText()

        addSecurityButton.setOnClickListener {
            viewModel.addSecurityPackage(security)
        }
    }

    private fun initPriceEditText() {
        priceEditText.suffix = " ${SecurityCurrencyDelegate.getCurrencyBadge(requireContext(),security.currency)}"

        priceEditText.addTextChangedListener(object : DecimalPriceTextWatcher(priceEditText, DecimalFormatStorage.priceEditTextDecimalFormat) {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                val formatter = DecimalFormatStorage.priceEditTextDecimalFormat
                val price = priceEditText.text.toString()
                    .replace(formatter.decimalFormatSymbols.groupingSeparator.toString(), "")
                    .replace(currentSeparator.toString(), LOCAL_SEPARATOR.toString())
                    .toDoubleOrNull() ?: 0.0
                viewModel.setSecurityPrice(price)
            }
        })

        priceEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && priceEditText.text.toString().isEmpty()) {
                priceEditText.setText("0")
            }
        }

        priceEditText.setText(security.currentPrice.toString())
    }

    private fun initCountEditText() {
        countEditText.addTextChangedListener(object : DecimalCountTextWatcher(countEditText, DecimalFormatStorage.countEditTextDecimalFormat) {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                val decimalFormat = DecimalFormatStorage.countEditTextDecimalFormat
                val securitiesCount = countEditText.text.toString()
                    .replace(decimalFormat.decimalFormatSymbols.groupingSeparator.toString(), "")
                    .toIntOrNull() ?: 0
                viewModel.setSecurityCount(securitiesCount)
            }
        })

        if (countEditText.requestFocus()) {
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    companion object {
        const val TAG = "AddSecurityBottomDialog"

        private const val SHAKE_AMPLITUDE = 8f

        private const val ARGUMENT_SECURITY_NET_MODEL = "security_net_model"

        fun newInstance(securityNetModel: SecurityNetModel): AddSecurityBottomDialog {
            return AddSecurityBottomDialog().apply {
                arguments = bundleOf(ARGUMENT_SECURITY_NET_MODEL to securityNetModel)
            }
        }
    }
}