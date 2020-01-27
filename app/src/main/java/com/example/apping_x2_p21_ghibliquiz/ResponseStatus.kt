package com.example.apping_x2_p21_ghibliquiz

import java.io.Serializable

data class ResponseStatus (
    val isGoodResponse: Boolean,
    val filmUrl: String,
    val infoFilm: List<String>
): Serializable