package com.johnny.constanta_test

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.johnny.constanta_test.model.Film
import com.johnny.constanta_test.util.DataRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val films: LiveData<List<Film>> = DataRepository.getFilms {
        Toast.makeText(application, it, Toast.LENGTH_LONG).show()
        Log.d("TAG", it)
    }
}