package com.example.cacherun

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
// TODO: Make this change posts to an array of coupons
class CouponAdapter (val posts: ArrayList<Coupon>) : RecyclerView.Adapter<CouponAdapter.ViewHolder>() {

    override fun getItemCount() = posts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_coupon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.firstName.text = posts[position].name
        holder.coupImg.setImageResource(posts[position].imageId)
        holder.deltaD.text = posts[position].deltaD.toString() + "m"
        holder.coupImg.setOnClickListener {
            for (coupon in posts) {
                coupon.isSelected = false
            }
            posts[position].isSelected = true
            holder.itemView.setBackgroundColor(Color.GREEN)
        }


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val firstName: TextView = itemView.findViewById(R.id.firstName)
        val coupImg:ImageView = itemView.findViewById(R.id.couponImage)
        val deltaD: TextView = itemView.findViewById(R.id.distanceToTextView)


    }

}