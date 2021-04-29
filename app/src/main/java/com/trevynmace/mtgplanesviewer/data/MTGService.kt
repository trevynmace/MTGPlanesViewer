package com.trevynmace.mtgplanesviewer.data

import com.trevynmace.mtgplanesviewer.data.model.Cards
import retrofit2.http.GET
import retrofit2.http.Query

interface MTGService {
    @GET("v1/cards/")
    suspend fun getCards(@Query("pageSize") pageSize: Int): Cards
}