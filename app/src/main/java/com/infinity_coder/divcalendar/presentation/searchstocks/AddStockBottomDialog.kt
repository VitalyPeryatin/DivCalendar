package com.infinity_coder.divcalendar.presentation.searchstocks

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
import com.infinity_coder.divcalendar.data.db.model.StockPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.ShortStockNetworkModel
import com.infinity_coder.divcalendar.presentation._common.BottomDialog
import com.infinity_coder.divcalendar.presentation._common.viewModel
import kotlinx.android.synthetic.main.bottom_dialog_add_stock.*

class AddStockBottomDialog : BottomDialog() {

    private var clickListener: OnClickListener? = null

    private lateinit var stock: ShortStockNetworkModel

    private val viewModel: AddStockViewModel by lazy {
        viewModel {
            AddStockViewModel()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)

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

        nameTextView.text = stock.name

        viewModel.getTotalStockPriceLiveData().observe(viewLifecycleOwner, Observer {
            totalPriceTextView.text = "Total price: $it₽"
        })

        priceEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setStockPrice(s.toString().toFloat())
            }
        })

        countEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setStockCount(s.toString().toInt())
            }

        })

        addStockButton.setOnClickListener {
            val stockPackage = viewModel.getStockPackage()
            clickListener?.onAddStockPackageClick(stockPackage)
        }
    }

    companion object {

        private const val ARGUMENT_SEC_ID = "sec_id"
        private const val ARGUMENT_NAME = "stock_name"

        fun newInstance(stock: ShortStockNetworkModel): AddStockBottomDialog {
            val dialog = AddStockBottomDialog()
            dialog.arguments = bundleOf(
                ARGUMENT_SEC_ID to stock.secid,
                ARGUMENT_NAME to stock.name
            )
            return dialog
        }
    }

    interface OnClickListener {

        fun onAddStockPackageClick(stockPackage: StockPackageDbModel)

    }
}