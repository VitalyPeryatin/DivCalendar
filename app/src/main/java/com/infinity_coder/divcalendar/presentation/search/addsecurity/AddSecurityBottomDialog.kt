package com.infinity_coder.divcalendar.presentation.search.addsecurity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.infinity_coder.divcalendar.presentation._common.DecimalTextWatcher
import com.infinity_coder.divcalendar.presentation._common.SecurityCurrencyDelegate
import com.infinity_coder.divcalendar.presentation._common.extensions.shake
import com.infinity_coder.divcalendar.presentation._common.extensions.viewModel
import kotlinx.android.synthetic.main.bottom_dialog_add_security.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

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
            ticker = requireArguments().getString(ARGUMENT_SEC_ID, ""),
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
        viewModel.securityPackage.observe(viewLifecycleOwner, Observer(this::addSecPackage))
        viewModel.shakePriceEditText.observe(viewLifecycleOwner, Observer { shakePriceEditText() })
        viewModel.shakeCountEditText.observe(viewLifecycleOwner, Observer { shakeCountEditText() })
    }

    private fun initUI() {
        nameTextView.text = security.name

        val formatSymbols = DecimalFormatSymbols(Locale.getDefault())
        formatSymbols.decimalSeparator = ','
        formatSymbols.groupingSeparator = ' '
        val formatter = DecimalFormat("#,###.${"#".repeat(5)}", formatSymbols)
        priceEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val price = s.toString().replace(formatter.decimalFormatSymbols.groupingSeparator.toString(),"").toFloatOrNull() ?: 0f
                viewModel.setSecurityPrice(price)
            }
        })
        priceEditText.addTextChangedListener(DecimalTextWatcher(priceEditText,formatter))

        countEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, symCount: Int) {
                val securitiesCount = s.toString().toIntOrNull() ?: 0
                viewModel.setSecurityCount(securitiesCount)
            }
        })

        addSecurityButton.setOnClickListener {
            viewModel.addSecurityPackage()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTotalPrice(price: Float?) {
        if (price == null) return
        val priceWithCurrency = SecurityCurrencyDelegate.getValueWithCurrency(
            requireContext(),
            price,
            security.currency
        )
        totalPriceTextView.text = resources.getString(R.string.total_price, priceWithCurrency)
    }

    private fun addSecPackage(secPackage: SecurityDbModel) {
        clickListener?.onAddSecPackageClick(secPackage)
    }

    private fun shakePriceEditText() {
        priceEditText.shake(SHAKE_AMPLITUDE)
    }

    private fun shakeCountEditText() {
        countEditText.shake(SHAKE_AMPLITUDE)
    }

    companion object {

        private const val ARGUMENT_SEC_ID = "sec_id"
        private const val ARGUMENT_NAME = "sec_name"
        private const val ARGUMENT_LOGO = "logo"
        private const val ARGUMENT_YEAR_YIELD = "year_yield"
        private const val ARGUMENT_CURRENCY = "currency"
        private const val ARGUMENT_TYPE = "type"

        private const val SHAKE_AMPLITUDE = 8f

        fun newInstance(security: SecurityNetModel): AddSecurityBottomDialog {
            val dialog = AddSecurityBottomDialog()
            dialog.arguments = bundleOf(
                ARGUMENT_SEC_ID to security.ticker,
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
        fun onAddSecPackageClick(securityPackage: SecurityDbModel)
    }
}