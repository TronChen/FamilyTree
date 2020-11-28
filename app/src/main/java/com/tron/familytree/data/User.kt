package com.tron.familytree.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class User(
    var name : String = "",
    var birth : String = "",
    var id : String = "",
    var fatherId : String,
    var motherId : String,
    var mateId : String,
    var familyId : String,
    var episode : List<Episode>,
    var userImage : String,
    var gender : String,
    var alive: String
): Parcelable