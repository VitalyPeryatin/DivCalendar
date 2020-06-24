package com.infinity_coder.divcalendar.presentation.tabs.help

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.delegate.TelegramChannelDelegate
import com.infinity_coder.divcalendar.presentation.main.MainActivity
import com.infinity_coder.divcalendar.presentation.onboarding.OnBoardingFragment
import kotlinx.android.synthetic.main.fragment_help.*
import kotlinx.android.synthetic.main.item_help_normal.view.*

class HelpFragment : Fragment(R.layout.fragment_help) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        helpToolbar.title = getString(R.string.help)

        telegramQuestionItem.itemTextView.text = getString(R.string.telegram_question)
        telegramQuestionItem.itemImageView.setImageResource(R.drawable.ic_telegram)
        telegramQuestionItem.setOnClickListener {
            val intent = TelegramChannelDelegate.getOpenTelegramChannelIntent(requireContext())
            startActivity(intent)
        }

        helpToUseItem.itemTextView.text = getString(R.string.help_to_use)
        helpToUseItem.itemImageView.setImageResource(R.drawable.ic_help_to_use)
        helpToUseItem.setOnClickListener {
            val fragment = OnBoardingFragment.newInstance()
            (requireActivity() as MainActivity).startFragment(fragment)
        }
    }
}