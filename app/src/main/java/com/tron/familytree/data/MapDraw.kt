package com.tron.familytree.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class MapDraw(
    var canvas : Float = 0F
) : Parcelable