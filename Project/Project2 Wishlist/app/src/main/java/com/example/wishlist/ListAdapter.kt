package com.example.wishlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(private val lists: List<Item>):RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val nameTextView: TextView
        val priceTextView: TextView
        val urlTextView: TextView

        init{

            nameTextView = itemView.findViewById(R.id.name)
            priceTextView = itemView.findViewById(R.id.price)
            urlTextView = itemView.findViewById(R.id.url)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {

        return lists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val list = lists.get(position)
        holder.nameTextView.text = list.name
        holder.priceTextView.text = list.price.toString()
        holder.urlTextView.text = list.url


    }


}
