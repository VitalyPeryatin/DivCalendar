package com.infinity_coder.divcalendar.presentation._common

import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner

class SpinnerInteractionListener(
    private val onItemSelectedCallback:(Any)->Unit
):AdapterView.OnItemSelectedListener, View.OnTouchListener {

    private var userSelect = false

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(parent == null) return

        if(userSelect){
            onItemSelectedCallback.invoke(parent.getItemAtPosition(position))
            userSelect = false
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        userSelect = true
        return false
    }
}