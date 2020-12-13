package com.tron.familytree.util

import com.tron.familytree.FamilyTreeApplication

object UserManager {

    const val SHARED_PREF_KEY = "userInfo"
    const val ACCESS_TOKEN_KEY = "access_token"


    // get(), set()實現拿、放access_token功能
    var email: String?
        get() {
            return FamilyTreeApplication.INSTANCE.getSharedPreferences(SHARED_PREF_KEY, 0)
                .getString(ACCESS_TOKEN_KEY,null)
        }
        set(value) {
            FamilyTreeApplication.INSTANCE.getSharedPreferences(SHARED_PREF_KEY,0).edit()
                .putString(ACCESS_TOKEN_KEY, value).apply()
        }
}