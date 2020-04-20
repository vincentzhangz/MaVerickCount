package com.vincentzhangz.maverickcount

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
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.add(FriendFragment(), resources.getString(R.string.friend_list))
        adapter.add(FriendRequestActivity(), resources.getString(R.string.friend_search))
        adapter.add(FriendRequestHandlerActivity(), resources.getString(R.string.friend_request))
        rootView.friend_view_pager.adapter = adapter
        rootView.friend_tab.setupWithViewPager(rootView.friend_view_pager)
        return rootView
    }
}
