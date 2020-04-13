package com.vincentzhangz.maverickcount

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import kotlinx.android.synthetic.main.activity_lend_pending.view.*

class LendPendingActivity : Fragment() {
    private val database = FirebaseDatabase.getInstance()
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_lend_pending, container, false)
        rootView.recyclerview_pending_lend.visibility=View.VISIBLE
        rootView.empty_error.visibility=View.GONE
        userId = UserUtil.getUserId(this.activity!!.applicationContext)
        rootView.recyclerview_pending_lend.layoutManager = LinearLayoutManager(activity)
        fetchLendPending(rootView)
        return rootView
    }

    private fun fetchLendPending(view: View) {
        val db = database.getReference("lend-request")
        db.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(ds: DataSnapshot) {
                var dataExist = false
                val adapter = GroupAdapter<ViewHolder>()
                ds.children.forEach {
                    val borrow = it.getValue(BorrowRequest::class.java)
                    val borrowData = borrow?.let { it1 ->
                        BorrowRequestData(
                            it.key.toString(),
                            it1
                        )
                    } as BorrowRequestData
                    if (borrowData.borrowRequest.lender==userId) {
                        adapter.add(BorrowItem(borrowData))
                        dataExist = true
                    }
                }
                if (!dataExist) {
                    view.recyclerview_pending_lend.visibility=View.GONE
                    view.empty_error.visibility=View.VISIBLE
                    return
                }
                adapter.setOnItemClickListener { item, view ->
                    val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                    val data = item as BorrowItem
                    dialogBuilder.setMessage("Are you sure delete request ?")
                    dialogBuilder.setCancelable(false)
                    dialogBuilder.setPositiveButton(
                        "Delete"
                    ) { dialog, which ->
                        data.borrowData.borrowRequest.lender=""
                        val borrowData = data.borrowData.borrowRequest
                        database.getReference("borrow-request").child(data.borrowData.uid)
                            .setValue(
                                BorrowRequest(
                                    borrowData.borrower,
                                    borrowData.lender,
                                    borrowData.amount,
                                    borrowData.requestDate,
                                    borrowData.deadlineDate
                                )
                            )
                        database.getReference("lend-request").child(data.borrowData.uid)
                            .removeValue()
                        Toast.makeText(activity, "Success delete request", Toast.LENGTH_SHORT).show()
                        reload()
                    }
                    dialogBuilder.setNegativeButton(
                        "Cancel"
                    ) { dialog, which ->
                        Toast.makeText(activity, "Cancel", Toast.LENGTH_SHORT).show()
                    }
                    val dialog = dialogBuilder.create()
                    dialog.setTitle("Loan Confirmation")
                    dialog.show()
                }
                view.recyclerview_pending_lend.adapter = adapter
            }

        })
    }

    private fun reload(){
        this.fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
    }
}
