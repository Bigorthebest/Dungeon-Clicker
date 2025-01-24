package com.example.dungeonclicker

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class managerListItem (view: View, private val listener: OnItemClickListener): RecyclerView.ViewHolder(view), View.OnClickListener{

        //Récupération des id(s)
        val titre : TextView = view.findViewById(R.id.titre_amelioration)
        val description : TextView = view.findViewById(R.id.description_améliration)
        val prix : TextView = view.findViewById(R.id.prix_améliration)
        val bouton : ImageButton = view.findViewById(R.id.bouton_upgrade)

    /*
        init {
            view.setOnClickListener(this)
            bouton.setOnClickListener {
                listener.upgrade()
            }
        }

        override fun onClick(p0: View?) {
            listener.upgrade()
        }

        interface OnItemClickListener{
            fun upgrade()
        }

     */
    init {
        // Détecter le clic sur l'élément
        bouton.setOnClickListener {
            listener.onBuyClick(adapterPosition)
        }
    }

    override fun onClick(p0: View?) {
        val position: Int = adapterPosition
        if(position != RecyclerView.NO_POSITION) {
            listener.onItemClick(position)
        }
    }

    // Interface pour gérer les clics
    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onBuyClick(position: Int)
    }
}
