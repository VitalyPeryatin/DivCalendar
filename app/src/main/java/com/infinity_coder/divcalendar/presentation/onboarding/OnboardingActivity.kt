package com.infinity_coder.divcalendar.presentation.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.delegate.TelegramChannelDelegate
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnboardingActivity : AppCompatActivity() {

    val viewModel: OnboardingViewModel by lazy {
        ViewModelProvider(this).get(OnboardingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewModel.pagePosition.observe(this, Observer(this::setPagePosition))

        initUI()
    }

    private fun initUI() {
        initViewPager()
        indicator.attachToPager(onboardingViewPager)

        fartherButton.setOnClickListener {
            if (onboardingViewPager.currentItem != pages.size - 1) {
                onboardingViewPager.currentItem = onboardingViewPager.currentItem + 1
            }
        }

        endButton.setOnClickListener {
            finish()
        }
    }

    private fun initViewPager() {
        val adapter = OnboardingPageAdapter(object : OnboardingPageAdapter.OnboardingPageCallback {
            override fun onClickTelegramChannelLink() {
                startActivity(TelegramChannelDelegate.getOpenTelegramChannelIntent(this@OnboardingActivity))
            }
        })
        adapter.update(pages)
        onboardingViewPager.adapter = adapter
        onboardingViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.updatePagePosition(position)
            }
        })
    }

    private fun setPagePosition(position: Int) {
        fartherButton.visibility = if (position != pages.size - 1) View.VISIBLE else View.GONE
        endButton.visibility = if (position == pages.size - 1) View.VISIBLE else View.GONE
    }

    companion object {
        private val pages = listOf(
            OnboardingPageModel(R.drawable.onboarding_portfolio_image, R.string.onboarding_portfolio_title, R.string.onboarding_portfolio_message),
            OnboardingPageModel(R.drawable.onboarding_calendar_image, R.string.onboarding_calendar_title, R.string.onboarding_calendar_message),
            OnboardingPageModel(R.drawable.onboarding_help_image, R.string.onboarding_help_title, R.string.onboarding_help_message)
        )
    }
}