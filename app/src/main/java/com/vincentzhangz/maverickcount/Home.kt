package com.vincentzhangz.maverickcount

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vincentzhangz.maverickcount.models.UserData
import com.vincentzhangz.maverickcount.utilities.SystemUtility.Companion.toast
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

        var topup: Boolean = false

        rootView.top_up_button.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(rootView.context)
            builder.setTitle("Top Up")
            val input = EditText(rootView.context)
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)
            builder.setPositiveButton(
                "OK",
                DialogInterface.OnClickListener { dialog, which ->
                    topup = true
                    var balance: Int = 0
                    database.getReference("users").child(userId)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                            override fun onDataChange(ds: DataSnapshot) {
                                if (topup) {
                                    val data = ds.getValue(UserData::class.java) as UserData
                                    balance = data.balance
                                    val inputBalance: String = input.text.toString()
                                    balance += inputBalance.toInt()
                                    FirebaseDatabase.getInstance().getReference("users")
                                        .child(userId)
                                        .child("balance").setValue(balance)
                                    toast(rootView.context, "Top up success")
                                    topup = false
                                }
                            }

                        })
                })
            builder.setNegativeButton(
                "Cancel",
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                    topup = false
                })

            builder.show()
        }
        return rootView
    }

    private fun getAdditionalData(view: View) {
        database.getReference("users").child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(ds: DataSnapshot) {
                    val data = ds.getValue(UserData::class.java) as UserData
                    view.balance.text = data.balance.toString()
                }

            })
    }

}
