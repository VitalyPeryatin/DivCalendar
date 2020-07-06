package com.infinity_coder.divcalendar.presentation.calendar.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation.import_sheet.SecuritiesFileReader
import kotlinx.android.synthetic.main.dialog_error.*

class ErrorDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val message = when (requireArguments().getString(ARGUMENT_MESSAGE_CODE)) {
            SecuritiesFileReader.ERROR_CODE_INVALID_DATA -> getString(R.string.invalid_data)
            SecuritiesFileReader.ERROR_CODE_EMPTY_PORTFOLIO_NAME -> getString(R.string.empty_portfolio_name_error)
            SecuritiesFileReader.ERROR_CODE_NOT_UNIQUE_NAME -> getString(R.string.not_new_portfolio_name_error)
            else -> getString(R.string.invalid_data)
        }

        messageTextView.text = message

        cancelButton.setOnClickListener {
            dismiss()
        }
    }

    companion object {

        const val TAG = "ErrorDialog"

        private const val ARGUMENT_MESSAGE_CODE = "ARGUMENT_MESSAGE_CODE"

        fun newInstance(messageCode: String): ErrorDialog {
            return ErrorDialog().apply {
                arguments = bundleOf(ARGUMENT_MESSAGE_CODE to messageCode)
            }
        }
    }
}