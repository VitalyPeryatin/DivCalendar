package com.infinity_coder.divcalendar.presentation._common.text_watchers

import android.text.Editable
import android.text.TextWatcher

open class OpenTextWatcher : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}