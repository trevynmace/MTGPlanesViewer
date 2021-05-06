package com.trevynmace.mtgplanesviewer.data

import com.trevynmace.mtgplanesviewer.data.model.Cards
import com.trevynmace.mtgplanesviewer.data.model.MTGSets
import retrofit2.http.GET
import retrofit2.http.Query

interface MTGService {
    @GET("v1/cards/")
    suspend fun getCards(
            @Query("pageSize") pageSize: Int,
            @Query("name") name: String,
            @Query("set") set: String,
            @Query("colors") colors: List<String>): Cards

    @GET("v1/cards/")
    suspend fun getCards(
            @Query("page") pageNumber: Int,
            @Query("pageSize") pageSize: Int,
            @Query("name") name: String,
            @Query("set") set: String,
            @Query("colors") colors: List<String>): Cards

    @GET("v1/sets/")
    suspend fun getSets(): MTGSets
}