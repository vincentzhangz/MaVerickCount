package com.vincentzhangz.maverickcount

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_global_borrow.view.*

class GlobalBorrowActivity : Fragment() {
    private val database = FirebaseDatabase.getInstance()
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_global_borrow, container, false)
        userId = UserUtil.getUserId(this.activity!!.applicationContext)
        rootView.recyclerview_global_borrow.layoutManager = LinearLayoutManager(activity)
        fetchBorrowData(rootView)
        return rootView
    }

    private fun fetchBorrowData(view: View) {
        val db = database.getReference("borrow-request")
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
                    if (borrowData.borrowRequest.lender == "" && borrowData.borrowRequest.borrower != userId) {
                        adapter.add(BorrowItem(borrowData))
                        dataExist = true
                    }
                }
                if (!dataExist) {
                    return
                }
                adapter.setOnItemClickListener { item, view ->
                    val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                    val data = item as BorrowItem
                    dialogBuilder.setMessage("Are you sure give a loan ?")
                    dialogBuilder.setCancelable(false)
                    dialogBuilder.setPositiveButton(
                        "Accept"
                    ) { dialog, which ->
//                        Toast.makeText(applicationContext,data.borrowData.borrowRequest.borrower,Toast.LENGTH_SHORT).show()
                        val borrowData = data.borrowData.borrowRequest
                        database.getReference("lend-request").child(data.borrowData.uid)
                            .setValue(
                                BorrowRequest(
                                    borrowData.borrower,
                                    UserUtil.getUserId(view.context),
                                    borrowData.amount,
                                    borrowData.requestDate,
                                    borrowData.deadlineDate
                                )
                            )
                        database.getReference("borrow-request").child(data.borrowData.uid)
                            .removeValue()
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
                view.recyclerview_global_borrow.adapter = adapter
            }

        })
    }
}
