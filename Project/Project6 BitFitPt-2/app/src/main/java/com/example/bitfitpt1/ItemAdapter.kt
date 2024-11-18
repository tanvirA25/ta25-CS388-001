package com.example.bitfitpt1


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val lists: List<Item>, private val onDeleteClicked: (Item)-> Unit):RecyclerView.Adapter<ItemAdapter.ViewHolder>(){
    private var selectedItemPosition: Int? = null
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val foodTextView: TextView
        val calorieTextView: TextView
        val delete: TextView

        init {
            foodTextView = itemView.findViewById(R.id.foodName)
            calorieTextView = itemView.findViewById(R.id.calorieInt)
            delete = itemView.findViewById(R.id.delete)
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

        holder.delete.visibility = if(position == selectedItemPosition) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            selectedItemPosition = if (selectedItemPosition == position) null else position
            notifyDataSetChanged()
        }

        holder.delete.setOnClickListener{
            onDeleteClicked(list)
            selectedItemPosition = null
            notifyDataSetChanged()
        }

    }
}