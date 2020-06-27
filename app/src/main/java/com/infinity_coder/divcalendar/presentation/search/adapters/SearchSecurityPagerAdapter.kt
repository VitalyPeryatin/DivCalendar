package com.infinity_coder.divcalendar.presentation.search.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.infinity_coder.divcalendar.presentation.search.securitylist.SearchSecurityListFragment

class SearchSecurityPagerAdapter(private val securityTypes: Array<String>, fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = securityTypes.size

    override fun createFragment(position: Int): Fragment {
        return SearchSecurityListFragment.newInstance(securityTypes[position])
    }
}