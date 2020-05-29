package com.infinity_coder.divcalendar.presentation.help

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.delegate.TelegramChannelDelegate
import com.infinity_coder.divcalendar.presentation.onboarding.OnboardingActivity
import kotlinx.android.synthetic.main.fragment_help.*
import kotlinx.android.synthetic.main.item_help_normal.view.*

class HelpFragment : Fragment(R.layout.fragment_help) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        helpToolbar.title = getString(R.string.help)

        telegramQuestionItem.itemTextView.text = getString(R.string.telegram_question)
        telegramQuestionItem.itemImageView.setImageResource(R.drawable.ic_telegram)
        telegramQuestionItem.setOnClickListener {
            startActivity(TelegramChannelDelegate.getOpenTelegramChannelIntent(requireContext()))
        }

        helpToUseItem.itemTextView.text = getString(R.string.help_to_use)
        helpToUseItem.itemImageView.setImageResource(R.drawable.ic_help_to_use)
        helpToUseItem.setOnClickListener {
            startActivity(Intent(requireContext(), OnboardingActivity::class.java))
        }
    }
}