package com.infinity_coder.divcalendar.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.network.RetrofitService
import com.infinity_coder.divcalendar.presentation.calendar.CalendarFragment
import com.infinity_coder.divcalendar.presentation.news.NewsFragment
import com.infinity_coder.divcalendar.presentation.portfolio.PortfolioFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val api = RetrofitService.moexApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showFragment(PortfolioFragment())

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

    private fun sendRequest() {
        GlobalScope.launch {
            val stocks = api.getStocks().stocks.toList()
            // val result = JsoupService.getStock("sibn")
            // Log.d("DivCalendar", result)
            Log.d("DivCalendar", stocks.toString())
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }
}
