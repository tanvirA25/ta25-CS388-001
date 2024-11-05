package com.example.bitfitpt1

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val itemList: MutableList<Item> = mutableListOf()
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = ItemAdapter(itemList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadFromDatabase()

        findViewById<Button>(R.id.addButton).setOnClickListener {
            val intent = Intent(this, AddItem::class.java)
            startActivity(intent)
        }
    }

        private fun loadFromDatabase(){
            lifecycleScope.launch {
                (application as ItemApplication).db.itemDao().getAllItems().collect {databaseList ->
                    val items = databaseList.map { entity ->
                        Item(entity.name, entity.calorie)

                    }
                    itemList.clear()
                    itemList.addAll(items)
                    adapter.notifyDataSetChanged()

                    val totalCal = items.sumOf { it.calorie}
                    val averageCal = if (items.isNotEmpty()) totalCal / items.size else 0

                    findViewById<TextView>(R.id.average).text = "Average Calorie: $averageCal"
                }
            }

        }
}
