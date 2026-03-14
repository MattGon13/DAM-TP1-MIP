package com.example.catlendar.network

import retrofit2.http.GET

interface CatFactApiService {
    @GET("facts/random")
    suspend fun getRandomFact(): CatFactDto
}
