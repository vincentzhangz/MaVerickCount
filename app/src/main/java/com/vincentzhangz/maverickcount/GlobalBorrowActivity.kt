package com.vincentzhangz.maverickcount

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import kotlinx.android.synthetic.main.activity_global_borrow.*
import java.security.AccessController.getContext

class GlobalBorrowActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_borrow)

        recyclerview_global_borrow.layoutManager = LinearLayoutManager(this)
        fetchBorrowData()
    }

    private fun fetchBorrowData() {
        val db=database.getReference("borrow-request")
        db.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(ds: DataSnapshot) {
                val adapter=GroupAdapter<ViewHolder>()
//                Log.d("borrow",ds.toString())
                ds.children.forEach {
//                    Log.d("borrow",it.key.toString())
                    val borrow=it.getValue(BorrowRequest::class.java)
                    val borrowData= borrow?.let { it1 -> BorrowRequestData(it.key.toString(), it1) } as BorrowRequestData
                    if(borrowData.borrowRequest.lender==""){
                        adapter.add(BorrowItem(borrowData))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                    val data=item as BorrowItem
                    dialogBuilder.setMessage("Are you sure give a loan ?")
                    dialogBuilder.setCancelable(false)
                    dialogBuilder.setPositiveButton("Accept",DialogInterface.OnClickListener { dialog, which ->
//                        Toast.makeText(applicationContext,data.borrowData.borrowRequest.borrower,Toast.LENGTH_SHORT).show()
                        val borrowData=data.borrowData.borrowRequest
                        database.getReference("borrow").child(data.borrowData.uid)
                            .setValue(BorrowRequest(borrowData.borrower,UserUtil.getUserId(view.context),borrowData.amount,borrowData.requestDate,borrowData.deadlineDate))
                        database.getReference("borrow-request").child(data.borrowData.uid).removeValue()
                    })
                    dialogBuilder.setNegativeButton("Cancel",DialogInterface.OnClickListener { dialog, which ->
                        Toast.makeText(applicationContext,"Cancel",Toast.LENGTH_SHORT).show()
                    })
                    val dialog=dialogBuilder.create()
                    dialog.setTitle("Loan Confirmation")
                    dialog.show()
                }
                recyclerview_global_borrow.adapter=adapter
            }

        })
    }
}
