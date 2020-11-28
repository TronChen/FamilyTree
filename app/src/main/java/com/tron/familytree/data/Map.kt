package com.tron.familytree.data

import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Map(
    var userId : String = "",
    var location : Float = 0F
): Parcelable