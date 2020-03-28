package com.infinity_coder.divcalendar.presentation.calendar

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
    }

    private fun initUI(){
        initCalendarToolbar()
        initPaymentsRecyclerView()
    }

    private fun initCalendarToolbar(){
        calendar_toolbar.setTitle(R.string.calendar_title)
        (activity as AppCompatActivity).setActionBar(calendar_toolbar)
    }

    private fun initPaymentsRecyclerView(){
        calendar_payments_recycler_view.run {

        }
    }
}