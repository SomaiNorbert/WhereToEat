package com.example.wheretoeat

import android.graphics.BitmapFactory
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.net.URL


class RecyclerViewAdapter(private val exampleList: List<ExampleItem>) : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>(){

    class RecyclerViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val textTitle: TextView = itemView.findViewById(R.id.text_view_title)
        val textAddress: TextView = itemView.findViewById(R.id.text_view_address)
        val textPrice: TextView = itemView.findViewById(R.id.text_view_price)
        val imageViewStar: ImageView = itemView.findViewById(R.id.image_view_star)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.example_item, parent, false)
        return RecyclerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val currentItem = exampleList[position]
        //holder.imageView.setImageResource(R.drawable.defaultimg);
        holder.textTitle.text = currentItem.title
        holder.textAddress.text = currentItem.address
        holder.textPrice.text = currentItem.price.toString()
        if(currentItem.favorite){
            holder.imageViewStar.setImageResource(R.drawable.favorite);
        }else{
            holder.imageViewStar.setImageResource(R.drawable.star);
        }


    }

    override fun getItemCount() = exampleList.size


}