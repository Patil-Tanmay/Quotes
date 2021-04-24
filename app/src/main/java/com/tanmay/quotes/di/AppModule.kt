package com.tanmay.quotes.di

import android.content.Context
import androidx.room.Room
import com.tanmay.quotes.api.QuotesApi
import com.tanmay.quotes.db.QuotesDataDao
import com.tanmay.quotes.db.QuotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(QuotesApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideQuotesAPi(retrofit: Retrofit) : QuotesApi=
        retrofit.create(QuotesApi::class.java)


    @Provides
    @Singleton
    fun provideSavedQuotesDatabase(@ApplicationContext context: Context):
            QuotesDatabase{
                    return Room.databaseBuilder(
                        context,
                        QuotesDatabase::class.java,
                        "SavedQuotesDatabase"
                    ).build()
    }


}