package com.tron.familytree.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class User(
    var name: String = "",
    var birth: String = "",
    var id: String = "",
    var fatherId: String? = null,
    var motherId: String? = null,
    var mateId: String? = null,
    var familyId: String? = null,
    var userImage: String? = null,
    var gender: String? = null,
    var alive: String? = null,
    var birthLocation: String? = null,
    var spanSize: Int? = 1,
    var deathDate: String? = null
): Parcelable

@Parcelize
data class UserEpisode(
    var episode: List<Episode>? = null
):Parcelable