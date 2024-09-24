package com.example.wishlist


import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    val list: MutableList<Item> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        val ListsRv = findViewById<RecyclerView>(R.id.listsRv)
        val adapter = ListAdapter(list)

        ListsRv.adapter = adapter


        ListsRv.layoutManager = LinearLayoutManager(this)


        findViewById<Button>(R.id.button).setOnClickListener {

            val name = findViewById<EditText>(R.id.name_input)
            val price = findViewById<EditText>(R.id.price_input)
            val url = findViewById<EditText>(R.id.url_input)
            name.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            price.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            url.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

            if (name.text.isBlank() || price.text.isBlank() || url.text.isBlank()){
                Toast.makeText(it.context, "One of the three field is missing", Toast.LENGTH_SHORT).show()
            }
            else {
                var item: Item = Item(
                    name.text.toString(),
                    price.text.toString().toDouble(),
                    url.text.toString()
                )
                list.add(item)
            }
            adapter.notifyDataSetChanged()


        }
    }
}
