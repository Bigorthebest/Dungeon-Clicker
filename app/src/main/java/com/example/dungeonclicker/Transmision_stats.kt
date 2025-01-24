package com.example.dungeonclicker

import android.content.Context
import android.graphics.Typeface
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.content.SharedPreferences

class Transmision_stats : ViewModel() {
    var or = MutableLiveData<Int>().apply { value = 0 }
    var lvl_degat = MutableLiveData<Int>().apply { value = 1 }
    var lvl_OrnClick = MutableLiveData<Int>().apply { value = 1 }
    var lvl_prime = MutableLiveData<Int>().apply { value = 1 }
    var monster_mort = MutableLiveData<Int>().apply { value = 0 }
    var id_monstre = MutableLiveData<Int>().apply { value = 99 } //99 Pour avoir un indice de monstre qui n'existe pas (0 étant existant)
    var hp_restant = MutableLiveData<Int>().apply { value = 0 }

    fun updateOr(valeur: Int) {
        or.value = valeur
    }

    fun upgradeDegat(valeur : Int){
        lvl_degat.value = valeur
    }

    fun upgradeOrClick(valeur : Int){
        lvl_OrnClick.value = valeur
    }

    fun ugradePrime(valeur : Int){
        lvl_prime.value = valeur
    }

    fun killboard(valeur : Int){
        monster_mort.value = valeur
    }


}