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
import com.infinity_coder.divcalendar.presentation._common.delegate.AppThemeDelegate
import com.infinity_coder.divcalendar.presentation.tabs.TabsContainerFragment

class MainActivity : AbstractSubscriptionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AppThemeDelegate.setAppTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            startFragment(TabsContainerFragment.newInstance())
        }
    }

    fun startFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.addToBackStack(fragment.toString())
        transaction.commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            super.onBackPressed()
        } else {
            finish()
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