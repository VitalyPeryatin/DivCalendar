package com.infinity_coder.divcalendar.presentation._common.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> Fragment.viewModel(crossinline f: () -> T): T {
    return ViewModelProvider(this,
        factory(f)
    ).get(T::class.java)
}

inline fun <reified T : ViewModel> FragmentActivity.viewModel(crossinline f: () -> T): T {
    return ViewModelProvider(this,
        factory(f)
    ).get(T::class.java)
}

inline fun <reified T : ViewModel> Fragment.viewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProvider(this, factory).get(T::class.java)
}

inline fun <reified T : ViewModel> FragmentActivity.viewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProvider(this, factory).get(T::class.java)
}

@Suppress("UNCHECKED_CAST")
inline fun <VM : ViewModel> factory(crossinline f: () -> VM) = object : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return f() as T
    }
}