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
import kotlinx.android.synthetic.main.activity_history_lend.view.*

class HistoryLend : Fragment() {
    private val database = FirebaseDatabase.getInstance()
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_history_lend, container, false)
        rootView.empty_error.visibility = View.VISIBLE
        rootView.recyclerview_history_lend.visibility = View.GONE
        userId = UserUtil.getUserId(this.activity!!.applicationContext)
        rootView.recyclerview_history_lend.layoutManager = LinearLayoutManager(activity)
        fetchHistoryLend(rootView)
        return rootView
    }

    private fun fetchHistoryLend(view: View) {
        val db = database.getReference("complete-borrow")
        db.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(ds: DataSnapshot) {
                var dataExists = false
                val adapter = GroupAdapter<ViewHolder>()
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
                    view.recyclerview_history_lend.visibility = View.VISIBLE
                }
                view.recyclerview_history_lend.adapter = adapter
            }

        })
    }

    private fun reload() {
        this.fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
    }
}
