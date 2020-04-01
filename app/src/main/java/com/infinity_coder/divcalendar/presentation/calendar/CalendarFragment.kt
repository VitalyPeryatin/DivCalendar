package com.infinity_coder.divcalendar.presentation.calendar

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.delegateadapter.delegate.diff.DiffUtilCompositeAdapter
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.setActionBar
import com.infinity_coder.divcalendar.presentation._common.viewModel
import kotlinx.android.synthetic.main.fragment_calendar.*

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private val viewModel: CalendarViewModel by lazy {
        viewModel {
            CalendarViewModel()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

        viewModel.state.observe(viewLifecycleOwner, Observer(this::updateState))
        viewModel.payments.observe(viewLifecycleOwner, Observer(this::updatePayments))
    }

    private fun initUI() {
        calendarToolbar.run {
            setTitle(R.string.calendar)
            (activity as AppCompatActivity).setActionBar(this)
        }

        calendarPaymentsRecyclerView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = DiffUtilCompositeAdapter.Builder()
                .add(getChartAdapter())
                .add(DividerAdapter())
                .add(HeaderPaymentRecyclerDelegateAdapter())
                .add(PaymentRecyclerDelegateAdapter())
                .add(FooterPaymentRecyclerDelegateAdapter())
                .build()
        }
    }

    private fun getChartAdapter():ChartPaymentRecyclerDelegateAdapter {
        return ChartPaymentRecyclerDelegateAdapter {
            calendarPaymentsRecyclerView.smoothScrollToPosition(
                viewModel.getPositionMonth(it)
            )
        }
    }

    private fun updatePayments(payments: List<IComparableItem>) {
        (calendarPaymentsRecyclerView.adapter as DiffUtilCompositeAdapter).swapData(payments)
    }

    private fun updateState(state: Int) {
        when (state) {
            CalendarViewModel.VIEW_STATE_CALENDAR_LOADING -> showLoading()

            CalendarViewModel.VIEW_STATE_CALENDAR_CONTENT -> showContent()

            CalendarViewModel.VIEW_STATE_CALENDAR_EMPTY -> showEmptyLayout()

            CalendarViewModel.VIEW_STATE_CALENDAR_NO_NETWORK -> showNoNetworkLayout()
        }
    }

    private fun showContent() {
        calendarContent.visibility = View.VISIBLE
        noNetworkLayout.visibility = View.GONE
        emptyLayout.visibility = View.GONE
        loadingLayout.visibility = View.GONE
    }

    private fun showLoading() {
        calendarContent.visibility = View.GONE
        noNetworkLayout.visibility = View.GONE
        emptyLayout.visibility = View.GONE
        loadingLayout.visibility = View.VISIBLE
    }

    private fun showEmptyLayout() {
        calendarContent.visibility = View.GONE
        noNetworkLayout.visibility = View.GONE
        emptyLayout.visibility = View.VISIBLE
        loadingLayout.visibility = View.GONE
    }

    private fun showNoNetworkLayout() {
        calendarContent.visibility = View.GONE
        noNetworkLayout.visibility = View.VISIBLE
        emptyLayout.visibility = View.GONE
        loadingLayout.visibility = View.GONE
    }
}