package com.infinity_coder.divcalendar.presentation.search.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.infinity_coder.divcalendar.presentation._common.delegate.SecurityTypeDelegate
import com.infinity_coder.divcalendar.presentation.search.securitylist.SearchSecurityListFragment

class SearchSecurityPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val securityTypes = SecurityTypeDelegate.securityTypes

    override fun getItemCount(): Int = securityTypes.size

    override fun createFragment(position: Int): Fragment {
        return SearchSecurityListFragment.newInstance(securityTypes[position])
    }
}