package com.folu.jejakkaki.model

data class Taman(
    val id: Int,
    val namaTaman: String,
    val facebook: String?,
    val twitter: String?,
    val youtube: String?,
    val instagram: String?,
    val logo: String,
    val logo2: String?,
    val bgPic: String,
    val info: Int?,
    val animals: List<Animal?>,
    val activities: List<Activity?>,
    val car1: String? = null,
    val car2: String? = null,
    val car3: String? = null,
)

data class Animal(
    val id: Int,
    val desc: Int,
    val pic: String,
)

data class Activity(
    val id: Int,
    val desc: Int,
    val pic: String,
)