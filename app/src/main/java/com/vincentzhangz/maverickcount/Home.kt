package com.vincentzhangz.maverickcount

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vincentzhangz.maverickcount.models.UserData
import com.vincentzhangz.maverickcount.utilities.UserUtil
import kotlinx.android.synthetic.main.fragment_home.view.*


class Home : Fragment() {
    private val database = FirebaseDatabase.getInstance()
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        userId = UserUtil.getUserId(this.activity!!.applicationContext)
        getAdditionalData(rootView)
        return rootView
    }

    private fun getAdditionalData(view: View) {
        database.getReference("users").child(userId)
            .addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(ds: DataSnapshot) {
                    val data=ds.getValue(UserData::class.java) as UserData
                    view.balance.text="Balance: "+data.balance.toString()
                }

            })
    }

}
