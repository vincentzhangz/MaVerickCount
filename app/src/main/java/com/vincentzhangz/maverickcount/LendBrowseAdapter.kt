package com.vincentzhangz.maverickcount

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.lend_item.view.*

class LendBrowseAdapter(val context:Context, val debts:List<Debt>):RecyclerView.Adapter<LendBrowseAdapter.ViewHolder>(){

    inner class ViewHolder(val lendView: View):RecyclerView.ViewHolder(lendView){

        fun setData(debt: Debt?){
            lendView.txv_amount.text=debt!!.ammount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.lend_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return debts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val debt=debts[position]
        holder.setData(debt)
    }

}