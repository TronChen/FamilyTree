package com.tron.familytree.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Episode(
    var user : String = "",
    var content : String = "",
    var title : String = "",
    var time : String = "",
    var location : String = ""
): Parcelable