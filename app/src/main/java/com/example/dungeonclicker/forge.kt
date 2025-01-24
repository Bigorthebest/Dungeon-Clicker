package com.example.dungeonclicker

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast

class Forge : Fragment(), managerListItem.OnItemClickListener {
    private val stats : Transmision_stats by activityViewModels()
    //Variables de la classe
    lateinit var adapter : adapter
    var compteur_degat : Int = 1
    var compteur_or_click : Int = 1
    var compteur_prime : Int = 1
    val listeUpgrade = arrayListOf(
        ListeAmelioration("Améliration de Dégat ","Augmente les dégats de votre click de +1 !",100) ,
        ListeAmelioration("Améliration d'Or par click ","Augmente l'or que vous recenvez par click de +1 !",100) ,
        ListeAmelioration("Améliration des primes","Quand vous tué un monstre vous recevez une prime ! Cette améliration l'augmente !",500)
    )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_forge, container, false)

        //Associé les données à leurs visuels
        adapter = adapter(listeUpgrade, this)

        //Choisir une liste (RecyclerView), pour la remplir avec les données
        val list_items = view.findViewById<RecyclerView>(R.id.list_items)
        list_items.adapter = adapter
        list_items.layoutManager = LinearLayoutManager(requireContext())
        list_items.setHasFixedSize(true)

        //Le texte a coté de la bourse qu'on affichera a chaque incrémentation
        val texteBourse = view.findViewById<TextView>(R.id.texte_bourse)

        //Observer les changements de taille de texte depuis le ViewModel
        stats.or.observe(viewLifecycleOwner) { nombre ->
            texteBourse.text = nombre.toString()
        }

        //On regarde si des améliration n'avais pas deja été faite
        //Pour les degats
        if(stats.lvl_degat.value!! > 1){
            compteur_degat = stats.lvl_degat.value!!
            listeUpgrade[0].nom = "Améliration de Dégat +${compteur_degat - 1}"
            listeUpgrade[0].prix = (compteur_degat * 100)
        }
        //pour l'or par click
        if(stats.lvl_OrnClick.value!! > 1){
            compteur_or_click = stats.lvl_OrnClick.value!!
            listeUpgrade[1].nom = "Améliration d'Or par click +${compteur_or_click - 1}"
            listeUpgrade[1].prix = (compteur_or_click * 200)
        }
        //Pour les primes
        if(stats.lvl_prime.value!! > 1){
            compteur_prime = stats.lvl_prime.value!!
            listeUpgrade[2].nom = "Améliration des Primes +${compteur_prime - 1}"
            listeUpgrade[2].prix = 500 + 500 * stats.lvl_prime.value!! + (stats.lvl_prime.value!! * 100)
        }
        for ( index in listeUpgrade.indices) {
            adapter.notifyItemChanged(index)
        }
        return view

    }

    // Gestion des clics sur les éléments
    override fun onItemClick(position: Int) {
        val item = adapter.itemCount.toString()
        Toast.makeText(requireContext(), "Clic sur :", Toast.LENGTH_SHORT).show()
    }

    override fun onBuyClick(position: Int) {
        //Test pour savoir si le joueurs à assez d'or
        val notifOr = layoutInflater.inflate(R.layout.toast, null)
        if(stats.or.value!! < listeUpgrade[position].prix){
            val toast = Toast(requireContext())
            toast.view = notifOr
            toast.duration = Toast.LENGTH_SHORT
            toast.setGravity(Gravity.CENTER or Gravity.CENTER_HORIZONTAL, 0, 100)
            toast.show()
        }
        else{
            if (position == 0){
                compteur_degat += 1
                stats.upgradeDegat(compteur_degat)
                val or_actuel = stats.or.value!! - listeUpgrade[position].prix
                stats.updateOr(or_actuel)
                //On update l'améliration aussi
                listeUpgrade[position].nom = "Améliration de Dégat +${compteur_degat - 1}"
                listeUpgrade[position].prix += 100
            }
            if (position == 1){
                compteur_or_click += 1
                stats.upgradeOrClick(compteur_or_click)
                //Déduction de la somme
                val or_actuel = stats.or.value!! - listeUpgrade[position].prix
                stats.updateOr(or_actuel)
                //On update l'améliration aussi
                listeUpgrade[position].nom = "Améliration d'Or par click +${compteur_or_click - 1}"
                listeUpgrade[position].prix += 200
            }
            if (position == 2){
                compteur_prime += 1
                stats.ugradePrime(compteur_prime)
                //Déduction de la somme
                val or_actuel = stats.or.value!! - listeUpgrade[position].prix
                stats.updateOr(or_actuel)
                //On update l'améliration aussi
                listeUpgrade[position].nom = "Améliration des Primes +${compteur_prime - 1}"
                listeUpgrade[position].prix = 500 + 500 * stats.lvl_prime.value!! + (stats.lvl_prime.value!! * 100)
            }
            //Pour update notres liste d'items avec les changements

            adapter.notifyItemChanged(position)

        }

    }

}
