package com.tanmay.quotes.api

import retrofit2.http.GET
import retrofit2.http.Query

interface QuotesApi {

    companion object{
        const val BASE_URL = "https://quote-garden.herokuapp.com/"
    }


    @GET("api/v3/quotes")
    suspend fun getQuotes(
        @Query("page") page:Int,
        @Query("limit") limit:Int
    ) : Quotes

}