package com.tron.familytree.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Family(
    var id: String = "",
    var title: String = ""
): Parcelable

@Parcelize
data class FamilyList(
    var family: Family? = null,
    var userList: List<User>? = mutableListOf<User>()
): Parcelable