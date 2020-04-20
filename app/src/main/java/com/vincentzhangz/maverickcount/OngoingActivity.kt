package com.vincentzhangz.maverickcount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vincentzhangz.maverickcount.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_ongoing.view.*

class OngoingActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_ongoing, container, false)
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.add(OngoingBorrow(), resources.getString(R.string.borrow))
        adapter.add(OngoingLend(), resources.getString(R.string.lend))
        rootView.ongoing_view_pager.adapter = adapter
        rootView.ongoing_tab.setupWithViewPager(rootView.ongoing_view_pager)
        return rootView
    }
}
