package com.tron.familytree.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Family(
    var id: String = "",
    var title: String = "",
    var attender: String = "",
    var episode: Episode? = null,
    var event: Event? = null,
    var photo: Photo? = null
): Parcelable