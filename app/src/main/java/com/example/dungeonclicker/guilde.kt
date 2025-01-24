package com.example.dungeonclicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class Guilde : Fragment() {
    private val stats : Transmision_stats by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_guilde, container, false)

        //Le texte a coté de la bourse qu'on affichera a chaque incrémentation
        val texteBourse = view.findViewById<TextView>(R.id.texte_bourse)
        //Affichages des statistiques
        val statView = view.findViewById<TextView>(R.id.statistique)
        //Affichage des stats dans le textview
        statView.text = "Statistiques du Joueur :\nMonstre tués : ${stats.monster_mort.value}\nDégats par clic : ${stats.lvl_degat.value}\nOr reçu par clic : ${stats.lvl_OrnClick.value}\nNiveau des primes : ${stats.lvl_prime.value}"

        //Gestion des deux bouton de se fragment
        val boutonReset = view.findViewById<Button>(R.id.bouton_reset)
        val boutonSave = view.findViewById<Button>(R.id.bouton_save)

        boutonSave.setOnClickListener{
            //On utilise ceci pour recuperer la fonction saveData definie dans mainactivity
            (requireActivity() as? MainActivity)?.saveData()
        }
        boutonReset.setOnClickListener{
            stats.or.value = 0
            stats.lvl_prime.value = 1
            stats.lvl_degat.value = 1
            stats.lvl_OrnClick.value = 1
            stats.monster_mort.value = 0
            (requireActivity() as? MainActivity)?.resetData()
            (requireActivity() as? MainActivity)?.saveData()
            statView.text = "Statistiques du Joueur :\nMonstre tués : ${stats.monster_mort.value}\nDégats par clic : ${stats.lvl_degat.value}\nOr reçu par clic : ${stats.lvl_OrnClick.value}\nNiveau des primes : ${stats.lvl_prime.value}"
        }

        //Observer les changements de taille de texte depuis le ViewModel
        stats.or.observe(viewLifecycleOwner) { nombre ->
            texteBourse.text = nombre.toString()
        }
        return view
    }

}
