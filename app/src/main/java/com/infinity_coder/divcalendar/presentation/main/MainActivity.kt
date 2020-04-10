package com.infinity_coder.divcalendar.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.android.billingclient.api.BillingFlowParams
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.BillingDelegate
import com.infinity_coder.divcalendar.presentation._common.viewModel
import com.infinity_coder.divcalendar.presentation.calendar.CalendarFragment
import com.infinity_coder.divcalendar.presentation.news.NewsFragment
import com.infinity_coder.divcalendar.presentation.portfolio.PortfolioFragment
import com.infinity_coder.divcalendar.presentation.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        viewModel { MainViewModel() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            showFragment(PortfolioFragment())
        }

        initUI()

        viewModel.billingParamsEvent.observe(this, Observer(this::launchBillingFlow))
        viewModel.isPurchased.observe(this, Observer(this::checkSubscription))

        viewModel.startConnection()
        viewModel.buySubscription()
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

    private fun launchBillingFlow(billingFlowParams: BillingFlowParams) {
        BillingDelegate.launchBillingFlow(this, billingFlowParams)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return true
    }

    private fun checkSubscription(hasSubscription: Boolean) {
        if (!hasSubscription) {
            Log.d("Billing", "No subscription")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settingsItem -> openSettingsActivity()
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
}