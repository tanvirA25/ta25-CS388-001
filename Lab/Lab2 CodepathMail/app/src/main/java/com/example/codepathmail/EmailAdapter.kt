package com.example.codepathmail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmailAdapter(private val emails: List<Email>): RecyclerView.Adapter<EmailAdapter.ViewHolder>() {
        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


            // as you render a row.
            val senderTextView: TextView
            val titleTextView: TextView
            val summaryTextView: TextView
            val imageImageView: ImageView


            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each sub-view
            init {
                // TODO: Store each of the layout's views into
                // the public final member variables created above
            }
            init {
                senderTextView = itemView.findViewById(R.id.sender)
                titleTextView = itemView.findViewById(R.id.emailTitle)
                summaryTextView = itemView.findViewById(R.id.email_sum)
                imageImageView = itemView.findViewById(R.id.icon)

            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.email_item, parent, false)
        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {

        return emails.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val email = emails.get(position)
        holder.senderTextView.text = email.sender
        holder.titleTextView.text = email.title
        holder.summaryTextView.text = email.summary
        holder.imageImageView.setImageResource(email.image)
    }
}
