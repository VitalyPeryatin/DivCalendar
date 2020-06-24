package com.infinity_coder.divcalendar.presentation.tabs

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.room.Update
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.base.UpdateCallback
import com.infinity_coder.divcalendar.presentation._common.extensions.hideAllFragments
import com.infinity_coder.divcalendar.presentation.main.MainActivity
import com.infinity_coder.divcalendar.presentation.tabs.calendar.CalendarFragment
import com.infinity_coder.divcalendar.presentation.tabs.help.HelpFragment
import com.infinity_coder.divcalendar.presentation.tabs.portfolio.PortfolioFragment
import com.infinity_coder.divcalendar.presentation.settings.SettingsFragment
import kotlinx.android.synthetic.main.fragment_tabs_container.*
import java.lang.IllegalStateException

class TabsContainerFragment: Fragment(R.layout.fragment_tabs_container), UpdateCallback {

    private val menuIdToFragment = mutableMapOf<Int, Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.settings, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settingsItem -> {
                val fragment = SettingsFragment.newInstance()
                (requireActivity() as MainActivity).startFragment(fragment)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            switchFragment(getFragment(R.id.portfolioItem))
        } else {
            restoreFragments()
        }
        initUI()
    }

    override fun onUpdate() {
        val itemId = bottomNavigationView.selectedItemId
        val fragment = getFragment(itemId)
        updateFragment(fragment)
    }

    private fun initUI() {
        bottomNavigationView.setOnNavigationItemSelectedListener {

            if (bottomNavigationView.selectedItemId == it.itemId) {
                return@setOnNavigationItemSelectedListener false
            }

            val fragment = getFragment(it.itemId)
            switchFragment(fragment)

            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun switchFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().apply {
            childFragmentManager.hideAllFragments(this)
            if (childFragmentManager.fragments.contains(fragment)) {
                show(fragment)
                updateFragment(fragment)
            } else {
                add(R.id.fragmentContainerView, fragment)
            }
        }.commit()
    }

    private fun getFragment(menuId: Int): Fragment {
        if (menuIdToFragment.containsKey(menuId))
            return menuIdToFragment.getValue(menuId)

        menuIdToFragment[menuId] = when (menuId) {
            R.id.portfolioItem -> PortfolioFragment()

            R.id.calendarItem -> CalendarFragment()

            R.id.helpItem -> HelpFragment()

            else -> throw IllegalStateException("this menu does not exist")
        }

        return menuIdToFragment.getValue(menuId)
    }

    private fun restoreFragments() {
        childFragmentManager.fragments.forEach {
            val menuId = when (it) {
                is PortfolioFragment -> R.id.portfolioItem

                is CalendarFragment -> R.id.calendarItem

                is HelpFragment -> R.id.helpItem

                else -> null
            }

            if (menuId != null)
                menuIdToFragment[menuId] = it
        }
    }

    private fun updateFragment(fragment: Fragment) {
        if (fragment is UpdateCallback) {
            fragment.onUpdate()
        }
    }

    companion object{
        fun newInstance(): TabsContainerFragment{
            return TabsContainerFragment()
        }
    }
}