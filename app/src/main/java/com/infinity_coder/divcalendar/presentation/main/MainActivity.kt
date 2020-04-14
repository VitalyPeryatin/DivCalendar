package com.infinity_coder.divcalendar.presentation.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation.buysubscription.BuySubscriptionActivity
import com.infinity_coder.divcalendar.presentation.calendar.CalendarFragment
import com.infinity_coder.divcalendar.presentation.news.NewsFragment
import com.infinity_coder.divcalendar.presentation.portfolio.PortfolioFragment
import com.infinity_coder.divcalendar.presentation.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), BillingProcessor.IBillingHandler {

    private val LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjqNWSexMmjxECoBCFKYAGTKAsesoYtvrxDk9uTDwg4A+sBA5YD7rvKBLEtQ2bfMLlKb0FQg6PZvmABtkb8oKAUEZCPVGuE6Ep4/pxWa/JO0Lz0mvZsLuh+8Obi8Bm1I3WG2kStaaeW+rYmA0r7m0vgd6XMa0Jl2ZImF+VFwcjHdL1wnik6WZNEMcme/Czvkxz06xADeapX7AocW7AMvgxFvbHdVqXTSoSzspfeNPB+/8745CN6B6HU4NlxCODjwkg3msYPROqNbO1rnoaWs4JBk+d6G67RYcsYxrJyZZYPNB6sKEZPjcokzwdDrcZ8fMsZF9j5jwpPdSR4epvwnWowIDAQAB"
    private var billingProcessor: BillingProcessor? = null
    private val subscriptionId = "standard_subscription"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            showFragment(PortfolioFragment())
        }

        initUI()

        billingProcessor = BillingProcessor(this, LICENSE_KEY, this)
        billingProcessor?.initialize()
    }

    private fun initUI() {
        bottomNavigationView.setOnNavigationItemSelectedListener {

            if (bottomNavigationView.selectedItemId == it.itemId) {
                return@setOnNavigationItemSelectedListener false
            }

            when (it.itemId) {
                R.id.portfolioItem -> showFragment(PortfolioFragment())

                R.id.calendarItem -> showFragment(CalendarFragment())

                R.id.newsItem -> showFragment(NewsFragment())
            }

            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (billingProcessor == null || !billingProcessor!!.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return true
    }

    override fun onDestroy() {
        billingProcessor?.release()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settingsItem -> {
                openSettingsActivity()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun openSettingsActivity() {
        val intent = SettingsActivity.getIntent(this)
        startActivity(intent)
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }

    // Billing callbacks

    override fun onBillingInitialized() {
        showToast("MainActivity onBillingInitialized()")
        billingProcessor?.loadOwnedPurchasesFromGoogle()
        val isSubscribed = billingProcessor!!.isSubscribed(subscriptionId)
        if (!isSubscribed) {
            val intent = BuySubscriptionActivity.getIntent(this)
            startActivity(intent)
            finish()
        }
        showToast("MainActivity isSubscribed: $isSubscribed")
    }

    override fun onPurchaseHistoryRestored() {
        showToast("MainActivity onPurchaseHistoryRestored()")
    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        showToast("MainActivity onProductPurchased($productId, ${details?.purchaseInfo})")
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        showToast("MainActivity onBillingError: errorCode = $errorCode")
    }

    private fun showToast(message: String) {
        Log.d("Billing", message)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}