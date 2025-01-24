package com.example.dungeonclicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class adapter(
    private val itemList: ArrayList<ListeAmelioration>,
    private val listener: managerListItem.OnItemClickListener
) : RecyclerView.Adapter<managerListItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): managerListItem {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.tiem_forge, parent, false)
        return managerListItem(adapterLayout, listener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: managerListItem, position: Int) {
        val itemCourant = itemList[position]
        holder.titre.text = itemList[position].nom
        holder.prix.text = itemList[position].prix.toString()
        holder.description.text = itemList[position].description
    }
}
