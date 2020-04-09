package com.vincentzhangz.maverickcount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.vincentzhangz.maverickcount.models.Status
import com.vincentzhangz.maverickcount.utilities.SystemUtility
import com.vincentzhangz.maverickcount.utilities.UserUtil
import kotlinx.android.synthetic.main.fragment_friend_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class Profile : Fragment() {

    private val _databaseUrlPrefix = "gs://maverick-count.appspot.com/"
    private lateinit var database: FirebaseDatabase
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        database = FirebaseDatabase.getInstance()
        userId = UserUtil.getUserId(activity!!.applicationContext)

        val db = database.getReference("users").child(userId).child("name")

        db.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(ds: DataSnapshot) {
                rootView.name.text = ds.getValue(String()::class.java).toString()
                SystemUtility.toast(rootView.context, ds.getValue(String()::class.java).toString())
            }

        })
        val storage = FirebaseStorage.getInstance()
        val parsedUrl = _databaseUrlPrefix + "profile-image/$userId"
        storage.getReferenceFromUrl(parsedUrl).downloadUrl.addOnSuccessListener {
            Glide.with(rootView).load(it).circleCrop().into(rootView.profile_image)
        }.addOnFailureListener {
            Glide.with(rootView)
                .load(R.drawable.person_icon_foreground)
                .circleCrop()
                .into(rootView.profile_image)
        }

        setStatus(rootView)
        return rootView
    }

    private fun setStatus(view: View) {
        database.getReference("users").child(userId).child("status")
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
