package com.tron.familytree.data

import android.os.Parcelable
import com.tron.familytree.family.create_event.EventType
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Event(
    var id: String = "",
    var publisher: String = "",
    var publisherFamilyId : String = "",
    var publisherId: String = "",
    var title: String = "",
    var time: String = "",
    var date: String = "",
    var attender: List<String>? = null,
    var content: String = "",
    var location: String = "",
    var eventType: EventType? = null,
    var eventTime: Long? = null,
    var eventMonth: String = ""
):Parcelable