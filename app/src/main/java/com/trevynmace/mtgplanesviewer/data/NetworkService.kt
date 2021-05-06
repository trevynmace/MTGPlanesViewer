package com.trevynmace.mtgplanesviewer.data

import com.trevynmace.mtgplanesviewer.data.model.Card
import com.trevynmace.mtgplanesviewer.data.model.MTGSet
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkService {
    private val scope = CoroutineScope(Dispatchers.IO)

    private const val baseUrl = "https://api.magicthegathering.io/"

    private val mService: MTGService

    init {
        mService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(MTGService::class.java)
    }

    suspend fun getCardsAsync(pageSize: Int, name: String = ""): Deferred<List<Card>> =
        scope.async(Dispatchers.IO) {
            return@async mService.getCards(pageSize, name).cards
        }

    suspend fun getCardsAsync(pageNumber: Int, pageSize: Int, name: String = ""): Deferred<List<Card>> =
            scope.async(Dispatchers.IO) {
                return@async mService.getCards(pageNumber, pageSize, name).cards
            }

    suspend fun getSetsAsync(): Deferred<List<MTGSet>> =
        scope.async(Dispatchers.IO) {
            return@async mService.getSets().sets
        }
}