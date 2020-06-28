package com.infinity_coder.divcalendar.presentation.portfolio.dialogs.changesecurity

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

class ChangeSecurityBottomDialog : BottomDialog() {

    private lateinit var securityPackage: SecurityDbModel

    private var changeSecurityCallback: ChangeSecurityCallback? = null

    private val viewModel: ChangeSecurityViewModel by lazy {
        ViewModelProvider(this).get(ChangeSecurityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialogStyle)

        securityPackage = requireArguments().getParcelable(ARGUMENT_SECURITY_DB_MODEL)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_dialog_remove_security, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val parentFragment = parentFragment
        if (parentFragment is ChangeSecurityCallback) {
            changeSecurityCallback = parentFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.securityPackageChanged.observe(viewLifecycleOwner, Observer { onChangePortfolio() })
        viewModel.shakePriceEditText.observe(viewLifecycleOwner, Observer { priceEditText.shake(SHAKE_AMPLITUDE) })
        viewModel.shakeCountEditText.observe(viewLifecycleOwner, Observer { countEditText.shake(SHAKE_AMPLITUDE) })

        initUI()
    }

    private fun onChangePortfolio() {
        dismiss()
        changeSecurityCallback?.onChangeSecurityPackage()
    }

    private fun initUI() {
        nameTextView.text = securityPackage.name

        initPriceEditText()

        initCountEditText()

        changePackageButton.setOnClickListener {
            viewModel.changePackage(securityPackage)
        }

        deletePackageButton.setOnClickListener {
            viewModel.removePackage(securityPackage)
        }
    }

    private fun initPriceEditText() {
        priceEditText.suffix = " ${SecurityCurrencyDelegate.getCurrencyBadge(requireContext(), securityPackage.currency)}"

        priceEditText.addTextChangedListener(object : DecimalPriceTextWatcher(priceEditText, DecimalFormatStorage.priceEditTextDecimalFormat) {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                val formatter = DecimalFormatStorage.priceEditTextDecimalFormat
                val price = priceEditText.text.toString()
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

        val priceString = DecimalFormatStorage.formatterWithPoints.format(securityPackage.totalPrice)
        priceEditText.setText(priceString)
    }

    private fun initCountEditText() {
        countEditText.addTextChangedListener(object : DecimalCountTextWatcher(countEditText, DecimalFormatStorage.countEditTextDecimalFormat) {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                val decimalFormat = DecimalFormatStorage.countEditTextDecimalFormat
                val securitiesCount = countEditText.text.toString()
                    .replace(decimalFormat.decimalFormatSymbols.groupingSeparator.toString(), "")
                    .toBigDecimalOrNull() ?: BigDecimal.ZERO
                viewModel.setPackageCount(securitiesCount)
            }
        })

        countEditText.setText(securityPackage.count.toString())
    }

    companion object {
        const val TAG = "ChangeSecurityBottomDialog"

        private const val SHAKE_AMPLITUDE = 8f

        private const val ARGUMENT_SECURITY_DB_MODEL = "security_db_model"

        fun newInstance(securityPackage: SecurityDbModel): ChangeSecurityBottomDialog {
            return ChangeSecurityBottomDialog().apply {
                arguments = bundleOf(ARGUMENT_SECURITY_DB_MODEL to securityPackage)
            }
        }
    }

    interface ChangeSecurityCallback {
        fun onChangeSecurityPackage()
    }
}