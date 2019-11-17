package com.example.horizontalrecyclerview

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class RecyclerViewAdapter(context: Context, names:ArrayList<String>, imageUrls:ArrayList<String>) {
    //vars
    private var mNames = ArrayList<String>()
    private var mImageUrls = ArrayList<String>()
    private val mContext:Context



    init{
        mNames = names
        mImageUrls = imageUrls
        mContext = context
    }

    fun getItemCount() = mNames.size

    fun onCreateViewHolder(parent:ViewGroup, viewType:Int):ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_listitem, parent, false)
        return ViewHolder(view)
    }
    fun onBindViewHolder(holder:ViewHolder, posistion:Int) {
        holder.name.text = "test!!"
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)

    }
}

//public class RecyclerViewAdapter {
//
//    private static final String TAG = "RecyclerViewAdapter";
//
//    //vars
//    private ArrayList<String> mNames = new ArrayLists<>();
//    private ArrayList<String> mImageUrls = new ArrayLists<>();
//    private Context mContext;
//
//    public RecyclerViewAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls) {
//        mNames = names;
//        mImageUrls = imageUrls;
//        mContext = context;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.form(parent.getContect()).inflate(R.layout.layout_listitem, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int posistion) {
//        Log.d(TAG, "onBindViewHolder: called.");
//
//        Glide.with(mContext)
//            .asBitMap()
//            .load(mImageUrls.get(position))
//            .into(holder.image);
//
//        holder.image.setOnClickListener(new View.OnClickListener() {
//            Log.d(TAG, "onClick: clicked on an image: " + mnames.get(position));
//            Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();
//        })
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mNames.size();
//    }
//
//    public class ViewHolder extentds RecyclerView.ViewHolder extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
//
//        CircleImageView image;
//        TextView name;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            image = itemView.findViewById(R.id.image);
//            name = itemView.findViewById(R.id.name);
//        }
//
//    }
//
//}

