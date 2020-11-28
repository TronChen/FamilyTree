package com.tron.familytree.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo (
    var publisher : String = "",
    var photo : List<String>,
    var title : String = "",
    var event : Event
): Parcelable