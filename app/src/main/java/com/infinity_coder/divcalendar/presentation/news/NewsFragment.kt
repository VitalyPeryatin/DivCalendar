package com.infinity_coder.divcalendar.presentation.news

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.PostDbModel
import com.infinity_coder.divcalendar.presentation._common.setActionBar
import com.infinity_coder.divcalendar.presentation._common.viewModel
import com.infinity_coder.divcalendar.presentation.newspost.NewsPostActivity
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : Fragment(R.layout.fragment_news) {

    private val adapter = NewsAdapter()

    private val viewModel: NewsViewModel by lazy {
        viewModel { NewsViewModel() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsToolbar.title = context!!.resources.getString(R.string.news)
        val parentActivity = (activity as AppCompatActivity)
        parentActivity.setActionBar(newsToolbar)

        newsRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter.onItemClickListener = object : NewsAdapter.NewsItemClickListener {
            override fun onClick(post: PostDbModel) {
                openNewsArticle(post)
            }
        }
        newsRecyclerView.adapter = adapter

        viewModel.getNewsPostsLiveData().observe(viewLifecycleOwner, Observer(this::updateNewsPosts))
        viewModel.loadNewsPosts()
    }

    private fun updateNewsPosts(posts: List<PostDbModel>) {
        adapter.setPosts(posts)
    }

    fun openNewsArticle(post: PostDbModel) {
        val intent = NewsPostActivity.getIntent(context!!, post.id)
        context?.startActivity(intent)
    }
}