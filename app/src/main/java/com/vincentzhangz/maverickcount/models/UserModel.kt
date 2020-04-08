package com.vincentzhangz.maverickcount.models

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vincentzhangz.maverickcount.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.friend_item.view.*

class User(val uid: String, val name: String,val balance:Int, val status: Status, val friends: ArrayList<Friend>) {
    constructor() : this("", "",0,Status(), ArrayList())
}

class Status(val paid:Int, val unpaid:Int, val late:Int){
    constructor():this(0,0,0)
}

class UserData(val uid: String, val name: String,val balance:Int,  val status: Status, val friends: HashMap<String, Friend>) {
    constructor() : this("", "",0, Status(),HashMap())
}

class Friend(val uid: String, val name: String) {
    constructor() : this("", "")
}

class FriendRequest(val from:String, val to:String, val timestamp:Long){
    constructor():this("","",System.currentTimeMillis())
}

class FriendSearch(val userData: UserData):Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.friend_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.friend_name.text=userData.name
    }

}

class FriendRequestHandler(val uid:String, val data: FriendRequest):Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.friend_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
       FirebaseDatabase.getInstance().getReference("users").child(data.from).child("name")
           .addValueEventListener(object :ValueEventListener{
               override fun onCancelled(p0: DatabaseError) {
                   TODO("Not yet implemented")
               }

               override fun onDataChange(ds: DataSnapshot) {
                   viewHolder.itemView.friend_name.text=ds.getValue(String()::class.java)
               }

           })
    }

}