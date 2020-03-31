package com.infinity_coder.divcalendar.presentation.search.addsec

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
import com.infinity_coder.divcalendar.data.db.model.SecPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.ShortStockNetworkModel
import com.infinity_coder.divcalendar.presentation._common.BottomDialog
import com.infinity_coder.divcalendar.presentation._common.shake
import com.infinity_coder.divcalendar.presentation._common.viewModel
import kotlinx.android.synthetic.main.bottom_dialog_add_stock.*

class AddSecBottomDialog : BottomDialog() {

    private var clickListener: OnClickListener? = null

    private lateinit var stock: ShortStockNetworkModel

    private val viewModel: AddSecViewModel by lazy {
        viewModel {
            AddSecViewModel()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialogStyle)

        stock = ShortStockNetworkModel(
            secid = arguments!!.getString(ARGUMENT_SEC_ID, ""),
            name = arguments!!.getString(ARGUMENT_NAME, "")
        )
        viewModel.setStock(stock)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_dialog_add_stock, container, false)
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

        viewModel.getTotalStockPriceLiveData().observe(viewLifecycleOwner, Observer(this::setTotalPrice))
        viewModel.secPackage.observe(viewLifecycleOwner, Observer(this::addSecPackage))
        viewModel.shakePriceEditText.observe(viewLifecycleOwner, Observer { shakePriceEditText() })
        viewModel.shakeCountEditText.observe(viewLifecycleOwner, Observer { shakeCountEditText() })
    }

    private fun initUI() {
        nameTextView.text = stock.name

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
                viewModel.setStockCount(securitiesCount)
            }
        })

        addStockButton.setOnClickListener {
            viewModel.addSecPackage()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTotalPrice(price: Float?) {
        if (price == null) return
        totalPriceTextView.text = resources.getString(R.string.total_price, price) + getString(R.string.currency_rub)
    }

    private fun addSecPackage(secPackage: SecPackageDbModel) {
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
        private const val ARGUMENT_NAME = "stock_name"

        private const val SHAKE_AMPLITUDE = 8f

        fun newInstance(stock: ShortStockNetworkModel): AddSecBottomDialog {
            val dialog =
                AddSecBottomDialog()
            dialog.arguments = bundleOf(
                ARGUMENT_SEC_ID to stock.secid,
                ARGUMENT_NAME to stock.name
            )
            return dialog
        }
    }

    interface OnClickListener {

        fun onAddSecPackageClick(stockPackage: SecPackageDbModel)

    }
}