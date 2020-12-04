package com.tron.familytree

import android.app.Application
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.tron.familytree.util.ServiceLocator
import kotlin.properties.Delegates

class FamilyTreeApplication : Application() {

    // Depends on the flavor,
    val repository: FamilyTreeRepository
        get() = ServiceLocator.provideRepository(this)

    companion object {
        var INSTANCE: FamilyTreeApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    fun isLiveDataDesign() = true
}
