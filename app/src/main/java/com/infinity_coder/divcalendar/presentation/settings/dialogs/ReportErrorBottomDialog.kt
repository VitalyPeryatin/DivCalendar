package com.infinity_coder.divcalendar.presentation.settings.dialogs

import android.content.Context
import android.os.Bundle
import android.text.InputFilter.LengthFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.BottomDialog
import com.infinity_coder.divcalendar.presentation._common.extensions.showSuccessfulToast
import com.infinity_coder.divcalendar.presentation.settings.SettingsActivity
import com.infinity_coder.divcalendar.presentation.settings.SettingsViewModel
import kotlinx.android.synthetic.main.bottom_dialog_report_error.*

class ReportErrorBottomDialog : BottomDialog() {

    private lateinit var parentViewModel: SettingsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is SettingsActivity) {
            parentViewModel = context.viewModel
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_dialog_report_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancelButton.setOnClickListener { dismiss() }

        sendButton.setOnClickListener {
            sendReport()
            dismiss()
        }

        reportEditText.filters = arrayOf(LengthFilter(MAX_MESSAGE_LENGTH))
    }

    private fun sendReport() {
        val reportMessage = reportEditText.text.toString()
        if (reportMessage.isNotEmpty()) {
            parentViewModel.reportError(reportMessage)
            requireContext().showSuccessfulToast(layoutInflater, R.string.send_successful)
        }
    }

    companion object {

        private const val MAX_MESSAGE_LENGTH = 300

        const val TAG = "ReportErrorBottomDialog"

        fun newInstance(): ReportErrorBottomDialog {
            return ReportErrorBottomDialog()
        }
    }
}