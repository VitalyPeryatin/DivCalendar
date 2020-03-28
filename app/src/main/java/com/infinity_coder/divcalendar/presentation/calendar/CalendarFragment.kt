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
import com.infinity_coder.divcalendar.presentation._common.visibilityGone
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.layout_loading.*

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
    }

    private fun initUI() {
        calendarToolbar.run {
            setTitle(R.string.calendar_title)
            (activity as AppCompatActivity).setActionBar(this)
        }

        calendarPaymentsRecyclerView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = DiffUtilCompositeAdapter.Builder()
                .add(DividerAdapter())
                .add(HeaderPaymentRecyclerDelegateAdapter())
                .add(PaymentRecyclerDelegateAdapter())
                .add(FooterPaymentRecyclerDelegateAdapter())
                .build()
        }
    }

    private fun updateState(state: State) {
        when (state) {
            is State.Progress -> {
                loadingProgressBar.visibilityGone(true)
                calendarPaymentsRecyclerView.visibilityGone(false)
            }
            is State.Data<*> -> {
                loadingProgressBar.visibilityGone(false)
                calendarPaymentsRecyclerView.visibilityGone(true)
                (calendarPaymentsRecyclerView.adapter as DiffUtilCompositeAdapter).swapData(state.data as List<IComparableItem>)
            }
            is State.Error -> {

            }
        }
    }
}