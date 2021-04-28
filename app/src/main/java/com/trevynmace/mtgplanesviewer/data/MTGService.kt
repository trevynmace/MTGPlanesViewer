package com.trevynmace.mtgplanesviewer.data

import com.trevynmace.mtgplanesviewer.data.model.Cards
import retrofit2.http.GET

interface MTGService {
    @GET("v1/cards/")
    suspend fun getCards(): Cards
}