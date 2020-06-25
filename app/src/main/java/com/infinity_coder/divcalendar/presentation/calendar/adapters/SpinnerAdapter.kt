package com.infinity_coder.divcalendar.presentation.calendar.adapters

import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.infinity_coder.divcalendar.R
import kotlinx.android.synthetic.main.simple_spinner_item.view.*

class SpinnerAdapter(context: Context, objects: Array<String>) :
    ArrayAdapter<String>(context, R.layout.simple_spinner_item, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val spinnerPadding = parent.context.resources.getDimension(R.dimen.spinner_padding).toInt()
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            (parent.parent as View).setPadding(0, 0, spinnerPadding, 0)
            view.spinnerItem.setPadding(0, 0, spinnerPadding, 0)
        }
        return view
    }
}