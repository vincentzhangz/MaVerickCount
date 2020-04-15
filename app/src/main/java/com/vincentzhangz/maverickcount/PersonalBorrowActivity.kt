package com.vincentzhangz.maverickcount

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.vincentzhangz.maverickcount.models.MessageHead
import com.vincentzhangz.maverickcount.utilities.OtherUtil
import com.vincentzhangz.maverickcount.utilities.SystemUtility
import com.vincentzhangz.maverickcount.utilities.UserUtil
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_personal_borrow.view.*

class PersonalBorrowActivity : Fragment() {
    private val database = FirebaseDatabase.getInstance()
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_personal_borrow, container, false)
        rootView.empty_error.visibility=View.VISIBLE
        rootView.recyclerview_personal_borrow.visibility=View.GONE
        userId = UserUtil.getUserId(this.activity!!.applicationContext)
        rootView.recyclerview_personal_borrow.layoutManager = LinearLayoutManager(activity)
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
                val adapter = GroupAdapter<ViewHolder>()
                var dataExists= false
//                Log.d("borrow",ds.toString())
                ds.children.forEach {
//                    Log.d("borrow",it.key.toString())
                    val borrow = it.getValue(BorrowRequest::class.java)
                    val borrowData = borrow?.let { it1 ->
                        BorrowRequestData(
                            it.key.toString(),
                            it1
                        )
                    } as BorrowRequestData
                    if (borrowData.borrowRequest.lender == userId) {
                        adapter.add(BorrowItem(borrowData))
                        dataExists=true
                    }
                }
                if(dataExists){
                    view.empty_error.visibility=View.GONE
                    view.recyclerview_personal_borrow.visibility=View.VISIBLE
                }
                adapter.setOnItemClickListener { item, view ->
                    val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                    val data = item as BorrowItem
                    dialogBuilder.setMessage("Are you sure give a loan ?")
                    dialogBuilder.setCancelable(false)
                    dialogBuilder.setPositiveButton("Accept",
                        DialogInterface.OnClickListener { dialog, which ->
//                        Toast.makeText(applicationContext,data.borrowData.borrowRequest.borrower,Toast.LENGTH_SHORT).show()
                            val borrowData = data.borrowData.borrowRequest

                            database.getReference("users").child(borrowData.lender).child("balance")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onDataChange(ds: DataSnapshot) {
                                        val currBalance = ds.getValue(Int::class.java) as Int
                                        if (currBalance >= borrowData.amount) {
                                            val newBalance: Long = currBalance - borrowData.amount
                                            database.getReference("users").child(borrowData.lender)
                                                .child("balance").setValue(newBalance)
                                            database.getReference("borrow")
                                                .child(data.borrowData.uid)
                                                .setValue(
                                                    BorrowRequest(
                                                        borrowData.borrower,
                                                        borrowData.lender,
                                                        borrowData.amount,
                                                        borrowData.requestDate,
                                                        borrowData.deadlineDate
                                                    )
                                                )
                                            database.getReference("users").child(borrowData.borrower).child("balance")
                                                .addListenerForSingleValueEvent(object:ValueEventListener{
                                                    override fun onCancelled(p0: DatabaseError) {
                                                        TODO("Not yet implemented")
                                                    }

                                                    override fun onDataChange(ds: DataSnapshot) {
                                                        val currBalance=ds.getValue(Int::class.java) as Int
                                                        var newBalance=currBalance+borrowData.amount
                                                        database.getReference("users").child(borrowData.borrower)
                                                            .child("balance").setValue(newBalance)
                                                    }

                                                })

                                            database.getReference("borrow-request")
                                                .child(data.borrowData.uid)
                                                .removeValue()
//                                            database.getReference("chats").push()
//                                                .setValue(MessageHead(borrowData.borrower, borrowData.lender))
                                            OtherUtil.createNewChat(MessageHead(borrowData.borrower, borrowData.lender))

                                            OtherUtil.updateUnpaid(borrowData.borrower)
                                        } else {
                                            SystemUtility.toast(context!!, "Not enough Balance")
                                        }
                                    }

                                })
                            reload()
                        })
                    dialogBuilder.setNeutralButton("Reject",
                        DialogInterface.OnClickListener { dialog, which ->
                            database.getReference("borrow-request").child(data.borrowData.uid)
                                .removeValue()
                            reload()
                        })
                    dialogBuilder.setNegativeButton("Cancel",
                        DialogInterface.OnClickListener { dialog, which ->
                            Toast.makeText(activity, "Cancel", Toast.LENGTH_SHORT).show()
                        })
                    val dialog = dialogBuilder.create()
                    dialog.setTitle("Loan Confirmation")
                    dialog.show()
                }
                view.recyclerview_personal_borrow.adapter = adapter
            }

        })
    }

    fun reload() {
        this.fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
    }

}
