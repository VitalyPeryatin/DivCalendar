package com.infinity_coder.divcalendar.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.infinity_coder.divcalendar.BuildConfig
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.SwitchServerVersionDelegate
import com.infinity_coder.divcalendar.presentation._common.base.AbstractSubscriptionActivity
import com.infinity_coder.divcalendar.presentation.billing.dialogs.BuySubscriptionDialog
import com.infinity_coder.divcalendar.presentation.billing.dialogs.SubscriptionPurchasedDialog
import com.infinity_coder.divcalendar.presentation.settings.dialogs.ChangeThemeBottomDialog
import com.infinity_coder.divcalendar.presentation.settings.dialogs.ReportErrorBottomDialog
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.item_settings_switch.view.*

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    val viewModel: SettingsViewModel by lazy {
        ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        viewModel.hideCopecks.observe(viewLifecycleOwner, Observer(this::updateHideCopecks))
        viewModel.isAccountTaxes.observe(viewLifecycleOwner, Observer(this::updateIsAccountTaxes))
        viewModel.isScrollingCalendarForCurrentMonth.observe(viewLifecycleOwner, Observer(this::updateIsScrollingCalendarForCurrentMonth))
    }

    private fun initUI() {
        settingsToolbar.title = resources.getString(R.string.settings)
        settingsToolbar.setOnClickListener { onToolbarClick() }
        settingsToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        settingsToolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

        taxesItem.itemTextView.text = getString(R.string.account_for_taxes)
        taxesItem.settingsSwitch.text = viewModel.taxesValue
        taxesItem.settingsSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveIsAccountTaxes(isChecked)
        }
        taxesItem.setOnClickListener {
            taxesItem.settingsSwitch.isChecked = !taxesItem.settingsSwitch.isChecked
        }

        hideCopecksItem.itemTextView.text = getString(R.string.hide_copecks)
        hideCopecksItem.settingsSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveIsHideCopecks(isChecked)
        }
        hideCopecksItem.setOnClickListener {
            hideCopecksItem.settingsSwitch.isChecked = !hideCopecksItem.settingsSwitch.isChecked
        }

        scrollingCalendarForCurrentMonthItem.itemTextView.text = getString(R.string.scrolling_calendar_for_current_year)
        scrollingCalendarForCurrentMonthItem.settingsSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveIsScrollingCalendarForCurrentMonth(isChecked)
        }
        scrollingCalendarForCurrentMonthItem.setOnClickListener {
            scrollingCalendarForCurrentMonthItem.settingsSwitch.isChecked = !scrollingCalendarForCurrentMonthItem.settingsSwitch.isChecked
        }

        darkThemeItem.itemTextView.text = getString(R.string.dark_theme)
        darkThemeItem.setOnClickListener {
            val dialog = ChangeThemeBottomDialog.newInstance()
            dialog.show(childFragmentManager, ChangeThemeBottomDialog.TAG)
        }

        feedbackItem.itemTextView.text = getString(R.string.feedback)
        feedbackItem.setOnClickListener {
            val dialog = ReportErrorBottomDialog.newInstance()
            dialog.show(childFragmentManager, ReportErrorBottomDialog.TAG)
        }

        subscribeItem.itemTextView.text = getString(R.string.purchase_subscription)
        subscribeItem.setOnClickListener {
            val billingActivity = requireActivity() as AbstractSubscriptionActivity
            if (!billingActivity.hasSubscription()) {
                val dialog = BuySubscriptionDialog.newInstance()
                dialog.show(childFragmentManager, BuySubscriptionDialog.TAG)
            } else {
                val dialog = SubscriptionPurchasedDialog.newInstance()
                dialog.show(childFragmentManager, SubscriptionPurchasedDialog.TAG)
            }
        }

        versionTextView.visibility = View.VISIBLE
        versionTextView.text = getString(R.string.version_name, BuildConfig.VERSION_NAME)
    }

    private fun updateIsAccountTaxes(isAccountTaxes: Boolean) {
        taxesItem.settingsSwitch.isChecked = isAccountTaxes
    }

    private fun updateHideCopecks(hideCopecks: Boolean) {
        hideCopecksItem.settingsSwitch.isChecked = hideCopecks
    }

    private fun updateIsScrollingCalendarForCurrentMonth(isScrollingCalendarForCurrentMonth: Boolean) {
        scrollingCalendarForCurrentMonthItem.settingsSwitch.isChecked = isScrollingCalendarForCurrentMonth
    }

    private fun onToolbarClick() {
        if (BuildConfig.DEBUG) {
            SwitchServerVersionDelegate.click()
        }
    }

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}