package com.tron.familytree.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Photo(
    var publisher: String = "",
    var photo: String? = null,
    var id: String? = null,
    var createTime: Long? = Calendar.getInstance().time.time
): Parcelable