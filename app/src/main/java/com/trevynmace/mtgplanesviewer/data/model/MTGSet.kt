package com.trevynmace.mtgplanesviewer.data.model

data class MTGSet(
    val name: String = "",
    val code: String = ""
) {
    override fun toString(): String {
        return "$code - $name"
    }
}