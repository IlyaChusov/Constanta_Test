package com.johnny.constanta_test.util

import com.johnny.constanta_test.model.FilmsArray
import retrofit2.Call
import retrofit2.http.GET

interface NetworkAPI {
    @GET("constanta-android-dev/intership-wellcome-task/main/films.json")
    fun getFilms(): Call<FilmsArray>
}