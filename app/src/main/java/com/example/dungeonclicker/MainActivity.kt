package com.example.dungeonclicker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.activity.viewModels
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.widget.TextView
import androidx.lifecycle.enableSavedStateHandles
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private val stats : Transmision_stats by viewModels()
    private lateinit var donjonFragment : Donjon
    private lateinit var forgeFragment: Forge
    private lateinit var guideFragment: Guilde
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        //Initialization des fragments
        donjonFragment = Donjon()
        forgeFragment = Forge()
        guideFragment = Guilde()

        //Item venant du main activity
        val titre = findViewById<TextView>(R.id.titre)


        loadData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Variable des boutons du menus
        val boutonDonjon = findViewById<ImageButton>(R.id.bouton_donjon)
        val boutonForge = findViewById<ImageButton>(R.id.bouton_forge)
        val boutonGuilde = findViewById<ImageButton>(R.id.bouton_guilde)

        showFragment(donjonFragment)

       //On ecoute chaque bouton
        boutonDonjon.setOnClickListener {
            titre.text = "Donjon"
            //On change les icons des menus en bas
            boutonForge.setImageResource(R.drawable.forge_icon)
            boutonDonjon.setImageResource(R.drawable.donjon_icon_activate)
            boutonGuilde.setImageResource(R.drawable.guilde_icon_d)
            showFragment(donjonFragment)
        }

        boutonForge.setOnClickListener {
            titre.text = "Forge"
            //On change les icons des menus en bas
            boutonForge.setImageResource(R.drawable.forge_icon_active)
            boutonDonjon.setImageResource(R.drawable.donjon_icon)
            boutonGuilde.setImageResource(R.drawable.guilde_icon_d)
            showFragment(forgeFragment)
        }

        boutonGuilde.setOnClickListener {
            titre.text = "Guilde"
            //On change les icons des menus en bas
            boutonForge.setImageResource(R.drawable.forge_icon)
            boutonDonjon.setImageResource(R.drawable.donjon_icon)
            boutonGuilde.setImageResource(R.drawable.guilde_icon)
            showFragment(guideFragment)
        }


    }

    private fun showFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.window, fragment)
        fragmentTransaction.addToBackStack(null) //fragmentTransaction.addToBackStack(null) peut bien marcher juste pour faire un retour
        fragmentTransaction.commit()
    }

    fun saveData() {
        // Initialiser SharedPreferences
        sharedPreferences = getSharedPreferences("BDD", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("OR", stats.or.value!!) // Enregistrer la valeur
        editor.putInt("DEGAT",stats.lvl_degat.value!!)
        editor.putInt("CLICK",stats.lvl_OrnClick.value!!)
        editor.putInt("PRIME",stats.lvl_prime.value!!)
        editor.putInt("MORT",stats.monster_mort.value!!)
        editor.apply() // Appliquer les changements
    }

    fun loadData(){
        sharedPreferences = getSharedPreferences("BDD", Context.MODE_PRIVATE)
        val saveOr = sharedPreferences.getInt("OR",0)
        val lvl_degat = sharedPreferences.getInt("DEGAT",1)
        val lvl_click = sharedPreferences.getInt("CLICK",1)
        val lvl_prime = sharedPreferences.getInt("PRIME",1)
        val mort = sharedPreferences.getInt("MORT",0)
        stats.updateOr(saveOr)
        stats.lvl_degat.value = lvl_degat
        stats.lvl_OrnClick.value = lvl_click
        stats.lvl_prime.value = lvl_prime
        stats.killboard(mort)
    }

    fun resetData(){
        sharedPreferences = getSharedPreferences("BDD", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("OR", 0) // Enregistrer la valeur
        editor.putInt("DEGAT",1)
        editor.putInt("CLICK",1)
        editor.putInt("PRIME",1)
        editor.putInt("MORT",0)
        editor.apply() // Appliquer les changements
    }

    //Fonction qui sauveguarde la progression à la fermeture de l'appli
    override fun onDestroy() {
        saveData()
        super.onDestroy()
    }
    //Fonction pour étre sur que ça soit bien sauveguarder
    override fun onPause() {
        saveData()
        super.onPause()
    }
    //Fonction pour être sur, sur, sur
    override fun onStop() {
        saveData()
        super.onStop()
    }

}

