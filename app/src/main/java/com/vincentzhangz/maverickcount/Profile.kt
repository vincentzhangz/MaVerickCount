package com.vincentzhangz.maverickcount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.vincentzhangz.maverickcount.utilities.UserUtil
import kotlinx.android.synthetic.main.fragment_profile.view.*

class Profile : Fragment() {

    private val _databaseUrlPrefix = "gs://maverick-count.appspot.com/"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        val storage = FirebaseStorage.getInstance()
        val userId = UserUtil.getUserId(activity!!.applicationContext)
        val parsedUrl = _databaseUrlPrefix + "profile-image/$userId"
        val gsReference =
            storage.getReferenceFromUrl(parsedUrl).downloadUrl.addOnSuccessListener {
                Glide.with(rootView).load(it).circleCrop().into(rootView.profile_image)
            }.addOnFailureListener {
                Glide.with(rootView)
                    .load(R.drawable.person_icon_foreground)
                    .circleCrop()
                    .into(rootView.profile_image)
            }

        return rootView
    }


}
