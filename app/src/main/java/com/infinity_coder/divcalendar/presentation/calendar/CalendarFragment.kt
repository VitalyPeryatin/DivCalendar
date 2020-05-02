package com.infinity_coder.divcalendar.presentation.calendar

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.delegateadapter.delegate.diff.DiffUtilCompositeAdapter
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.repositories.RateRepository
import com.infinity_coder.divcalendar.presentation._common.SpinnerInteractionListener
import com.infinity_coder.divcalendar.presentation._common.base.UpdateCallback
import com.infinity_coder.divcalendar.presentation._common.extensions.setActionBar
import com.infinity_coder.divcalendar.presentation._common.extensions.viewModel
import com.infinity_coder.divcalendar.presentation.calendar.adapters.*
import com.infinity_coder.divcalendar.presentation.calendar.dialogs.ChangePaymentDialog
import com.infinity_coder.divcalendar.presentation.calendar.models.PaymentPresentationModel
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.layout_stub_empty.view.*
import java.io.File
import java.util.*

class CalendarFragment : Fragment(R.layout.fragment_calendar),
    UpdateCallback {

    val viewModel: CalendarViewModel by lazy {
        viewModel { CalendarViewModel() }
    }

    private var items: Array<String> = arrayOf()

    private val paymentClickListener = object : PaymentRecyclerDelegateAdapter.OnItemClickListener {
        override fun onItemClick(item: PaymentPresentationModel) {
            val dialog = ChangePaymentDialog.newInstance(item)
            dialog.show(childFragmentManager, ChangePaymentDialog.TAG)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

        viewModel.state.observe(viewLifecycleOwner, Observer(this::updateState))
        viewModel.payments.observe(viewLifecycleOwner, Observer(this::updatePayments))
        viewModel.currentYear.observe(viewLifecycleOwner, Observer(this::updateCurrentYear))
        viewModel.isIncludeTaxes.observe(viewLifecycleOwner, Observer(this::setIsIncludedTexes))
        viewModel.sendFileEvent.observe(viewLifecycleOwner, Observer(this::sendFile))
        viewModel.portfolioNameTitleEvent.observe(viewLifecycleOwner, Observer(this::setPortfolioName))
    }

    override fun onStart() {
        super.onStart()

        viewModel.updateData(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.calendar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.uploadItem -> {
                viewModel.exportData(requireContext())
            }
        }
        return true
    }

    private fun sendFile(file: File?) {
        if (file == null) return

        val fileUri = FileProvider.getUriForFile(
            context!!, context!!.applicationContext.packageName.toString() + ".provider", file
        )
        val intentShareFileBuilder = ShareCompat.IntentBuilder.from(requireActivity())
            .setType("application/*")
            .setStream(fileUri)
            .intent
            .setAction(Intent.ACTION_SEND)
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val exportPayments = resources.getString(R.string.export_payments)

        startActivity(Intent.createChooser(intentShareFileBuilder, exportPayments))
    }

    override fun onUpdate() {
        viewModel.loadAllPayments(requireContext())
    }

    private fun initUI() {
        calendarToolbar.run {
            setTitle(R.string.calendar)
            (activity as AppCompatActivity).setActionBar(this)
        }

        initSpinnerYear()

        val checkedCurrencyRadioButton = when (viewModel.getDisplayCurrency()) {
            RateRepository.RUB_RATE -> rubRadioButton
            RateRepository.USD_RATE -> usdRadioButton
            else -> null
        }

        checkedCurrencyRadioButton?.isChecked = true

        rubRadioButton.setOnCheckedChangeListener(this::checkCurrency)
        usdRadioButton.setOnCheckedChangeListener(this::checkCurrency)

        calendarPaymentsRecyclerView.layoutManager = LinearLayoutManager(context)
        (calendarPaymentsRecyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        calendarPaymentsRecyclerView.adapter = DiffUtilCompositeAdapter.Builder()
            .add(getChartAdapter())
            .add(DividerDelegateAdapter())
            .add(HeaderPaymentRecyclerDelegateAdapter())
            .add(PaymentRecyclerDelegateAdapter(paymentClickListener))
            .add(FooterPaymentRecyclerDelegateAdapter())
            .build()

        emptySecuritiesLayout.emptyTextView.text = resources.getString(R.string.empty_securities)
    }

    private fun initSpinnerYear() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        items = Array(MAX_YEARS_CHOICE) { index -> (currentYear + index).toString() }
        yearSpinner.adapter = SpinnerAdapter(requireContext(), items).apply {
            setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        }

        val spinnerInteractionListener = SpinnerInteractionListener {
            viewModel.selectYear(requireContext(), it as String)
        }
        yearSpinner.onItemSelectedListener = spinnerInteractionListener
        yearSpinner.setOnTouchListener(spinnerInteractionListener)
    }

    private fun checkCurrency(radioButton: CompoundButton, isChecked: Boolean) {
        if (!isChecked) return

        when (radioButton) {
            rubRadioButton -> viewModel.setDisplayCurrency(
                requireContext(),
                RateRepository.RUB_RATE
            )
            usdRadioButton -> viewModel.setDisplayCurrency(
                requireContext(),
                RateRepository.USD_RATE
            )
        }
    }

    private fun getChartAdapter(): ChartPaymentRecyclerDelegateAdapter {
        val adapter = ChartPaymentRecyclerDelegateAdapter()
        adapter.onItemClickListener = object : ChartPaymentRecyclerDelegateAdapter.ChartItemClickListener {
            override fun onClick(numberMonth: Int) {
                calendarPaymentsRecyclerView.smoothScrollToPosition(
                    viewModel.getFooterPositionByMonthNumber(numberMonth)
                )
            }
        }
        return adapter
    }

    private fun setIsIncludedTexes(isIncludedTaxes: Boolean?) {
        if (isIncludedTaxes == null) return

        val colorId: Int
        if (isIncludedTaxes) {
            taxesTextView.text = resources.getString(R.string.taxes_included)
            colorId = R.color.colorAccent
        } else {
            taxesTextView.text = resources.getString(R.string.taxes_excluding)
            colorId = R.color.colorSecondary
        }
        taxesTextView.setTextColor(ContextCompat.getColor(requireContext(), colorId))
    }

    private fun updatePayments(payments: List<IComparableItem>) {
        val state = calendarPaymentsRecyclerView.layoutManager!!.onSaveInstanceState()
        (calendarPaymentsRecyclerView.adapter as DiffUtilCompositeAdapter).swapData(payments)
        calendarPaymentsRecyclerView.layoutManager!!.onRestoreInstanceState(state)
    }

    private fun setPortfolioName(portfolioName: String) {
        calendarToolbar.subtitle = portfolioName
    }

    private fun updateCurrentYear(year: String) {
        emptyLayout.emptyTextView.text = resources.getString(R.string.empty_payments, year)
        yearSpinner.setSelection(items.indexOf(year))
    }

    private fun updateState(state: Int) {
        calendarContent.visibility = View.GONE
        yearSpinner.visibility = View.GONE
        currencyRadioGroup.visibility = View.GONE
        calendarPaymentsRecyclerView.visibility = View.GONE
        loadingLayout.visibility = View.GONE
        emptyLayout.visibility = View.GONE
        noNetworkLayout.visibility = View.GONE
        emptySecuritiesLayout.visibility = View.GONE
        taxesTextView.visibility = View.GONE

        when (state) {
            CalendarViewModel.VIEW_STATE_CALENDAR_CONTENT -> {
                calendarContent.visibility = View.VISIBLE
                yearSpinner.visibility = View.VISIBLE
                currencyRadioGroup.visibility = View.VISIBLE
                calendarPaymentsRecyclerView.visibility = View.VISIBLE
                taxesTextView.visibility = View.VISIBLE
            }
            CalendarViewModel.VIEW_STATE_CALENDAR_LOADING -> {
                loadingLayout.visibility = View.VISIBLE
            }
            CalendarViewModel.VIEW_STATE_CALENDAR_EMPTY -> {
                calendarContent.visibility = View.VISIBLE
                yearSpinner.visibility = View.VISIBLE
                currencyRadioGroup.visibility = View.INVISIBLE
                emptyLayout.visibility = View.VISIBLE
            }
            CalendarViewModel.VIEW_STATE_CALENDAR_NO_NETWORK -> {
                noNetworkLayout.visibility = View.VISIBLE
            }
            CalendarViewModel.VIEW_STATE_CALENDAR_EMPTY_SECURITIES -> {
                emptySecuritiesLayout.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        private const val MAX_YEARS_CHOICE = 2
    }
}