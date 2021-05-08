package com.trevynmace.mtgplanesviewer.data.model

data class MTGSet(
    val name: String = "",
    val code: String = "",
    val releaseDate: String = ""
) {
    override fun toString(): String {
        return "$code - $name"
    }
}