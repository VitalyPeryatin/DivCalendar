package com.infinity_coder.divcalendar.presentation.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.network.model.SecurityNetModel
import com.infinity_coder.divcalendar.presentation._common.delegate.SecurityMarketDelegate
import com.infinity_coder.divcalendar.presentation._common.delegate.SecurityTypeDelegate
import com.infinity_coder.divcalendar.presentation._common.extensions.hideKeyboard
import com.infinity_coder.divcalendar.presentation._common.extensions.showKeyboard
import com.infinity_coder.divcalendar.presentation._common.extensions.visibility
import com.infinity_coder.divcalendar.presentation._common.text_watchers.OpenTextWatcher
import com.infinity_coder.divcalendar.presentation.search.adapters.SearchSecurityPagerAdapter
import kotlinx.android.synthetic.main.fragment_search_securities.*

class SearchSecurityFragment : Fragment(R.layout.fragment_search_securities) {

    val viewModel: SearchSecurityViewModel by lazy {
        ViewModelProvider(this).get(SearchSecurityViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        requireContext().showKeyboard()
    }

    private fun initUI() {
        viewPager.adapter = SearchSecurityPagerAdapter(SecurityNetModel.SECURITY_TYPES, this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = SecurityTypeDelegate.getTitle(requireContext(), position)
        }.attach()

        initSegmentedButton()

        initQueryEditText()

        backButton.setOnClickListener {
            requireContext().hideKeyboard(it)
            it.postDelayed({ requireActivity().onBackPressed() }, ESTIMATED_CLOSING_TIME_KEYBOARD)
        }

        clearButton.setOnClickListener {
            queryEditText.text.clear()
        }
    }

    private fun initSegmentedButton() {
        segmentedButton {
            initialCheckedIndex = SecurityMarketDelegate.getMarketIndex(viewModel.getCurrentMarket())

            initWithItems { SecurityMarketDelegate.getTitles(context) }

            onSegmentChecked {
                val market = SecurityMarketDelegate.getMarketByTitle(context, it.text.toString())
                viewModel.setMarket(market)
            }
        }
    }

    private fun initQueryEditText() {
        queryEditText.addTextChangedListener(object : OpenTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                clearButton.visibility(!query.isBlank())
                viewModel.executeQuery(query)
            }
        })

        queryEditText.setOnEditorActionListener { _, _, _ ->
            queryEditText.clearFocus()
            val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(queryEditText.windowToken, 0)
            return@setOnEditorActionListener true
        }
    }

    companion object {
        private const val ESTIMATED_CLOSING_TIME_KEYBOARD = 150L

        fun newInstance(): SearchSecurityFragment {
            return SearchSecurityFragment()
        }
    }
}