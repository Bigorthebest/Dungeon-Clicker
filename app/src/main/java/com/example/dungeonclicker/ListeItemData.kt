package com.example.dungeonclicker

//Data pour les monstres
data class ListeItemData(var imageId: Int, var nom: String, var hp : Int, var prime : Int )

//Data pour la liste d'amélioration
data class ListeAmelioration(var nom: String, var description : String, var prix : Int)
