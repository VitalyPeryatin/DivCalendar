package com.infinity_coder.divcalendar.presentation.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.ShortSecNetworkModel
import com.infinity_coder.divcalendar.presentation.search.addsec.AddSecurityBottomDialog
import kotlinx.android.synthetic.main.activity_search_secs.*

class SearchSecActivity : AppCompatActivity(), AddSecurityBottomDialog.OnClickListener {

    val viewModel: SearchSecViewModel by lazy { SearchSecViewModel() }

    private var addSecurityDialog: AddSecurityBottomDialog? = null

    private val secClickListener = object : SecRecyclerAdapter.OnClickListener {
        override fun onClick(security: ShortSecNetworkModel) {
            addSecurityDialog = AddSecurityBottomDialog.newInstance(security)
            addSecurityDialog?.show(
                supportFragmentManager.beginTransaction(),
                AddSecurityBottomDialog::class.toString()
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_secs)

        initUI()

        viewModel.requestSecuritiesByQuery("")
        viewModel.filteredSecuritiesLiveData.observe(this, Observer(this::setSecurities))
    }

    private fun initUI() {
        securitiesRecyclerView.layoutManager = LinearLayoutManager(this)
        securitiesRecyclerView.adapter = SecRecyclerAdapter(secClickListener)

        backButton.setOnClickListener {
            onBackPressed()
        }

        clearButton.setOnClickListener {
            queryEditText.text.clear()
        }
        queryEditText.addTextChangedListener(getQueryTextWatcher())
    }

    private fun getQueryTextWatcher() = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s == null) return

            if (s.isNotEmpty()) {
                clearButton.visibility = View.VISIBLE
            } else {
                clearButton.visibility = View.GONE
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.requestSecuritiesByQuery(s.toString())
        }

    }

    private fun setSecurities(securities: List<ShortSecNetworkModel>) {
        val adapter = securitiesRecyclerView.adapter as? SecRecyclerAdapter
        adapter?.setSecurities(securities)
    }

    override fun onAddSecPackageClick(securityPackage: SecurityPackageDbModel) {
        viewModel.appendSecurityPackage(securityPackage)
        dismissAddSecurityDialog()
    }

    private fun dismissAddSecurityDialog() {
        addSecurityDialog?.dismiss()
        addSecurityDialog = null
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SearchSecActivity::class.java)
        }
    }
}