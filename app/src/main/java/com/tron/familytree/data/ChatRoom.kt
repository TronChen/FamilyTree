package com.tron.familytree.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ChatRoom(
    var id : String = "",
    var userImage : List<String> = emptyList(),
    var attenderId : List<String> = emptyList(),
    var attenderName : List<String> = emptyList()
):Parcelable

@Parcelize
data class Message(
    var user : String = "",
    var time : Long = 0L,
    var text : String
): Parcelable