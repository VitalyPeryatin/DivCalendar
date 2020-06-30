package com.infinity_coder.divcalendar.presentation.datasourceslist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.repositories.datasources.models.DataSourceModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_data_source.*

class DataSourcesRecyclerAdapter(
    private val onDataSourceClickListener: OnDataSourceClickListener? = null
) : RecyclerView.Adapter<DataSourcesRecyclerAdapter.DataSourceViewHolder>() {

    private var dataSources: List<DataSourceModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataSourceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_data_source, parent, false)
        return DataSourceViewHolder(onDataSourceClickListener, view)
    }

    override fun getItemCount(): Int {
        return dataSources.size
    }

    override fun onBindViewHolder(holder: DataSourceViewHolder, position: Int) {
        holder.bind(dataSources[position])
    }

    fun updateDataSources(dataSources: List<DataSourceModel>) {
        this.dataSources = dataSources
        notifyDataSetChanged()
    }

    class DataSourceViewHolder(
        private val onDataSourceClickListener: OnDataSourceClickListener?,
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(dataSource: DataSourceModel) {
            iconImageView.setImageResource(dataSource.iconResId)
            nameTextView.text = dataSource.name
            linkTextView.text = dataSource.link
            containerView.setOnClickListener {
                onDataSourceClickListener?.onDataSourceClicked(dataSource)
            }
        }
    }

    interface OnDataSourceClickListener {
        fun onDataSourceClicked(dataSource: DataSourceModel)
    }
}