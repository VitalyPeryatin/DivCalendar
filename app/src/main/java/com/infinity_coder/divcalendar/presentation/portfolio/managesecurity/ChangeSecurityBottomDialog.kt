package com.infinity_coder.divcalendar.presentation.portfolio.managesecurity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.presentation._common.BottomDialog
import com.infinity_coder.divcalendar.presentation._common.DecimalFormatStorage
import com.infinity_coder.divcalendar.presentation._common.delegate.SecurityCurrencyDelegate
import com.infinity_coder.divcalendar.presentation._common.extensions.shake
import com.infinity_coder.divcalendar.presentation._common.text_watchers.DecimalCountTextWatcher
import com.infinity_coder.divcalendar.presentation._common.text_watchers.DecimalPriceTextWatcher
import kotlinx.android.synthetic.main.bottom_dialog_remove_security.*
import java.math.BigDecimal
import kotlin.math.abs

class ChangeSecurityBottomDialog : BottomDialog() {

    private var clickListener: OnClickListener? = null

    private val viewModel: ChangeSecurityViewModel by lazy {
        ViewModelProvider(this).get(ChangeSecurityViewModel::class.java)
    }

    private lateinit var securityName: String
    private lateinit var securityCurrency: String
    private var securityCount: Int = 0
    private var securityTotalPrice: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialogStyle)

        securityName = requireArguments().getString(ARGUMENT_NAME, "")
        securityCurrency = requireArguments().getString(ARGUMENT_CURRENCY, "")
        securityCount = requireArguments().getInt(ARGUMENT_COUNT, 0)
        securityTotalPrice = requireArguments().getDouble(ARGUMENT_TOTAL_PRICE, 0.0)
        val isin = requireArguments().getString(ARGUMENT_ISIN, "")

        viewModel.setSecurityIsin(isin)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_dialog_remove_security, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val parentFragment = parentFragment
        if (parentFragment is OnClickListener) {
            clickListener = parentFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.changeSecurity.observe(viewLifecycleOwner, Observer(this::changePackage))
        viewModel.shakePriceEditText.observe(viewLifecycleOwner, Observer { shakePriceEditText() })
        viewModel.shakeCountEditText.observe(viewLifecycleOwner, Observer { shakeCountEditText() })
        initUI()
    }

    private fun initUI() {
        nameTextView.text = securityName

        priceEditText.suffix = " ${SecurityCurrencyDelegate.getCurrencyBadge(requireContext(),securityCurrency)}"
        priceEditText.addTextChangedListener(object : DecimalPriceTextWatcher(priceEditText, DecimalFormatStorage.priceEditTextDecimalFormat) {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                val formatter = DecimalFormatStorage.priceEditTextDecimalFormat
                val price: BigDecimal = priceEditText.text.toString()
                    .replace(formatter.decimalFormatSymbols.groupingSeparator.toString(), "")
                    .replace(currentSeparator.toString(), LOCAL_SEPARATOR.toString())
                    .toBigDecimalOrNull() ?: BigDecimal.ZERO
                viewModel.setPackageCost(price)
            }
        })
        priceEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && priceEditText.text.toString().isEmpty()) {
                priceEditText.setText("0")
            }
        }

        val priceString = if (abs(securityTotalPrice % 1 - 0.0) < DecimalFormatStorage.EPS_ACCURACY) {
            securityTotalPrice.toInt().toString()
        } else {
            securityTotalPrice.toString()
        }
        priceEditText.setText(priceString)

        countEditText.addTextChangedListener(object : DecimalCountTextWatcher(countEditText, DecimalFormatStorage.countEditTextDecimalFormat) {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                val decimalFormat = DecimalFormatStorage.countEditTextDecimalFormat
                val securitiesCount: BigDecimal = countEditText.text.toString()
                    .replace(decimalFormat.decimalFormatSymbols.groupingSeparator.toString(), "")
                    .toBigDecimalOrNull() ?: BigDecimal.ZERO
                viewModel.setPackageCount(securitiesCount)
            }
        })

        countEditText.setText(securityCount.toString())

        changePackageButton.setOnClickListener {
            viewModel.changePackage()
        }

        deletePackageButton.setOnClickListener {
            viewModel.removePackage()
        }
    }

    private fun changePackage(securityPackage: SecurityDbModel) {
        clickListener?.onChangePackageClick(securityPackage)
    }

    private fun shakePriceEditText() {
        priceEditText.shake(SHAKE_AMPLITUDE)
    }

    private fun shakeCountEditText() {
        countEditText.shake(SHAKE_AMPLITUDE)
    }

    companion object {

        private const val ARGUMENT_ISIN = "isin"
        private const val ARGUMENT_NAME = "name"
        private const val ARGUMENT_CURRENCY = "currency"
        private const val ARGUMENT_TOTAL_PRICE = "total_price"
        private const val ARGUMENT_COUNT = "count"

        private const val SHAKE_AMPLITUDE = 8f

        const val TAG = "ChangeSecurityBottomDialog"

        fun newInstance(security: SecurityDbModel): ChangeSecurityBottomDialog {
            val dialog = ChangeSecurityBottomDialog()
            dialog.arguments = bundleOf(
                ARGUMENT_ISIN to security.isin,
                ARGUMENT_NAME to security.name,
                ARGUMENT_CURRENCY to security.currency,
                ARGUMENT_TOTAL_PRICE to security.totalPrice,
                ARGUMENT_COUNT to security.count
            )
            return dialog
        }
    }

    interface OnClickListener {
        fun onChangePackageClick(securityPackage: SecurityDbModel)
    }
}