package com.tron.familytree.util

enum class CurrentFragmentType(val value: String) {
    BRANCH("家族樹"),
    FAMILY("家族"),
    FAMILY_EVENT("家族活動"),
    FAMILY_ALBUM("家族相簿"),
    MAPS("成員位置"),
    MESSAGE("聊天室"),
    CHATROOM(""),
    PROFILE("個人"),
    PROFILE_USER_EDIT("編輯個人資料"),
    LOGIN(""),
    CALENDAR("參加的活動日期"),
    QR_CODE_SCAN("掃描QR code"),
    QR_CODE("顯示QR code")
}