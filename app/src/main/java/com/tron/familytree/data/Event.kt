package com.tron.familytree.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event (
    var publisher : String = "",
    var title : String = "",
    var time : Long = 0L,
    var attender : List<String>,
    var content : String = ""
):Parcelable