package com.tron.familytree.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
    var publisher: String = "",
    var photo: String? = null,
    var id: String? = null
): Parcelable