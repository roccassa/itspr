package com.itspr.parcial2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/*
data class CharacterResponse(
    val results: List<Character>
)
data class Character(
    val id: String,
    val image: String,
    val name: String,
    val house: String,
    val gender: String,
    val actor: String,
    val alive: Boolean
)
*/
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