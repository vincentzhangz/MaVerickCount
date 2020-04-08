package com.vincentzhangz.maverickcount

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vincentzhangz.maverickcount.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_friend.view.*

class FriendActivity : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_friend, container, false)
        val adapter= ViewPagerAdapter(childFragmentManager)
        adapter.add(FriendFragment(),"Friend List")
        adapter.add(FriendRequestActivity(),"Search Friend")
        adapter.add(FriendRequestHandlerActivity(),"Friend Request")
        rootView.friend_view_pager.adapter=adapter
        rootView.friend_tab.setupWithViewPager(rootView.friend_view_pager)
        return rootView
    }
}
