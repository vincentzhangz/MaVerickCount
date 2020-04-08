package com.vincentzhangz.maverickcount

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vincentzhangz.maverickcount.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_borrow_request_view_pager.view.*
import kotlinx.android.synthetic.main.activity_borrow_view_pager.view.*

class BorrowRequestViewPagerActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_borrow_request_view_pager, container, false)
        val adapter= ViewPagerAdapter(childFragmentManager)
        adapter.add(BorrowRequestActivity(),"Request Borrow")
        adapter.add(LenderRequestActivity(),"Global Request Handler")
        rootView.borrow_request_view_pager.adapter=adapter
        rootView.borrow_request_tab.setupWithViewPager(rootView.borrow_request_view_pager)
        return rootView
    }

}
