package com.vincentzhangz.maverickcount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vincentzhangz.maverickcount.models.FriendItem
import com.vincentzhangz.maverickcount.models.FriendModel
import com.vincentzhangz.maverickcount.utilities.UserUtil
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_friend.view.*

class FriendFragment : Fragment() {
    private val database = FirebaseDatabase.getInstance()
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_friend, container, false)
        getUserId()
        rootView.friend_list.layoutManager = LinearLayoutManager(activity)
        fetchFriend(rootView)
        return rootView
    }

    private fun getUserId() {
        userId = UserUtil.getUserId(activity!!.applicationContext)
    }

    private fun fetchFriend(v: View) {
        val db = database.getReference("users").child(userId).child("friends")

        db.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(ds: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                ds.children.forEach {
                    val friend = it.getValue(FriendModel::class.java)
                    friend as FriendModel
//                    Log.d("friend", data.name)
                    adapter.add(FriendItem(friend))
                }

                adapter.setOnItemClickListener { item, _ ->
                    item as FriendItem
                    val bundle = Bundle()
                    bundle.putString("name", item.friend.name)
                    val friendProfile = FriendProfile()
                    friendProfile.arguments = bundle

                    activity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, friendProfile)
                        .addToBackStack(null).commit()
                }

                v.friend_list.adapter = adapter
            }

        })
    }
}
