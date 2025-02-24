package com.example.dungeonclicker

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import kotlin.random.Random

class Donjon : Fragment() {
    private val stats : Transmision_stats by activityViewModels()
    var compteurOr: Int = 0
    //Nombre de monstres dans le tableau
    var nbr_monstre : Int = 6
    //Nombre de boss
    var nbr_boss : Int = 1
    //Compteur avant proc du boss
    var compt_boss : Int = 0
    //Nombre de monstrer a tuer avant l'apparition du boss
    var apparition_boss : Int = 10

    //Liste des monstres et leurs informations
    private val listeMonstre = arrayListOf(
        ListeItemData((R.drawable.mimicgrabd),"Mimic",60,120),
        ListeItemData((R.drawable.cervaux),"Dévoreur",40,80),
        ListeItemData((R.drawable.feu_follet),"Feu Follet",30,60),
        ListeItemData((R.drawable.gobelin),"Gobelin",75,150),
        ListeItemData((R.drawable.squelette),"Squelette",100,200),
        ListeItemData((R.drawable.tyranoeil),"Spectateur",90,180),
        )
    //Liste des boss sous le même format
    private val listeBoss = arrayListOf(
        ListeItemData((R.drawable.boss),"Tyranoeil",600,1200)
    )

    //ref au view model
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_donjon, container, false)

        //Hauteur de l'ecran (pour le posisionement du Toast des primes
        val hauteurEcran = resources.displayMetrics.heightPixels

        compteurOr = 0
        if (stats.or.value!! > 0){
            compteurOr += stats.or.value!!
        }

        //Un variable pour stocker quels monstres arrivera dans le donjon ensuite
        var randomMonstre : Int
        // Compteur d'hp de la créature actuelle
        var compteurHp : Int


        //Le texte a coté de la bourse qu'on affichera a chaque incrémentation
        val texteBourse = view.findViewById<TextView>(R.id.texte_bourse)
        //Bouton monstre - le monstre qu'on clique pour gagner le l'or
        val boutonMonstre = view.findViewById<ImageButton>(R.id.monstre)
        // NomInfo - Le titre en desous du monstre
        val nomInfo = view.findViewById<TextView>(R.id.nom_monstre)

        //On recupére l'ancien monstre si il avait deja été gen
        if(stats.id_monstre.value!! == 99){
            randomMonstre = Random.nextInt(0,nbr_monstre)
            compteurHp = listeMonstre[randomMonstre].hp * stats.lvl_prime.value!!
            stats.id_monstre.value = randomMonstre
            boutonMonstre.setImageResource(listeMonstre[randomMonstre].imageId)
            nomInfo.text = "${listeMonstre[randomMonstre].nom} ${compteurHp}/${listeMonstre[randomMonstre].hp * stats.lvl_prime.value!!}"
        }
        else if(stats.id_monstre.value!! > 99){ //au dessus de 100 donc un boss
            randomMonstre = stats.id_monstre.value!! - 100
            compteurHp = stats.hp_restant.value!!
            //Gestion de l'affiche du BOSS celon le chiffre random
            boutonMonstre.setImageResource(listeBoss[randomMonstre].imageId)
            nomInfo.text = "${listeBoss[randomMonstre].nom} ${compteurHp}/${listeBoss[randomMonstre].hp * stats.lvl_prime.value!!}"
        }
        else{
            randomMonstre = stats.id_monstre.value!!
            compteurHp = stats.hp_restant.value!!
            //Gestion de l'affiche du monstre celon le chiffre random
            boutonMonstre.setImageResource(listeMonstre[randomMonstre].imageId)
            nomInfo.text = "${listeMonstre[randomMonstre].nom} ${compteurHp}/${listeMonstre[randomMonstre].hp * stats.lvl_prime.value!!}"
        }

        //Bug actuelle : Crash quand on essaye de re avoir le boss depuis la forge par exemple



        //Observer les changements de taille de texte depuis le ViewModel
        stats.or.observe(viewLifecycleOwner) { nombre ->
            texteBourse.text = nombre.toString()
        }

        boutonMonstre.setOnClickListener {
            //On enléve aussi les pv et on gére le cas limite
            compteurHp -= stats.lvl_degat.value!!
            stats.hp_restant.value = compteurHp
            if(compteurHp < 1){
                //On oublie pas la prime au joueurs !
                compteurOr += listeMonstre[randomMonstre].prime + (stats.lvl_prime.value!! * 100)
                //On update les choses en lien avec les morts d'un mostre
                stats.killboard(stats.monster_mort.value!! + 1)
                compt_boss++
                //Nouvelle notif de prime pour le joueurs
                val notifPrime = layoutInflater.inflate(R.layout.toast_prime, null)
                val texteToats = notifPrime.findViewById<TextView>(R.id.textView_toast)
                texteToats.text = "Prime de ${listeMonstre[randomMonstre].prime + (stats.lvl_prime.value!! * 100)} reçu !"
                val toast = Toast(requireContext())
                toast.view = notifPrime
                toast.duration = Toast.LENGTH_LONG
                toast.setGravity(Gravity.CENTER or Gravity.CENTER_HORIZONTAL, 0, -(hauteurEcran / 4))
                toast.show()
                //On reassigne le nouveau monstre (ou le boss)
                if(compt_boss > apparition_boss){
                    compt_boss = 0
                    //Mettre le random ici quand y en aura un
                    randomMonstre = 0
                    //L'id des boss commenceront par 100
                    stats.id_monstre.value = randomMonstre + 100
                    boutonMonstre.setImageResource(listeBoss[randomMonstre].imageId)
                    nomInfo.text = "${listeBoss[randomMonstre].nom} ${compteurHp}/${listeBoss[randomMonstre].hp}"
                    compteurHp = listeBoss[randomMonstre].hp * stats.lvl_prime.value!!
                }
                else {
                    randomMonstre = Random.nextInt(0, nbr_monstre)
                    stats.id_monstre.value = randomMonstre
                    boutonMonstre.setImageResource(listeMonstre[randomMonstre].imageId)
                    nomInfo.text =
                        "${listeMonstre[randomMonstre].nom} ${compteurHp}/${listeMonstre[randomMonstre].hp}"
                    compteurHp = listeMonstre[randomMonstre].hp * stats.lvl_prime.value!!
                }
            }
            //Sinon on update la boite de dialogue

            if(stats.id_monstre.value!! > 99) { //au dessus de 100 donc un boss
                nomInfo.text = "${listeBoss[randomMonstre].nom} ${compteurHp}/${listeBoss[randomMonstre].hp * stats.lvl_prime.value!!}"
            }
            else {
                nomInfo.text = "${listeMonstre[randomMonstre].nom} ${compteurHp}/${listeMonstre[randomMonstre].hp * stats.lvl_prime.value!!}"
            }
            //On augmente l'or
            compteurOr += stats.lvl_OrnClick.value!!
            stats.updateOr(compteurOr)
            //texteBourse.text = compteurOr.toString()
        }
        return view
    }
}
