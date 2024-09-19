package com.example.codepathmail

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {


    lateinit var emails: List<Email>

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val emailsRv = findViewById<RecyclerView>(R.id.emailsRv)

        emails = EmailFetcher.getEmails()

        val adapter = EmailAdapter(emails)

        emailsRv.adapter = adapter

        emailsRv.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.button).setOnClickListener {

            val newEmails = EmailFetcher.getNext5Emails()

            (emails as MutableList<Email>).addAll(newEmails)

            adapter.notifyDataSetChanged()
        }




    }
}

