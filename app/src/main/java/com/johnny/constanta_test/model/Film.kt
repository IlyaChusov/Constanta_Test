package com.johnny.constanta_test.model

data class Film(
    val title: String,
    val directorName: String,
    val releaseYear: Int,
    var actors: List<Actor>
)
