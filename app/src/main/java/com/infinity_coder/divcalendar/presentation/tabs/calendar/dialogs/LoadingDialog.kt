package com.infinity_coder.divcalendar.presentation.tabs.calendar.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.infinity_coder.divcalendar.R

class LoadingDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_loading, container, false)
    }

    companion object {

        const val TAG = "LoadingDialog"

        fun newInstance(): LoadingDialog {
            return LoadingDialog()
        }
    }
}