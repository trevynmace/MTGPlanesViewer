package com.trevynmace.mtgplanesviewer.data

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

    suspend fun getCardsAsync(): Deferred<String> =
        scope.async(Dispatchers.IO) {
            val result = mService.getCards()
            return@async result.cards[0].name
        }
}