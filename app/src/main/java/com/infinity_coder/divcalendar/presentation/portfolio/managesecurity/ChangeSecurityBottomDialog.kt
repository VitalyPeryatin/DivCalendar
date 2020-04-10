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
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.SecurityNetworkModel
import com.infinity_coder.divcalendar.presentation._common.BottomDialog
import com.infinity_coder.divcalendar.presentation._common.shake
import com.infinity_coder.divcalendar.presentation._common.viewModel
import com.infinity_coder.divcalendar.presentation.search.addsecurity.AddSecurityBottomDialog
import kotlinx.android.synthetic.main.bottom_dialog_remove_security.*

class ChangeSecurityBottomDialog : BottomDialog() {

    private var clickListener: OnClickListener? = null

    private lateinit var security: SecurityNetworkModel

    private val viewModel: ChangeSecurityViewModel by lazy {
        viewModel { ChangeSecurityViewModel() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialogStyle)

        security = SecurityNetworkModel(
            ticker = requireArguments().getString(ARGUMENT_SEC_ID, ""),
            name = requireArguments().getString(ARGUMENT_NAME, ""),
            logo = requireArguments().getString(ARGUMENT_LOGO, ""),
            yearYield = requireArguments().getFloat(ARGUMENT_YEAR_YIELD, 0f)
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
                val price = s.toString().toFloatOrNull() ?: 0f
                viewModel.setPackageCost(price)
            }
        })

        countEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, symCount: Int) {
                val securitiesCount = s.toString().toIntOrNull() ?: 0
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

    private fun changePackage(securityPackage: SecurityPackageDbModel) {
        clickListener?.onChangePackageClick(securityPackage)
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

        private const val SHAKE_AMPLITUDE = 8f

        fun newInstance(security: SecurityPackageDbModel): ChangeSecurityBottomDialog {
            val dialog = ChangeSecurityBottomDialog()
            dialog.arguments = bundleOf(
                ARGUMENT_SEC_ID to security.secid,
                ARGUMENT_NAME to security.name,
                ARGUMENT_LOGO to security.logo,
                ARGUMENT_YEAR_YIELD to security.yearYield
            )
            return dialog
        }
    }

    interface OnClickListener {
        fun onChangePackageClick(securityPackage: SecurityPackageDbModel)
    }
}