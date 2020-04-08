package com.vincentzhangz.maverickcount

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.vincentzhangz.maverickcount.adapter.ViewPagerAdapter
import com.vincentzhangz.maverickcount.utilities.UserUtil
import kotlinx.android.synthetic.main.activity_borrow_request_view_pager.view.*
import kotlinx.android.synthetic.main.activity_global_borrow.view.*
import kotlinx.android.synthetic.main.activity_ongoing.view.*

class OngoingActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_ongoing, container, false)
        val adapter= ViewPagerAdapter(childFragmentManager)
        adapter.add(OngoingBorrow(),"Borrow")
        adapter.add(OngoingLend(),"Lend")
        rootView.ongoing_view_pager.adapter=adapter
        rootView.ongoing_tab.setupWithViewPager(rootView.ongoing_view_pager)
        return rootView
    }
}
