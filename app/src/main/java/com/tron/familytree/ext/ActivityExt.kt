package com.tron.familytree.ext

import android.app.Activity
import android.view.Gravity
import android.widget.Toast
import com.tron.familytree.FamilyTreeApplication
import com.tron.familytree.factory.ViewModelFactory

fun Activity.getVmFactory(): ViewModelFactory {
    val repository = (applicationContext as FamilyTreeApplication).repository
    return ViewModelFactory(repository)
}

fun Activity?.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.CENTER, 0, 0)
        show()
    }
}