package com.example.kebabapp.fragments.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kebabapp.KebabPlaces
import com.example.kebabapp.R

class AdapterFavoritesClass(
    private val dataList: KebabPlaces,
) : RecyclerView.Adapter<AdapterFavoritesClass.ViewHolderClass>() {
    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvName: TextView = itemView.findViewById(R.id.fav_kebab_name)
        val rvAddress: TextView = itemView.findViewById(R.id.fav_kebab_address)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_fav_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(
        holder: ViewHolderClass,
        position: Int,
    ) {
        val currentItem = dataList[position]
        holder.rvName.text = currentItem.name
        holder.rvAddress.text = currentItem.address
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
