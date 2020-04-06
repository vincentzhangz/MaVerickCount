package com.vincentzhangz.maverickcount

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vincentzhangz.maverickcount.models.BorrowRequest
import com.vincentzhangz.maverickcount.models.Friend
import com.vincentzhangz.maverickcount.utilities.UserUtil
import kotlinx.android.synthetic.main.activity_borrow_request.*
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.collections.ArrayList

class BorrowRequestActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private var friendList=ArrayList<Friend>()
    private var postIdx=0
    private lateinit var datePicker:DatePickerDialog
    private lateinit var deadlineDate:LocalDateTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_borrow_request)
        supportActionBar?.title="Borrow Request"

        getFriends()

        deadlineDate=LocalDateTime.now().plusDays(7)
        date_picker.text=deadlineDate.toString()

        date_picker.setOnClickListener {
            setDatePicker()
        }

        btn_req_borrow.setOnClickListener{
            requestBorrow()
        }

    }

    private fun requestBorrow() {
        val amount:Long?=borrow_amount.text.toString().toLongOrNull()
        Log.d("amount",amount.toString())
        if(amount==null){
            Toast.makeText(this,"Amount must be filled",Toast.LENGTH_SHORT).show()
        }
        else{
            val userId=UserUtil.getUserId(this)
            var lender=""
            if(postIdx!=0){
                lender=friendList[postIdx-1].uid
            }
            val db=database.getReference("borrow-request").push().setValue(
                BorrowRequest(userId,lender,amount,System.currentTimeMillis(),deadlineDate.toInstant(
                    ZoneOffset.ofTotalSeconds(0)).toEpochMilli())
            )
            Toast.makeText(this,"Success to post borrow request",Toast.LENGTH_SHORT).show()
        }
    }


    private fun setDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        datePicker= DatePickerDialog(this,DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            deadlineDate= LocalDateTime.of(year,month+1,dayOfMonth,23,59)
            date_picker.text= deadlineDate.toString()
        },year,month,day)
        datePicker.show()
    }

    private fun getFriends() {
        val userId=UserUtil.getUserId(this)
        val db=database.getReference("users").child(userId).child("friends")
        var postOpt=ArrayList<String>()
        postOpt.add("Global")
        db.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(ds: DataSnapshot) {
                ds.children.forEach{
                    val friend=it.getValue(Friend::class.java)
//                    Log.d("friend",friend!!.uid+" "+friend!!.name)
                    postOpt.add(friend!!.name)
                    friendList.add(friend)
                }
                addSpinner(postOpt)
            }
        })

    }

    fun addSpinner(postOpt:ArrayList<String>){
        val arrayAdapter=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,postOpt)
        post_option.setSelection(0)
        post_option.adapter=arrayAdapter
        post_option.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?,view: View?, position: Int,id: Long) {
//                Toast.makeText(applicationContext, postOpt[position], Toast.LENGTH_LONG).show()
                postIdx=position
            }

        }
    }

}
