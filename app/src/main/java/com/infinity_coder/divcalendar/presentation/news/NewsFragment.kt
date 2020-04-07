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
import com.infinity_coder.divcalendar.presentation.browser.BrowserActivity
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.layout_stub_empty.view.*

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

        emptySecuritiesLayout.emptyTextView.text = resources.getText(R.string.empty_securities)

        newsRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter.onItemClickListener = getItemClickListener()
        newsRecyclerView.adapter = adapter

        viewModel.newsPost.observe(viewLifecycleOwner, Observer(this::updateNewsPosts))
        viewModel.state.observe(viewLifecycleOwner, Observer(this::updateState))
        viewModel.loadNewsPosts()
    }

    private fun getItemClickListener() = object : NewsAdapter.NewsItemClickListener {
        override fun onClick(post: PostDbModel) {
            openNewsArticle(post)
        }
    }

    private fun updateNewsPosts(posts: List<PostDbModel>) {
        adapter.setPosts(posts)
    }

    fun openNewsArticle(post: PostDbModel) {
        BrowserActivity.openActivity(context!!, post.link)
    }

    private fun updateState(state: Int) {
        contentLayout.visibility = View.GONE
        loadingLayout.visibility = View.GONE
        emptyLayout.visibility = View.GONE
        noNetworkLayout.visibility = View.GONE
        emptySecuritiesLayout.visibility = View.GONE

        when (state) {
            NewsViewModel.VIEW_STATE_NEWS_CONTENT -> contentLayout.visibility = View.VISIBLE
            NewsViewModel.VIEW_STATE_NEWS_LOADING -> loadingLayout.visibility = View.VISIBLE
            NewsViewModel.VIEW_STATE_NEWS_EMPTY -> emptyLayout.visibility = View.VISIBLE
            NewsViewModel.VIEW_STATE_NEWS_NO_NETWORK -> noNetworkLayout.visibility = View.VISIBLE
            NewsViewModel.VIEW_STATE_NEWS_EMPTY_SECURITIES -> emptySecuritiesLayout.visibility =
                View.VISIBLE
        }
    }
}