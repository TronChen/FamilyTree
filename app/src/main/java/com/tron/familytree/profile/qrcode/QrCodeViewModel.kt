package com.tron.familytree.profile.qrcode

import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.util.UserManager

class QrCodeViewModel(
    private val repository: FamilyTreeRepository
) : ViewModel() {

    var userImage :String = ""

    init {
        userImage = UserManager.photo.toString()
    }
}