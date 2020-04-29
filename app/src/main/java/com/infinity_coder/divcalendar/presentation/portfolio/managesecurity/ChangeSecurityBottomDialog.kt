package com.infinity_coder.divcalendar.presentation.portfolio.managesecurity

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
import com.infinity_coder.divcalendar.presentation._common.DecimalFormatStorage
import com.infinity_coder.divcalendar.presentation._common.text_watchers.DecimalTextWatcher
import com.infinity_coder.divcalendar.presentation._common.extensions.shake
import com.infinity_coder.divcalendar.presentation._common.extensions.viewModel
import com.infinity_coder.divcalendar.presentation._common.text_watchers.DecimalCountTextWatcher
import kotlinx.android.synthetic.main.bottom_dialog_remove_security.*
import kotlinx.android.synthetic.main.bottom_dialog_remove_security.countEditText
import kotlinx.android.synthetic.main.bottom_dialog_remove_security.nameTextView
import kotlinx.android.synthetic.main.bottom_dialog_remove_security.priceEditText
import kotlinx.android.synthetic.main.item_security_portfolio.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class ChangeSecurityBottomDialog : BottomDialog() {

    private var clickListener: OnClickListener? = null

    private lateinit var security: SecurityNetModel

    private val viewModel: ChangeSecurityViewModel by lazy {
        viewModel { ChangeSecurityViewModel() }
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

        initUI()

        viewModel.changeSecurityPackage.observe(viewLifecycleOwner, Observer(this::changePackage))
        viewModel.shakePriceEditText.observe(viewLifecycleOwner, Observer { shakePriceEditText() })
        viewModel.shakeCountEditText.observe(viewLifecycleOwner, Observer { shakeCountEditText() })
    }

    private fun initUI() {
        nameTextView.text = security.name

        priceEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val formatter = DecimalFormatStorage.formatter
                val price = s.toString().replace(formatter.decimalFormatSymbols.groupingSeparator.toString(), "").toDoubleOrNull() ?: 0.0
                viewModel.setPackageCost(price)
            }
        })
        priceEditText.addTextChangedListener(
            DecimalTextWatcher(
                priceEditText,
                DecimalFormatStorage.formatter
            )
        )

        countEditText.addTextChangedListener(DecimalCountTextWatcher(countEditText, DecimalFormatStorage.countDecimalFormat))
        countEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, symCount: Int) {
                val decimalFormat = DecimalFormatStorage.countDecimalFormat
                val securitiesCount = s.toString().replace(decimalFormat.decimalFormatSymbols.groupingSeparator.toString(), "").toIntOrNull() ?: 0
                viewModel.setPackageCount(securitiesCount)
            }
        })

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
        private const val ARGUMENT_TICKER = "ticker"
        private const val ARGUMENT_NAME = "sec_name"
        private const val ARGUMENT_LOGO = "logo"
        private const val ARGUMENT_YEAR_YIELD = "year_yield"
        private const val ARGUMENT_CURRENCY = "currency"
        private const val ARGUMENT_TYPE = "type"

        private const val SHAKE_AMPLITUDE = 8f

        private const val MAX_NUMBER_IN_INTEGER_PART_COUNT = 9

        fun newInstance(security: SecurityDbModel): ChangeSecurityBottomDialog {
            val dialog = ChangeSecurityBottomDialog()
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

    interface OnClickListener {
        fun onChangePackageClick(securityPackage: SecurityDbModel)
    }
}