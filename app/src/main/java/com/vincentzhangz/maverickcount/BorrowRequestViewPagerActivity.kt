package com.vincentzhangz.maverickcount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vincentzhangz.maverickcount.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_borrow_request_view_pager.view.*

class BorrowRequestViewPagerActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =
            inflater.inflate(R.layout.activity_borrow_request_view_pager, container, false)
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.add(BorrowRequestActivity(), resources.getString(R.string.request_borrow))
        adapter.add(LenderRequestActivity(), resources.getString(R.string.global_request_handler))
        rootView.borrow_request_view_pager.adapter = adapter
        rootView.borrow_request_tab.setupWithViewPager(rootView.borrow_request_view_pager)
        return rootView
    }

}
