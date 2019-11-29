package com.example.cacherun

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
// TODO: Make this change posts to an array of coupons
class CouponAdapter (val posts: ArrayList<String>) : RecyclerView.Adapter<CouponAdapter.ViewHolder>() {

    override fun getItemCount() = posts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_coupon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.firstName.text = posts[position]
        // TODO: set holder.coupImage to posts[coupon.couponImage]
        holder.coupImg.setImageResource(R.drawable.coupon)
        // TODO: Display coupon distance to
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val firstName: TextView = itemView.findViewById(R.id.firstName)
        val coupImg:ImageView = itemView.findViewById(R.id.couponImage)
    }

}