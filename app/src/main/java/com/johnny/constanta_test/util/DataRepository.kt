package com.johnny.constanta_test.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.johnny.constanta_test.model.Film
import com.johnny.constanta_test.model.FilmsArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DataRepository {

    companion object {
        private const val BASE_URL = "https://raw.githubusercontent.com/"

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        private val networkAPI = retrofit.create(NetworkAPI::class.java)

        fun getFilms(onError: (message: String) -> (Unit)): LiveData<List<Film>> {
            val liveData: MutableLiveData<List<Film>> = MutableLiveData()

            networkAPI.getFilms().enqueue(object : Callback<FilmsArray> {
                override fun onResponse(call: Call<FilmsArray>, response: Response<FilmsArray>) {
                    if (response.isSuccessful) {
                        val items = response.body()?.items
                        if (items == null)
                            liveData.value = listOf()
                        else {
                            items.forEach { film -> film.actors = film.actors.distinctBy { it.actorName } }
                            liveData.value = items
                        }
                    } else {
                        onError("Ошибка на стороне сервера: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<FilmsArray>, t: Throwable) {
                    onError("Неудачная попытка запроса: ${t.message}")
                }
            })

            return liveData
        }
    }
}