package com.itspr.micro.model


import com.google.gson.annotations.SerializedName

// Clase principal para el resultado de la búsqueda de CP
data class ZipCodeResponse(
    // El CP que buscaste
    @SerializedName("post code")
    val postCode: String,

    // El país
    val country: String,

    // Lista de lugares (asentamientos, colonias)
    val places: List<Place>
)

// Clase que representa cada asentamiento (colonia, municipio, estado)
data class Place(
    // Nombre del asentamiento/colonia
    @SerializedName("place name")
    val placeName: String,

    // Estado o Entidad Federativa
    @SerializedName("state")
    val state: String,

    // Abreviación del estado (útil)
    @SerializedName("state abbreviation")
    val stateAbbreviation: String,

    // Municipio o Ciudad (si aplica)
    @SerializedName("country abbreviation")
    val municipality: String,

    // Latitud y longitud (útil para mapeo)
    val latitude: String,
    val longitude: String
)