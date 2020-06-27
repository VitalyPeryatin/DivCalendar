package com.infinity_coder.divcalendar.presentation.onboarding

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.delegate.TelegramChannelDelegate
import com.infinity_coder.divcalendar.presentation.onboarding.adapters.OnboardingPageAdapter
import com.infinity_coder.divcalendar.presentation.onboarding.models.OnboardingPageModel
import kotlinx.android.synthetic.main.fragment_onboarding.*

class OnBoardingFragment : Fragment(R.layout.fragment_onboarding) {

    val viewModel: OnboardingViewModel by lazy {
        ViewModelProvider(this).get(OnboardingViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.pagePosition.observe(viewLifecycleOwner, Observer(this::setPagePosition))

        initUI()
    }

    private fun initUI() {
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.main_background)

        initViewPager()
        indicator.attachToPager(onboardingViewPager)

        fartherButton.setOnClickListener {
            if (onboardingViewPager.currentItem != pages.size - 1) {
                onboardingViewPager.currentItem = onboardingViewPager.currentItem + 1
            }
        }

        endButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun initViewPager() {
        val adapter = OnboardingPageAdapter(
            object : OnboardingPageAdapter.OnboardingPageCallback {
                override fun onClickTelegramChannelLink() {
                    val intent = TelegramChannelDelegate.getOpenTelegramChannelIntent(requireContext())
                    startActivity(intent)
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
            OnboardingPageModel(
                R.drawable.onboarding_portfolio_image,
                R.string.onboarding_portfolio_title,
                R.string.onboarding_portfolio_message
            ),
            OnboardingPageModel(
                R.drawable.onboarding_calendar_image,
                R.string.onboarding_calendar_title,
                R.string.onboarding_calendar_message
            ),
            OnboardingPageModel(
                R.drawable.onboarding_help_image,
                R.string.onboarding_help_title,
                R.string.onboarding_help_message
            )
        )

        fun newInstance(): OnBoardingFragment {
            return OnBoardingFragment()
        }
    }
}