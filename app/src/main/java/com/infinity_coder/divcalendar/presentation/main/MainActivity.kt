package com.infinity_coder.divcalendar.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.base.AbstractSubscriptionActivity
import com.infinity_coder.divcalendar.presentation._common.base.UpdateCallback
import com.infinity_coder.divcalendar.presentation.tabs.TabsContainerFragment

class MainActivity : AbstractSubscriptionActivity() {

    private val backStackFragments = mutableListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            startFragment(TabsContainerFragment.newInstance())
        }else{
            supportFragmentManager.fragments.forEach {
                if(it !is SupportRequestManagerFragment)
                    backStackFragments.add(it)
            }
        }
    }

    fun startFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        if (backStackFragments.isNotEmpty()) {
            transaction.hide(backStackFragments.last())
        }
        transaction.add(R.id.fragmentContainerView, fragment)
        backStackFragments.add(fragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        if (backStackFragments.size > 1) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.remove(backStackFragments.last())
            backStackFragments.remove(backStackFragments.last())
            transaction.show(backStackFragments.last())
            transaction.commit()
            updateCurrentFragment()
        } else {
            super.onBackPressed()
        }
    }

    private fun updateCurrentFragment() {
        if (backStackFragments.isNotEmpty()) {
            (backStackFragments.last() as UpdateCallback).onUpdate()
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