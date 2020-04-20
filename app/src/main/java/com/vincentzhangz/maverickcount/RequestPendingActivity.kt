package com.vincentzhangz.maverickcount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vincentzhangz.maverickcount.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_borrow_request_view_pager.view.*

class RequestPendingActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =
            inflater.inflate(R.layout.activity_borrow_request_view_pager, container, false)
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.add(BorrowPendingActivity(), resources.getString(R.string.borrow_pending))
        adapter.add(LendPendingActivity(), resources.getString(R.string.lend_pending))
        rootView.borrow_request_view_pager.adapter = adapter
        rootView.borrow_request_tab.setupWithViewPager(rootView.borrow_request_view_pager)
        return rootView
    }

}
