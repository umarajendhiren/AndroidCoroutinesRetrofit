package com.devtides.androidcoroutinesretrofit.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*to make this class singleton ,we need to add object  keyword.so that only one instance used by whole application
* if it is object class ,we no need to create new instance  every time when we need .we can directly access this class like static*/
object CountriesService {
    private val BASE_URL="https://raw.githubusercontent.com"

    fun getCountriesService():CountriesApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountriesApi::class.java)

    }
}