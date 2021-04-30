package com.trevynmace.mtgplanesviewer.data.model

import com.squareup.moshi.Json
import java.io.Serializable

data class Card (
    val name: String = "",
    val imageUrl: String = "",
    @Json(name="cmc") val convertedManaCost: Int = 0,
    val type: String = "",
    val rarity: String = "",
    val set: String = "",
    @Json(name="text") val description: String = "",
    val artist: String = "",
    val power: String = "",
    val toughness: String = "",
    val flavor: String = ""
) : Serializable