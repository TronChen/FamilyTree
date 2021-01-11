package com.tron.familytree.data

import android.os.Parcelable
import com.tron.familytree.util.UserManager
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Episode(
    var user: String = "",
    var userId: String = UserManager.email.toString(),
    var familyId: String? = null,
    var id: String? = null,
    var content: String = "",
    var title: String = "",
    var time: String = "",
    var location: String = "",
    var latitude: Double? = null,
    var longitude: Double? = null
): Parcelable