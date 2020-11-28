package com.tron.familytree.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ChatRoom(
    var id : String = "",
    var userImage : String,
    var attenderId : List<String>,
    var message : List<Message>
):Parcelable

@Parcelize
data class Message(
    var user : String = "",
    var time : Long = 0L,
    var text : String
): Parcelable