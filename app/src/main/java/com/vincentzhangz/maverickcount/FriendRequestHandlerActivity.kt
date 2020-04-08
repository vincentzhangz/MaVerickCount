package com.vincentzhangz.maverickcount

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
import com.vincentzhangz.maverickcount.models.Friend
import com.vincentzhangz.maverickcount.models.FriendRequest
import com.vincentzhangz.maverickcount.models.FriendRequestHandler
import com.vincentzhangz.maverickcount.models.FriendSearch
import com.vincentzhangz.maverickcount.utilities.UserUtil
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_friend_request_handler.view.*
import kotlinx.android.synthetic.main.friend_item.view.*

class FriendRequestHandlerActivity : Fragment() {
    private val database = FirebaseDatabase.getInstance()
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_friend_request_handler, container, false)
        userId = UserUtil.getUserId(this.activity!!.applicationContext)
        rootView.recyclerview_friend_request_handler.layoutManager=LinearLayoutManager(activity)

        fetchFriendRequest(rootView)

        return rootView
    }

    private fun fetchFriendRequest(view: View) {
        val db= database.getReference("friend-request")
        db.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(ds: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                ds.children.forEach {
                    var data=it.getValue(FriendRequest::class.java) as FriendRequest
                    if(data.to==userId){
                        adapter.add(FriendRequestHandler(it.key.toString(),data))
                    }
                }

                adapter.setOnItemClickListener { item, view ->
                    val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                    val data=item as FriendRequestHandler
                    val dbFriend=database.getReference("users").child(data.data.from)
                    val dbUser=database.getReference("users").child(data.data.to)
                    dialogBuilder.setMessage("Accept friend request ?")
                    dialogBuilder.setCancelable(false)

                    dialogBuilder.setPositiveButton(
                        "Accept"
                    ) { dialog, which ->
                        dbFriend.child("name")
                            .addValueEventListener(object :ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                                override fun onDataChange(ds: DataSnapshot) {
                                    dbUser.child("friends").child(data.data.from).setValue(Friend(data.data.from,ds.getValue(
                                        String()::class.java).toString()))
                                }

                            })
                        dbUser.child("name")
                            .addValueEventListener(object :ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                                override fun onDataChange(ds: DataSnapshot) {
                                    dbFriend.child("friends").child(data.data.to).setValue(Friend(data.data.to,ds.getValue(
                                        String()::class.java).toString()))
                                }

                            })

                        database.getReference("friend-request").child(data.uid).removeValue()
                    }
                    dialogBuilder.setNeutralButton(
                        "Decline"
                    ) { dialog, which ->
                        database.getReference("friend-request").child(data.uid).removeValue()
                    }
                    dialogBuilder.setNegativeButton(
                        "Cancel"
                    ) { dialog, which ->

                    }
                    val dialog = dialogBuilder.create()
                    dialog.setTitle("Friend Request")
                    dialog.show()


                }

                view.recyclerview_friend_request_handler.adapter=adapter
            }

        })
    }

}
