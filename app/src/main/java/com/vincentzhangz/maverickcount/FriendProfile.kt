package com.vincentzhangz.maverickcount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vincentzhangz.maverickcount.models.Status
import kotlinx.android.synthetic.main.fragment_friend_profile.*
import kotlinx.android.synthetic.main.fragment_friend_profile.view.*


class FriendProfile : Fragment() {
    private val database = FirebaseDatabase.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_friend_profile, container, false)
        val name = arguments?.getString("name")
        rootView.name.text = name
        setStatus(rootView)
        return rootView
    }

    private fun setStatus(view: View) {
        val uid = arguments?.getString("uid").toString()
        database.getReference("users").child(uid).child("status")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(ds: DataSnapshot) {
                    val status = ds.getValue(Status::class.java) as Status
                    view.paid.text = "Paid : " + status.paid
                    view.unpaid.text = "Unpaid : " + status.unpaid
                    view.late.text = "Late : " + status.late

                    val color = intArrayOf(R.color.paid, R.color.late, R.color.unpaid)
                    val entries = ArrayList<PieEntry>()

                    if (status.paid != 0)
                        entries.add(PieEntry(status.paid.toFloat(), "Paid"))
                    if (status.late != 0)
                        entries.add(PieEntry(status.late.toFloat(), "Late"))
                    if (status.unpaid != 0)
                        entries.add(PieEntry(status.unpaid.toFloat(), "Unpaid"))

                    val set = PieDataSet(entries, "Payment History")
                    set.setColors(color, context)
                    val data = PieData(set)
                    pie_chart.data = data
                    pie_chart.description.text = ""
                    pie_chart.legend.isEnabled = false
                    pie_chart.invalidate()
                }

            })
    }

}
