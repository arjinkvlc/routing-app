package com.example.routingproject.adapter

import com.example.routingproject.R
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val itemTextView: TextView
    private val starButton: ImageButton
    private val optionsButton: ImageButton

    init {
        itemTextView = itemView.findViewById<TextView>(R.id.title_text_view)
        starButton = itemView.findViewById<ImageButton>(R.id.favorites_image_view)
        optionsButton = itemView.findViewById<ImageButton>(R.id.deleteButton)
    }

    fun bind(item: String?) {
        itemTextView.text = item
        // İsterseniz burada diğer bileşenlerin davranışını da tanımlayabilirsiniz.
    }
}
