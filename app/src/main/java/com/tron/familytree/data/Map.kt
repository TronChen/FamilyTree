package com.tron.familytree.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Map(
    var userId: String = "",
    var userImage: String = "",
    var latitude: Double? = null,
    var longitude: Double? = null
): Parcelable