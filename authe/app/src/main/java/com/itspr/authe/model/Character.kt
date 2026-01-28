package com.itspr.authe.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//HP

@Parcelize
data class CharacterResponse(
    val results: List<Character>
): Parcelable

@Parcelize
data class Character(
    val id: String,
    val image: String,
    val name: String,
    val house: String,
    val gender: String,
    val wizard: Boolean,
    val ancestry: String,
    val wand: Wand,
    val actor: String,
    val dateOfBirth: String,
    val eyeColour: String,
    val hairColour: String,
    val alive: Boolean,
    val alternate_names: List<String>
): Parcelable

@Parcelize
data class Wand(
    val wood: String,
    val core: String,
): Parcelable



/*
@Parcelize
data class Character(
    val id: Int,
    val age: Int,
    val birthdate: String,
    val description: String,
    val gender: String,
    val name: String,
    val occupation: String,
    val status: String,
    @SerializedName("portrait_path") val image: String,
    val phrases: List<String>,
    val first_appearance_ep: FirstE
) : Parcelable

@Parcelize
data class FirstE(
    val id: Int,
    val airdate: String,
    val description: String,
    @SerializedName("episode_number") val episodeNumber: Int,
    @SerializedName("image_path") val imagePath: String,
    val name: String,
    val season: Int,
    val synopsis: String
) : Parcelable

*/


//RICKY & MORTY
/*
@Parcelize
data class CharacterResponse(
    val results: List<Character>
):Parcelable


@Parcelize
data class Character(
    val id: Int,
    val name: String,
    val species: String,
    val status: String,
    val image: String,
    val gender: String,
    val type: String,
    val origin: Origin,
    val location: Location,
    val created: String,
    val url: String
):Parcelable


@Parcelize
data class Origin(
    val name: String,
    val url: String
): Parcelable

@Parcelize
data class Location(
    val name: String,
    val url: String
): Parcelable
*/


//fake sotre
@Parcelize
data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating
) : Parcelable

@Parcelize
data class Rating(
    val rate: Double,
    val count: Int
) : Parcelable
