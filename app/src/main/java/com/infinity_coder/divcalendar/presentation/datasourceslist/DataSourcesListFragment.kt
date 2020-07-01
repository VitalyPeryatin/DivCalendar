package com.infinity_coder.divcalendar.presentation.datasourceslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.domain.models.DataSourceModel
import com.infinity_coder.divcalendar.presentation._common.setActionBar
import com.infinity_coder.divcalendar.presentation.browser.BrowserActivity
import com.infinity_coder.divcalendar.presentation.datasourceslist.adapters.DataSourcesRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_data_sources_list.*

class DataSourcesListFragment : Fragment(R.layout.fragment_data_sources_list),
    DataSourcesRecyclerAdapter.OnDataSourceClickListener {

    private val viewModel: DataSourcesViewModel by lazy {
        ViewModelProvider(this).get(DataSourcesViewModel::class.java)
    }

    private val dataSourcesAdapter: DataSourcesRecyclerAdapter
        get() = dataSourcesRecyclerView.adapter as DataSourcesRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        viewModel.dataSourceList.observe(viewLifecycleOwner, Observer(this::updateDataSourceList))
    }

    private fun initUI() {
        setActionBar(dataSourcesToolbar, hasBackNavigation = true)

        dataSourcesRecyclerView.layoutManager = LinearLayoutManager(context)
        dataSourcesRecyclerView.adapter = DataSourcesRecyclerAdapter(onDataSourceClickListener = this)
    }

    private fun updateDataSourceList(dataSourceList: List<DataSourceModel>) {
        dataSourcesAdapter.updateItems(dataSourceList)
    }

    override fun onDataSourceClicked(dataSource: DataSourceModel) {
        BrowserActivity.openActivity(requireContext(), dataSource.name, dataSource.link)
    }

    companion object {
        fun newInstance(): DataSourcesListFragment {
            return DataSourcesListFragment()
        }
    }
}