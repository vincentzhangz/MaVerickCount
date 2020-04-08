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
import com.vincentzhangz.maverickcount.models.Status
import com.vincentzhangz.maverickcount.utilities.UserUtil
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_ongoing_borrow.view.*

class OngoingBorrow : Fragment() {
    private val database = FirebaseDatabase.getInstance()
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_ongoing_borrow, container, false)
        userId = UserUtil.getUserId(this.activity!!.applicationContext)
        rootView.recyclerview_ongoing_borrow.layoutManager = LinearLayoutManager(activity)
        fetchOngoingBorrow(rootView)
        return rootView
    }

    private fun fetchOngoingBorrow(view: View) {
        val db = database.getReference("borrow")
        db.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(ds: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                ds.children.forEach {
                    val borrow = it.getValue(BorrowRequest::class.java)
                    val borrowData = borrow?.let { it1 ->
                        BorrowRequestData(
                            it.key.toString(),
                            it1
                        )
                    } as BorrowRequestData
                    if (borrowData.borrowRequest.borrower==userId) {
                        adapter.add(BorrowItem(borrowData))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                    val data = item as BorrowItem
                    dialogBuilder.setMessage("Are you sure to pay the debt ?")
                    dialogBuilder.setCancelable(false)
                    dialogBuilder.setPositiveButton(
                        "Pay"
                    ) { dialog, which ->
                        database.getReference("users").child(userId).child("balance")
                            .addListenerForSingleValueEvent(object :ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                                override fun onDataChange(ds: DataSnapshot) {
                                    val userBalance=ds.getValue(Int::class.java) as Int
                                    val borrowData = data.borrowData.borrowRequest
                                    if(userBalance>=borrowData.amount){
                                        database.getReference("complete-borrow").child(data.borrowData.uid)
                                            .setValue(
                                                BorrowRequest(
                                                    borrowData.borrower,
                                                    borrowData.lender,
                                                    borrowData.amount,
                                                    borrowData.requestDate,
                                                    borrowData.deadlineDate
                                                )
                                            )
                                        database.getReference("borrow").child(data.borrowData.uid)
                                            .removeValue()
                                        val currBalance:Long=userBalance-borrowData.amount
                                        database.getReference("users").child(userId).child("balance").setValue(currBalance)

                                        database.getReference("users").child(userId).child("status")
                                            .addListenerForSingleValueEvent(object :ValueEventListener{
                                                override fun onCancelled(p0: DatabaseError) {
                                                    TODO("Not yet implemented")
                                                }

                                                override fun onDataChange(ds: DataSnapshot) {
                                                    val data=ds.getValue(Status::class.java) as Status
                                                    val newStatus:Int=data.paid+1
                                                    database.getReference("users").child(userId).child("status")
                                                        .child("paid").setValue(newStatus)
                                                }

                                            })
                                        database.getReference("users").child(borrowData.lender).child("balance")
                                            .addListenerForSingleValueEvent(object :ValueEventListener{
                                                override fun onCancelled(p0: DatabaseError) {
                                                    TODO("Not yet implemented")
                                                }

                                                override fun onDataChange(ds: DataSnapshot) {
                                                    val balance=ds.getValue(Int::class.java) as Int
                                                    val newBalance: Long =balance+borrowData.amount
                                                    database.getReference("users").child(borrowData.lender)
                                                        .child("balance").setValue(newBalance)
                                                }

                                            })

                                        Toast.makeText(activity, "Success delete request", Toast.LENGTH_SHORT).show()
                                        reload()
                                    }
                                    else{
                                        Toast.makeText(context!!.applicationContext,"Insufficient Balance, Please TopUp your balance",Toast.LENGTH_SHORT).show()
                                    }

                                }

                            })

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
                view.recyclerview_ongoing_borrow.adapter = adapter
            }

        })
    }

    private fun reload(){
        this.fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
    }

}
