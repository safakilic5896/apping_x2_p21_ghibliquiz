package com.example.apping_x2_p21_ghibliquiz

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface WebServiceInterface {
    @GET
    fun filmItem(@Url url: String): Call<FilmItem>

    @GET("people")
    fun listAllPeople(): Call<List<PeopleItem>>
}