package com.vincentzhangz.maverickcount

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vincentzhangz.maverickcount.models.BorrowRequest
import com.vincentzhangz.maverickcount.models.Friend
import com.vincentzhangz.maverickcount.utilities.UserUtil
import kotlinx.android.synthetic.main.activity_borrow_request.*
import kotlinx.android.synthetic.main.activity_borrow_request.view.*
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.collections.ArrayList

class BorrowRequestActivity : Fragment() {
    private val database = FirebaseDatabase.getInstance()
    private var friendList=ArrayList<Friend>()
    private var postIdx=0
    private lateinit var datePicker:DatePickerDialog
    private lateinit var deadlineDate:LocalDateTime

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView=inflater.inflate(R.layout.activity_borrow_request,container,false)
        getFriends()
        deadlineDate=LocalDateTime.now().plusDays(7)
        rootView.date_picker.text=deadlineDate.toString()

        rootView.date_picker.setOnClickListener {
            setDatePicker(rootView)
        }

        rootView.btn_req_borrow.setOnClickListener{
            requestBorrow(rootView)
        }
        return rootView
    }

    private fun requestBorrow(view: View) {
        val amount:Long?=view.borrow_amount.text.toString().toLongOrNull()
        Log.d("amount",amount.toString())
        if(amount==null){
            Toast.makeText(activity,"Amount must be filled",Toast.LENGTH_SHORT).show()
        }
        else{
            val userId=UserUtil.getUserId(this.activity!!.applicationContext)
            var lender=""
            if(postIdx!=0){
                lender=friendList[postIdx-1].uid
            }
            database.getReference("borrow-request").push().setValue(
                BorrowRequest(userId,lender,amount,System.currentTimeMillis(),deadlineDate.toInstant(
                    ZoneOffset.ofTotalSeconds(0)).toEpochMilli())
            )
            Toast.makeText(activity,"Success to post borrow request",Toast.LENGTH_SHORT).show()
        }
    }


    private fun setDatePicker(viewRoot: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        datePicker= DatePickerDialog(this.activity!!,DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            deadlineDate= LocalDateTime.of(year,month+1,dayOfMonth,23,59)
            viewRoot.date_picker.text= deadlineDate.toString()
        },year,month,day)
        datePicker.show()
    }

    private fun getFriends() {
        val userId=UserUtil.getUserId(this.activity!!.applicationContext)
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
        val arrayAdapter=ArrayAdapter(this.activity!!.baseContext,R.layout.support_simple_spinner_dropdown_item,postOpt)
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
