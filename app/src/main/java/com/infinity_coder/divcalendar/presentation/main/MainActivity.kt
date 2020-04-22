package com.infinity_coder.divcalendar.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.AbstractSubscriptionActivity
import com.infinity_coder.divcalendar.presentation._common.extensions.hideAllFragments
import com.infinity_coder.divcalendar.presentation._common.extensions.isAddedFragmentManager
import com.infinity_coder.divcalendar.presentation.calendar.CalendarFragment
import com.infinity_coder.divcalendar.presentation.news.NewsFragment
import com.infinity_coder.divcalendar.presentation.portfolio.PortfolioFragment
import com.infinity_coder.divcalendar.presentation.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IllegalStateException

class MainActivity : AbstractSubscriptionActivity() {

    private val menuIdToFragment = mutableMapOf<Int,Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            showFragment(getFragment(R.id.portfolioItem))
        }else{
            restoreFragments()
        }

        initUI()
    }

    private fun initUI() {
        bottomNavigationView.setOnNavigationItemSelectedListener {

            if (bottomNavigationView.selectedItemId == it.itemId) {
                return@setOnNavigationItemSelectedListener false
            }

            val fragment = getFragment(it.itemId)
            showFragment(fragment)

            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return true
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

    private fun getFragment(menuId:Int):Fragment{
        if(menuIdToFragment.containsKey(menuId))
            return menuIdToFragment.getValue(menuId)

        menuIdToFragment[menuId] = when (menuId) {
            R.id.portfolioItem -> PortfolioFragment()

            R.id.calendarItem -> CalendarFragment()

            R.id.newsItem -> NewsFragment()

            else -> throw IllegalStateException("this menu does not exist")
        }

        return menuIdToFragment.getValue(menuId)
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            supportFragmentManager.hideAllFragments(this)
            if(fragment.isAddedFragmentManager(supportFragmentManager)){
                show(fragment)
            }else{
                add(R.id.fragmentContainerView, fragment)
            }
        }.commit()
    }

    private fun restoreFragments(){
        supportFragmentManager.fragments.forEach {
            val menuId = when(it.javaClass.name.split(".").last()){
                "PortfolioFragment" -> R.id.portfolioItem

                "CalendarFragment" -> R.id.calendarItem

                "NewsFragment" -> R.id.newsItem

                else -> null
            }

            if(menuId != null)
                menuIdToFragment[menuId] = it
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("EXIT", true)
            }
        }
    }
}