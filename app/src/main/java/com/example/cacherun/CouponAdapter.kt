package com.example.cacherun

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CouponAdapter (val couponList: ArrayList<Coupon>) : RecyclerView.Adapter<CouponAdapter.ViewHolder>() {
    override fun getItemCount() = couponList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_coupon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.couponName.text = couponList[position].name
        holder.couponImage.setImageResource(couponList[position].imageId)
        holder.deltaD.text = couponList[position].deltaD.toString() + "m"
        holder.couponImage.setOnClickListener {
            for (coupon in couponList) {
                coupon.isSelected = false
            }
            couponList[position].isSelected = true
            holder.itemView.setBackgroundColor(Color.GREEN)
        }


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val couponName: TextView = itemView.findViewById(R.id.couponName)
        val couponImage:ImageView = itemView.findViewById(R.id.couponImage)
        val deltaD: TextView = itemView.findViewById(R.id.distanceToTextView)


    }

}