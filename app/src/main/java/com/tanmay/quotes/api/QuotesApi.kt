package com.tanmay.quotes.api

import com.tanmay.quotes.data.models.QuotesGenres
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

    @GET("api/v3/quotes")
    suspend fun getQuotesByGenre(
        @Query("genre") tag: String,
        @Query("page") page:Int,
        @Query("limit") limit:Int
    ) : Quotes

    @GET("api/v3/genres")
    suspend fun getGenres(): QuotesGenres

}