package com.tron.familytree.util

import com.tron.familytree.FamilyTreeApplication

object UserManager {

    const val NAME = "name"
    const val NAME_VALUE = "name_value"
    const val EMAIL = "email"
    const val EMAIL_VALUE = "email_value"



    // get(), set()實現拿、放access_token功能
    var name: String?
        get() {
            return FamilyTreeApplication.INSTANCE.getSharedPreferences(NAME, 0)
                .getString(NAME_VALUE,null)
        }
        set(value) {
            FamilyTreeApplication.INSTANCE.getSharedPreferences(NAME,0).edit()
                .putString(NAME_VALUE, value).apply()
        }

    var email: String?
        get() {
            return FamilyTreeApplication.INSTANCE.getSharedPreferences(EMAIL, 0)
                .getString(EMAIL_VALUE,null)
        }
        set(value) {
            FamilyTreeApplication.INSTANCE.getSharedPreferences(EMAIL,0).edit()
                .putString(EMAIL_VALUE, value).apply()
        }
}