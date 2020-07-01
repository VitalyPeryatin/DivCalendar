package com.infinity_coder.divcalendar.presentation.datasourceslist.adapters

import android.view.View
import android.view.ViewGroup
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.domain.models.DataSourceModel
import com.infinity_coder.divcalendar.presentation._common.base.BaseAdapter
import com.infinity_coder.divcalendar.presentation._common.extensions.inflate
import kotlinx.android.synthetic.main.item_data_source.*

class DataSourcesRecyclerAdapter(
    private val onDataSourceClickListener: OnDataSourceClickListener? = null
) : BaseAdapter<DataSourceModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DataSourceModel> {
        val view = parent.inflate(R.layout.item_data_source)
        val holder = DataSourceViewHolder(view)
        holder.setOnClickListener { position ->
            onDataSourceClickListener?.onDataSourceClicked(items[position])
        }
        return holder
    }

    class DataSourceViewHolder(
        override val containerView: View
    ) : BaseViewHolder<DataSourceModel>(containerView) {

        override fun bind(model: DataSourceModel, position: Int) {
            iconImageView.setImageResource(model.iconResId)
            nameTextView.text = model.name
            linkTextView.text = model.link
        }
    }

    interface OnDataSourceClickListener {
        fun onDataSourceClicked(dataSource: DataSourceModel)
    }
}