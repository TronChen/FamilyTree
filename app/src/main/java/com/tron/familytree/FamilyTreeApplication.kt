package com.tron.familytree

import android.app.Application
import com.tron.familytree.data.source.FamilyTreeRepository
import com.tron.familytree.util.ServiceLocator
import kotlin.properties.Delegates

class FamilyTreeApplication : Application(){

        // Depends on the flavor,
        val familyTreeRepository: FamilyTreeRepository
            get() = ServiceLocator.provideTasksRepository(this)

        companion object {
            var instance: FamilyTreeApplication by Delegates.notNull()
        }

        override fun onCreate() {
            super.onCreate()
            instance = this
        }
}
