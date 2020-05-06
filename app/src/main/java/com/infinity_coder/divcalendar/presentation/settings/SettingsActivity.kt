package com.infinity_coder.divcalendar.presentation.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.infinity_coder.divcalendar.BuildConfig
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.repositories.SettingsRepository
import com.infinity_coder.divcalendar.presentation._common.SwitchServerVersionDelegate
import com.infinity_coder.divcalendar.presentation._common.base.AbstractSubscriptionActivity
import com.infinity_coder.divcalendar.presentation._common.extensions.isAppAvailable
import com.infinity_coder.divcalendar.presentation._common.extensions.setActionBar
import com.infinity_coder.divcalendar.presentation._common.extensions.showSuccessfulToast
import com.infinity_coder.divcalendar.presentation.billing.dialogs.BuySubscriptionDialog
import com.infinity_coder.divcalendar.presentation.billing.dialogs.SubscriptionPurchasedDialog
import com.infinity_coder.divcalendar.presentation.settings.dialogs.ReportErrorBottomDialog
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.item_settings_switch.view.*

class SettingsActivity : AbstractSubscriptionActivity() {

    val viewModel: SettingsViewModel by lazy {
        ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initUI()

        viewModel.isAccountTaxes.observe(this, Observer(this::updateIsAccountTaxes))
    }

    private fun initUI() {
        setActionBar(settingsToolbar, hasBackNavigation = true)
        settingsToolbar.title = resources.getString(R.string.settings)
        settingsToolbar.setOnClickListener { onToolbarClick() }

        taxesItem.itemTextView.text = resources.getString(R.string.account_for_taxes)
        taxesItem.settingsSwitch.text = viewModel.taxesValue
        taxesItem.settingsSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveIsAccountTaxes(isChecked)
        }
        taxesItem.setOnClickListener {
            taxesItem.settingsSwitch.isChecked = !taxesItem.settingsSwitch.isChecked
        }
        telegramChatItem.itemTextView.text = resources.getString(R.string.telegram_chat)
        telegramChatItem.setOnClickListener { openTelegramChannel() }
        feedbackItem.itemTextView.text = resources.getString(R.string.feedback)
        feedbackItem.setOnClickListener { openSendReportDialog() }
        dataCastItem.itemTextView.text = resources.getString(R.string.send_data_cast_to_developers)
        dataCastItem.setOnClickListener { sendDataCast() }
        subscribeItem.itemTextView.text = resources.getString(R.string.purchase_subscription)
        subscribeItem.setOnClickListener {
            if (!hasSubscription()) {
                val dialog = BuySubscriptionDialog.newInstance()
                dialog.show(supportFragmentManager, BuySubscriptionDialog.TAG)
            } else {
                val dialog = SubscriptionPurchasedDialog.newInstance()
                dialog.show(supportFragmentManager, SubscriptionPurchasedDialog.TAG)
            }
        }

        tryShowCurrentVersion()
    }

    private fun openSendReportDialog() {
        val dialog = ReportErrorBottomDialog.newInstance()
        dialog.show(supportFragmentManager, ReportErrorBottomDialog.TAG)
    }

    private fun sendDataCast() {
        val parenViewModel = SettingsViewModel()
        parenViewModel.sendDataCast()
        applicationContext.showSuccessfulToast(layoutInflater, R.string.data_cast_send_successful)
    }

    private fun openTelegramChannel() {
        val telegramIntent = Intent(Intent.ACTION_VIEW, Uri.parse(SettingsRepository.TELEGRAM_GROUP_LINK))

        val telegramPackage = "org.telegram.messenger"
        val telegramXPackage = "org.thunderdog.challegram"
        if (isAppAvailable(telegramPackage)) {
            telegramIntent.setPackage(telegramPackage)
        } else if (isAppAvailable(telegramXPackage)) {
            telegramIntent.setPackage(telegramXPackage)
        }
        startActivity(telegramIntent)
    }

    private fun tryShowCurrentVersion() {
        versionTextView.visibility = View.VISIBLE
        versionTextView.text = resources.getString(R.string.version_name, BuildConfig.VERSION_NAME)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateIsAccountTaxes(isAccountTaxes: Boolean) {
        taxesItem.settingsSwitch.isChecked = isAccountTaxes
    }

    private fun onToolbarClick() {
        if (BuildConfig.DEBUG) {
            SwitchServerVersionDelegate.click()
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }
}