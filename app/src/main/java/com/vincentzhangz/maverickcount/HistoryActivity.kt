package com.vincentzhangz.maverickcount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vincentzhangz.maverickcount.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_history.view.*

class HistoryActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_history, container, false)
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.add(HistoryBorrow(), resources.getString(R.string.borrow))
        adapter.add(HistoryLend(), resources.getString(R.string.lend))
        rootView.history_view_pager.adapter = adapter
        rootView.history_tab.setupWithViewPager(rootView.history_view_pager)
        return rootView
    }

}
