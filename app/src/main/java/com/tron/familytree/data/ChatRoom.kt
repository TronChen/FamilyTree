package com.tron.familytree.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
data class ChatRoom(
    var id: String = "",
    var userImage: List<String> = emptyList(),
    var attenderId: List<String> = emptyList(),
    var attenderName: List<String> = emptyList(),
    var latestMessage: Message? = null
):Parcelable

@Parcelize
data class Message(
    var user: String = "",
    var userName: String = "",
    var userImage: String? = null,
    var time: Date? = Calendar.getInstance().time,
    var text: String? = null
): Parcelable