package com.vincentzhangz.maverickcount

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vincentzhangz.maverickcount.models.*
import com.vincentzhangz.maverickcount.utilities.UserUtil
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_friend_request.view.*

class FriendRequestActivity : Fragment() {

    private val database = FirebaseDatabase.getInstance()
    private lateinit var userId: String
    private lateinit var friendList:ArrayList<Friend>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_friend_request, container, false)
        userId = UserUtil.getUserId(this.activity!!.applicationContext)
        rootView.recyclerview_search_friend.layoutManager=LinearLayoutManager(activity)
        friendList=ArrayList()
        fetchFriend(rootView)

        return rootView
    }

    private fun fetchFriend(view: View) {
        val db=database.getReference("users").child(userId).child("friends")
        db.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(ds: DataSnapshot) {
                ds.children.forEach {
                    val data=it.getValue(Friend::class.java) as Friend
                    friendList.add(data)
                }
                addButtonEvent(view)
            }

        })
    }

    private fun checkFriend(uid: String):Boolean{
        for (x in friendList){
            if(x.uid==uid){
                return false
            }
        }
        return true
    }

    private fun addButtonEvent(view: View) {
        val db = database.getReference("users")
        view.btn_search_friend.setOnClickListener {
            val friendName=view.et_name.text.toString()
            db.addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(ds: DataSnapshot) {
                    val adapter = GroupAdapter<ViewHolder>()
                    ds.children.forEach {
//                        Log.d("userData",it.toString())
                        val data=it.getValue(UserData::class.java) as UserData
                        if(data.uid!=userId&&data.name==friendName&&checkFriend(data.uid)){
                            adapter.add(FriendSearch(data))
                        }
                    }
                    view.recyclerview_search_friend.adapter=adapter

                    adapter.setOnItemClickListener { item, view ->
                        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                        val data=item as FriendSearch
                        dialogBuilder.setMessage("Send friend request to "+data.userData.name+" ?")
                        dialogBuilder.setCancelable(false)
                        dialogBuilder.setPositiveButton(
                            "Request"
                        ) { dialog, which ->
                            database.getReference("friend-request").push().setValue(FriendRequest(userId,data.userData.uid,System.currentTimeMillis()))
                        }
                        dialogBuilder.setNegativeButton(
                            "Cancel"
                        ) { dialog, which ->

                        }
                        val dialog = dialogBuilder.create()
                        dialog.setTitle("Friend Request")
                        dialog.show()
                    }
                }

            })
        }
    }
}
