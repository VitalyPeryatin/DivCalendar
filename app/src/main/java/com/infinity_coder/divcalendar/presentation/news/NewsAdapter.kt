package com.infinity_coder.divcalendar.presentation.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.PostDbModel
import com.infinity_coder.divcalendar.presentation._common.loadImg
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_post.*

class NewsAdapter(
    var onItemClickListener: NewsItemClickListener? = null
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private var newsPosts: List<PostDbModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_post, parent, false)
        return NewsViewHolder(view, onItemClickListener)
    }

    override fun getItemCount(): Int = newsPosts.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val isLastPosition = position == newsPosts.size - 1
        holder.bind(newsPosts[position], isLastPosition)
    }

    fun setPosts(posts: List<PostDbModel>) {
        this.newsPosts = posts
        notifyDataSetChanged()
    }

    class NewsViewHolder(
        override val containerView: View,
        private val onItemClickListener: NewsItemClickListener?
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(post: PostDbModel, isLastItem: Boolean = false) {
            titleTextView.text = post.title
            sourceTextView.text = post.source
            textTextView.text = post.text
            timeAgoTextView.text = post.date
            logoImageView.loadImg(post.logo)

            postItemLayout.setOnClickListener {
                onItemClickListener?.onClick(post)
            }

            if (isLastItem) {
                separatorView.visibility = View.GONE
            }
        }
    }

    interface NewsItemClickListener {
        fun onClick(post: PostDbModel)
    }
}