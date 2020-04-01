package com.vincentzhangz.maverickcount

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_lend_browse.*

class LendBrowseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lend_browse)

        val layoutManager= LinearLayoutManager(this)
        layoutManager.orientation=LinearLayoutManager.VERTICAL
        lendRecyclerView.layoutManager=layoutManager

        val lendBrowseAdapter=LendBrowseAdapter(this, LendBrowse.debt)
        lendRecyclerView.adapter=lendBrowseAdapter
    }
}
