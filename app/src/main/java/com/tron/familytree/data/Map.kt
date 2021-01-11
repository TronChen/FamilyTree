package com.tron.familytree.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Map(
    var userId: String = "",
    var familyId: String = "",
    var userImage: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null
): Parcelable