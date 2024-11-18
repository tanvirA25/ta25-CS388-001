package com.example.bitfitpt1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainFragment : Fragment(R.layout.fragment_main) {
    private val itemList: MutableList<Item> = mutableListOf()
    private lateinit var adapter: ItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        adapter = ItemAdapter(itemList) { itemToDelete ->
            deleteFromDatabase(itemToDelete)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        loadFromDatabase()


       }

        private fun loadFromDatabase(){
            lifecycleScope.launch {
                (requireActivity().application as ItemApplication).db.itemDao().getAllItems().collect {databaseList ->
                    val items = databaseList.map { entity ->
                        Item(entity.name, entity.calorie, entity.date)

                    }
                    itemList.clear()
                    itemList.addAll(items)
                    adapter.notifyDataSetChanged()

                    val totalCal = items.sumOf { it.calorie}
                    val averageCal = if (items.isNotEmpty()) totalCal / items.size else 0

                    view?.findViewById<TextView>(R.id.average)?.text = "Average Calorie: $averageCal"
                }
            }

        }
        private fun deleteFromDatabase(item: Item){
            lifecycleScope.launch {
                (requireActivity().application as ItemApplication).db.itemDao().delete(item.toEntity())
                itemList.remove(item)
                Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show()
                adapter.notifyDataSetChanged()
            }
        }


}
