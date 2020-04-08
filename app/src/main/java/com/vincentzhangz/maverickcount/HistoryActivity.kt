package com.vincentzhangz.maverickcount

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vincentzhangz.maverickcount.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_history.view.*
import kotlinx.android.synthetic.main.activity_ongoing.view.*

class HistoryActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_history, container, false)
        val adapter= ViewPagerAdapter(childFragmentManager)
        adapter.add(HistoryBorrow(),"Borrow")
        adapter.add(HistoryLend(),"Lend")
        rootView.history_view_pager.adapter=adapter
        rootView.history_tab.setupWithViewPager(rootView.history_view_pager)
        return rootView
    }

}
