package com.trevynmace.mtgplanesviewer.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Card (
    val name: String = "",
    val imageUrl: String = "",
    val cmc: Int = 0,
    val type: String = "",
    val rarity: String = "",
    val set: String = "",
    val text: String = "",
    val artist: String = "",
    val power: String = "",
    val toughness: String = "",
    val flavor: String = ""
) : Serializable