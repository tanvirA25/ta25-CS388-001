package com.example.bitfitpt1


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val lists: List<Item>):RecyclerView.Adapter<ItemAdapter.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val foodTextView: TextView
        val calorieTextView: TextView

        init {
            foodTextView = itemView.findViewById(R.id.foodName)
            calorieTextView = itemView.findViewById(R.id.calorieInt)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.show_item, parent, false)
        return ViewHolder(contactView)
    }
    override fun getItemCount(): Int{
        return lists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = lists.get(position)
        holder.foodTextView.text = list.name
        holder.calorieTextView.text = list.calorie.toString()

    }
}