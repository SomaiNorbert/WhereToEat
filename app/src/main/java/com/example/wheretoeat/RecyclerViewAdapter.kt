package com.example.wheretoeat

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wheretoeat.models.ExampleItem


class RecyclerViewAdapter(
    private val exampleList: List<ExampleItem>,
    private val listener: OnItemClickedListener,
    private val context: Fragment
) : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.example_item, parent, false)
        return RecyclerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val currentItem = exampleList[position]

        Glide.with(context).load(currentItem.imageRes).into(holder.imageView)
        holder.textTitle.text = currentItem.title
        holder.textAddress.text = currentItem.address
        val txt = "Price: " + currentItem.price.toString()
        holder.textPrice.text = txt
        if(currentItem.favorite){
            holder.imageViewStar.setImageResource(R.drawable.favorite);
        }else{
            holder.imageViewStar.setImageResource(R.drawable.star);
        }
    }

    override fun getItemCount() = exampleList.size

    inner class RecyclerViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val textTitle: TextView = itemView.findViewById(R.id.text_view_title)
        val textAddress: TextView = itemView.findViewById(R.id.text_view_address)
        val textPrice: TextView = itemView.findViewById(R.id.text_view_price)
        val imageViewStar: ImageView = itemView.findViewById(R.id.image_view_star)

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position:Int =  adapterPosition;
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickedListener{
        fun onItemClick(position:Int)
    }



}