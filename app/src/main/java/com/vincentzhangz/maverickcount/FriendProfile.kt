package com.vincentzhangz.maverickcount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.vincentzhangz.maverickcount.models.Status
import kotlinx.android.synthetic.main.fragment_friend_profile.*
import kotlinx.android.synthetic.main.fragment_friend_profile.view.*
import kotlinx.android.synthetic.main.fragment_friend_profile.view.late
import kotlinx.android.synthetic.main.fragment_friend_profile.view.name
import kotlinx.android.synthetic.main.fragment_friend_profile.view.paid
import kotlinx.android.synthetic.main.fragment_friend_profile.view.unpaid
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.profile_image


class FriendProfile : Fragment() {
    private val database = FirebaseDatabase.getInstance()
    private val _databaseUrlPrefix = "gs://maverick-count.appspot.com/"
    private lateinit var userId: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_friend_profile, container, false)
        val name = arguments?.getString("name")
        userId = arguments?.getString("uid").toString()
        rootView.name.text = name
        val storage = FirebaseStorage.getInstance()
        val parsedUrl = _databaseUrlPrefix + "profile-image/$userId"
        storage.getReferenceFromUrl(parsedUrl).downloadUrl.addOnSuccessListener {
            Glide.with(rootView).load(it).circleCrop().into(rootView.profile_image)
        }.addOnFailureListener {
            Glide.with(rootView)
                .load(R.drawable.person_icon_foreground)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .into(rootView.profile_image)
        }
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
