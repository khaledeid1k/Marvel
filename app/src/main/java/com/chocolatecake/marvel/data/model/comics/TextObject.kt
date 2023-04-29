package com.chocolatecake.marvel.data.model.comics


import com.google.gson.annotations.SerializedName

data class TextObject(
    @SerializedName("language")
    val language: String? = null,
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("type")
    val type: String? = null
)