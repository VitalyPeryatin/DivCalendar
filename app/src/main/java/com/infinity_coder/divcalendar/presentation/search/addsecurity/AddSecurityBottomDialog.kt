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
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.ShortSecurityNetworkModel
import com.infinity_coder.divcalendar.presentation._common.BottomDialog
import com.infinity_coder.divcalendar.presentation._common.shake
import com.infinity_coder.divcalendar.presentation._common.viewModel
import kotlinx.android.synthetic.main.bottom_dialog_add_security.*

class AddSecurityBottomDialog : BottomDialog() {

    private var clickListener: OnClickListener? = null

    private lateinit var security: ShortSecurityNetworkModel

    private val viewModel: AddSecurityViewModel by lazy {
        viewModel { AddSecurityViewModel() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialogStyle)

        security = ShortSecurityNetworkModel(
            secid = arguments!!.getString(ARGUMENT_SEC_ID, ""),
            name = arguments!!.getString(ARGUMENT_NAME, "")
        )
        viewModel.setSecurity(security)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_dialog_add_security, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnClickListener) {
            clickListener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        viewModel.getTotalSecurityPriceLiveData()
            .observe(viewLifecycleOwner, Observer(this::setTotalPrice))
        viewModel.securityPackage.observe(viewLifecycleOwner, Observer(this::addSecPackage))
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
                viewModel.setSecurityPrice(price)
            }
        })

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
        totalPriceTextView.text =
            resources.getString(R.string.total_price, price) + getString(R.string.currency_rub)
    }

    private fun addSecPackage(secPackage: SecurityPackageDbModel) {
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

        private const val SHAKE_AMPLITUDE = 8f

        fun newInstance(security: ShortSecurityNetworkModel): AddSecurityBottomDialog {
            val dialog = AddSecurityBottomDialog()
            dialog.arguments = bundleOf(
                ARGUMENT_SEC_ID to security.secid,
                ARGUMENT_NAME to security.name
            )
            return dialog
        }
    }

    interface OnClickListener {

        fun onAddSecPackageClick(securityPackage: SecurityPackageDbModel)

    }
}