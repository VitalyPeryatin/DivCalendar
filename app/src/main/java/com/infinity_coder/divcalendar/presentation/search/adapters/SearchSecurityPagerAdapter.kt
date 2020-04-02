package com.infinity_coder.divcalendar.presentation.search.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.infinity_coder.divcalendar.presentation._common.SecurityTypeDelegate
import com.infinity_coder.divcalendar.presentation.search.securitylist.SearchSecurityListFragment

class SearchSecurityPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val securityTypes = SecurityTypeDelegate.securityTypes
    private val securityListFragments = generateSearchListFragments()

    override fun getItemCount(): Int = securityTypes.size

    override fun createFragment(position: Int): Fragment {
        return securityListFragments[position]
    }

    private fun generateSearchListFragments(): List<SearchSecurityListFragment> {
        return MutableList(securityTypes.size) { position -> generateSearchListFragment(position) }
    }

    private fun generateSearchListFragment(position: Int): SearchSecurityListFragment {
        return SearchSecurityListFragment.newInstance(securityTypes[position])
    }

    fun executeQuery(query: String) {
        securityListFragments.forEach { it.executeQuery(query) }
    }

    fun updateMarket() {
        securityListFragments.forEach { it.updateMarket() }
    }
}