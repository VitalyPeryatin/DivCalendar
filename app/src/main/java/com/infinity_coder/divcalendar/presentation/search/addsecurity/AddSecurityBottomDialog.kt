package com.infinity_coder.divcalendar.presentation.search.addsecurity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.data.network.model.SecurityNetModel
import com.infinity_coder.divcalendar.presentation._common.BottomDialog
import com.infinity_coder.divcalendar.presentation._common.DecimalFormatStorage
import com.infinity_coder.divcalendar.presentation._common.text_watchers.DecimalPriceTextWatcher
import com.infinity_coder.divcalendar.presentation._common.SecurityCurrencyDelegate
import com.infinity_coder.divcalendar.presentation._common.extensions.shake
import com.infinity_coder.divcalendar.presentation._common.extensions.viewModel
import com.infinity_coder.divcalendar.presentation._common.text_watchers.DecimalCountTextWatcher
import kotlinx.android.synthetic.main.bottom_dialog_add_security.*
import kotlinx.android.synthetic.main.bottom_dialog_add_security.countEditText
import kotlinx.android.synthetic.main.bottom_dialog_add_security.nameTextView
import kotlinx.android.synthetic.main.bottom_dialog_add_security.priceEditText
import kotlinx.android.synthetic.main.bottom_dialog_remove_security.*

class AddSecurityBottomDialog : BottomDialog() {

    private var clickListener: OnDialogClickListener? = null

    private lateinit var security: SecurityNetModel

    private val viewModel: AddSecurityViewModel by lazy {
        viewModel { AddSecurityViewModel() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialogStyle)

        security = SecurityNetModel(
            isin = requireArguments().getString(ARGUMENT_ISIN, ""),
            ticker = requireArguments().getString(ARGUMENT_TICKER, ""),
            name = requireArguments().getString(ARGUMENT_NAME, ""),
            logo = requireArguments().getString(ARGUMENT_LOGO, ""),
            yearYield = requireArguments().getFloat(ARGUMENT_YEAR_YIELD, 0f),
            currency = requireArguments().getString(ARGUMENT_CURRENCY, ""),
            type = requireArguments().getString(ARGUMENT_TYPE, "")
        )
        viewModel.setSecurity(security)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_dialog_add_security, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val parentFragment = parentFragment
        if (parentFragment is OnDialogClickListener) {
            clickListener = parentFragment
        } else if (context is OnDialogClickListener) {
            clickListener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        viewModel.getTotalSecurityPriceLiveData().observe(viewLifecycleOwner, Observer(this::setTotalPrice))
        viewModel.securityLiveData.observe(viewLifecycleOwner, Observer(this::addSecurity))
        viewModel.shakePriceEditText.observe(viewLifecycleOwner, Observer { shakePriceEditText() })
        viewModel.shakeCountEditText.observe(viewLifecycleOwner, Observer { shakeCountEditText() })
    }

    private fun initUI() {
        nameTextView.text = security.name

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

        addSecurityButton.setOnClickListener {
            viewModel.addSecurityPackage()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTotalPrice(price: Double?) {
        if (price == null) return
        val priceWithCurrency = SecurityCurrencyDelegate.getValueWithCurrency(
            requireContext(),
            price,
            security.currency
        )
        totalPriceTextView.text = resources.getString(R.string.total_price, priceWithCurrency)
    }

    private fun addSecurity(security: SecurityDbModel) {
        clickListener?.onAddSecurityClick(security)
    }

    private fun shakePriceEditText() {
        priceEditText.shake(SHAKE_AMPLITUDE)
    }

    private fun shakeCountEditText() {
        countEditText.shake(SHAKE_AMPLITUDE)
    }

    companion object {

        private const val ARGUMENT_ISIN = "isin"
        private const val ARGUMENT_TICKER = "ticker"
        private const val ARGUMENT_NAME = "sec_name"
        private const val ARGUMENT_LOGO = "logo"
        private const val ARGUMENT_YEAR_YIELD = "year_yield"
        private const val ARGUMENT_CURRENCY = "currency"
        private const val ARGUMENT_TYPE = "type"

        private const val SHAKE_AMPLITUDE = 8f

        fun newInstance(security: SecurityNetModel): AddSecurityBottomDialog {
            val dialog = AddSecurityBottomDialog()
            dialog.arguments = bundleOf(
                ARGUMENT_ISIN to security.isin,
                ARGUMENT_TICKER to security.ticker,
                ARGUMENT_NAME to security.name,
                ARGUMENT_LOGO to security.logo,
                ARGUMENT_YEAR_YIELD to security.yearYield,
                ARGUMENT_CURRENCY to security.currency,
                ARGUMENT_TYPE to security.type
            )
            return dialog
        }
    }

    interface OnDialogClickListener {
        fun onAddSecurityClick(securityPackage: SecurityDbModel)
    }
}