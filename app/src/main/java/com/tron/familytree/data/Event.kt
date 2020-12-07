package com.tron.familytree.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    var publisher: String = "",
    var title: String = "",
    var time: String = "",
    var attender: List<String>? = null,
    var content: String = "",
    var location: String = ""
):Parcelable