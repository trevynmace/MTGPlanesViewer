package com.trevynmace.mtgplanesviewer.data.model

import com.squareup.moshi.Json

data class Cards (
    @Json(name = "cards") val cards: List<Card>
)