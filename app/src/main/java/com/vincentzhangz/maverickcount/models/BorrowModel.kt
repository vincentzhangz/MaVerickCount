package com.vincentzhangz.maverickcount.models

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vincentzhangz.maverickcount.R
import com.vincentzhangz.maverickcount.utilities.OtherUtil
import com.vincentzhangz.maverickcount.utilities.SystemUtility.Companion.dateFormatter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.borrow_item.view.*
import java.time.LocalDateTime
import java.time.ZoneOffset

class BorrowRequest(
    val borrower: String,
    val lender: String,
    val amount: Long,
    val requestDate: Long,
    val deadlineDate: Long
) {
    constructor() : this(
        "", "", 0, System.currentTimeMillis(), LocalDateTime.now().plusDays(7).toInstant(
            ZoneOffset.ofTotalSeconds(0)
        ).toEpochMilli()
    )
}

class BorrowRequestData(val uid: String, val borrowRequest: BorrowRequest) {
    constructor() : this(
        "", BorrowRequest(
            "", "", 0, System.currentTimeMillis(), LocalDateTime.now().plusDays(7).toInstant(
                ZoneOffset.ofTotalSeconds(0)
            ).toEpochMilli()
        )
    )
}

class BorrowItem(val borrowData: BorrowRequestData) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.borrow_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        FirebaseDatabase.getInstance().getReference("users")
            .child(borrowData.borrowRequest.borrower).child("name")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(ds: DataSnapshot) {
                    viewHolder.itemView.txv_borrower_name.text =
                        ds.getValue(String()::class.java).toString()
                    viewHolder.itemView.txv_borrow_request_date.text =
                        "Request Date: " + dateFormatter(
                            OtherUtil.getTimeFromMilis(borrowData.borrowRequest.requestDate),
                            "dd-MM-yyyy HH:mm"
                        )
                    viewHolder.itemView.txv_borrow_deadline_date.text = "Deadline Date: " +
                            dateFormatter(
                                OtherUtil.getTimeFromMilis(borrowData.borrowRequest.deadlineDate),
                                "dd-MM-yyyy HH:mm"
                            )
                    viewHolder.itemView.txv_borrow_amount.text = "Amount: " +
                            borrowData.borrowRequest.amount.toString()
                }
            })
    }

}