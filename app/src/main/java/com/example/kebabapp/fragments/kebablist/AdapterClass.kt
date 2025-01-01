package com.example.kebabapp.fragments.kebablist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kebabapp.KebabPlaces
import com.example.kebabapp.R

class AdapterClass(private val dataList: KebabPlaces): RecyclerView.Adapter<AdapterClass.ViewHolderClass>() {
    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rvName:TextView = itemView.findViewById(R.id.kebab_name)
        val rvAddress:TextView = itemView.findViewById(R.id.kebab_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvName.text = currentItem.kebabName
        holder.rvAddress.text = currentItem.address

    }
}