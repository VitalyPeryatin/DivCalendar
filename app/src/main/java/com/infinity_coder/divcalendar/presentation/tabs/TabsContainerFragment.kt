package com.infinity_coder.divcalendar.presentation.tabs

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.base.UpdateCallback
import com.infinity_coder.divcalendar.presentation._common.extensions.hideAllFragments
import com.infinity_coder.divcalendar.presentation.calendar.CalendarFragment
import com.infinity_coder.divcalendar.presentation.help.HelpFragment
import com.infinity_coder.divcalendar.presentation.portfolio.PortfolioFragment
import kotlinx.android.synthetic.main.fragment_tabs_container.*
import java.lang.IllegalStateException

class TabsContainerFragment : Fragment(R.layout.fragment_tabs_container) {

    private val viewModel: TabsContainerViewModel by lazy {
        ViewModelProvider(this).get(TabsContainerViewModel::class.java)
    }

    private val menuIdToFragment = mutableMapOf<Int, Fragment>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.itemMenuId.observe(viewLifecycleOwner, Observer(this::switchFragment))

        if (savedInstanceState != null) {
            restoreFragments()
        }

        initUI()
    }

    private fun initUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        } else {
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
        }

        bottomNavigationView.setOnNavigationItemSelectedListener {
            if (bottomNavigationView.selectedItemId == it.itemId) {
                return@setOnNavigationItemSelectedListener false
            }
            viewModel.switchScreen(it.itemId)

            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun switchFragment(itemMenuId: Int) {
        val fragment = getFragment(itemMenuId)
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

    companion object {
        fun newInstance(): TabsContainerFragment {
            return TabsContainerFragment()
        }
    }
}