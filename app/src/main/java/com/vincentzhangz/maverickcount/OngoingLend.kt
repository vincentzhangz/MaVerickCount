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
import com.vincentzhangz.maverickcount.models.BorrowItem
import com.vincentzhangz.maverickcount.models.BorrowRequest
import com.vincentzhangz.maverickcount.models.BorrowRequestData
import com.vincentzhangz.maverickcount.utilities.UserUtil
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_ongoing_borrow.view.*

class OngoingLend : Fragment() {
    private val database = FirebaseDatabase.getInstance()
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_ongoing_borrow, container, false)
        rootView.empty_error.visibility = View.VISIBLE
        rootView.recyclerview_ongoing_borrow.visibility = View.GONE
        userId = UserUtil.getUserId(this.activity!!.applicationContext)
        rootView.recyclerview_ongoing_borrow.layoutManager = LinearLayoutManager(activity)
        fetchOngoingLend(rootView)
        return rootView
    }

    private fun fetchOngoingLend(view: View) {
        val db = database.getReference("borrow")
        db.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(ds: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                var dataExists = false
                ds.children.forEach {
                    val borrow = it.getValue(BorrowRequest::class.java)
                    val borrowData = borrow?.let { it1 ->
                        BorrowRequestData(
                            it.key.toString(),
                            it1
                        )
                    } as BorrowRequestData
                    if (borrowData.borrowRequest.lender == userId) {
                        adapter.add(BorrowItem(borrowData))
                        dataExists = true
                    }
                }
                if (dataExists) {
                    view.empty_error.visibility = View.GONE
                    view.recyclerview_ongoing_borrow.visibility = View.VISIBLE
                }
                view.recyclerview_ongoing_borrow.adapter = adapter
            }

        })
    }

    private fun reload() {
        this.fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
    }

}
