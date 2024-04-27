package com.folu.jejakkaki.model

import android.os.Parcelable
import com.folu.jejakkaki.R
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class Place(
    val id: Int,
    val name: String,
    val coordinate: LatLng,
    val image: Int = R.drawable.gunung_leuser
) : Parcelable