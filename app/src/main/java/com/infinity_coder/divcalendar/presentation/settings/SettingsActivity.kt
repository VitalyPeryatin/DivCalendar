package com.infinity_coder.divcalendar.presentation.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.infinity_coder.divcalendar.BuildConfig
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.AbstractSubscriptionActivity
import com.infinity_coder.divcalendar.presentation._common.setActionBar
import com.infinity_coder.divcalendar.presentation._common.viewModel
import com.infinity_coder.divcalendar.presentation.billing.dialogs.BuySubscriptionDialog
import com.infinity_coder.divcalendar.presentation.billing.dialogs.SubscriptionPurchasedDialog
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.item_settings_switch.view.*

class SettingsActivity : AbstractSubscriptionActivity() {

    private val viewModel: SettingsViewModel by lazy {
        viewModel { SettingsViewModel() }
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

        taxesItem.itemTextView.text = resources.getString(R.string.account_for_taxes)
        taxesItem.settingsSwitch.text = viewModel.taxesValue
        taxesItem.settingsSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveIsAccountTaxes(isChecked)
        }
        taxesItem.setOnClickListener {
            taxesItem.settingsSwitch.isChecked = !taxesItem.settingsSwitch.isChecked
        }
        telegramChatItem.itemTextView.text = resources.getString(R.string.telegram_chat)
        feedbackItem.itemTextView.text = resources.getString(R.string.feedback)
        subscribeItem.itemTextView.text = resources.getString(R.string.purchase_subscription)
        subscribeItem.setOnClickListener {
            if (!hasSubscription()) {
                val dialog = BuySubscriptionDialog.newInstance()
                dialog.show(supportFragmentManager, BuySubscriptionDialog::class.toString())
            } else {
                val dialog = SubscriptionPurchasedDialog.newInstance()
                dialog.show(supportFragmentManager, SubscriptionPurchasedDialog::class.toString())
            }
        }

        tryShowCurrentVersion()
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

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }
}