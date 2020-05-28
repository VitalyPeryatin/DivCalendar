package com.infinity_coder.divcalendar.presentation.help

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

import com.infinity_coder.divcalendar.R
import kotlinx.android.synthetic.main.fragment_help.*

class HelpFragment : Fragment(R.layout.fragment_help) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        helpToolbar.title = getString(R.string.help)
    }
}
