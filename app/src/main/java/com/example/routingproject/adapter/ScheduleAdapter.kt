package com.example.routingproject.adapter

import android.annotation.SuppressLint
import com.example.routingproject.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView



class ScheduleAdapter(private val itemList: MutableList<String>) :
    RecyclerView.Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.schedule_item_view, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    // Yeni bir öğe eklemek için bu metodu kullanın
    @SuppressLint("NotifyDataSetChanged")
    fun addItem(newItem: String) {
        itemList.add(newItem)
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }
}



