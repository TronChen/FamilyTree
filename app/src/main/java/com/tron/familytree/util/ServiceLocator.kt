package com.tron.familytree.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.tron.familytree.FamilyTreeApplication
import com.tron.familytree.data.source.DefaultFamilyTreeRepository
import com.tron.familytree.data.source.FamilyTreeDataSource
import com.tron.familytree.data.source.FamilyTreeRepository
import com.tron.familytree.data.source.local.FamilyTreeLocalDataSource
import com.tron.familytree.data.source.remote.FamilyTreeRemoteDataSource

object ServiceLocator {
    @Volatile
    var familyTreeRepository: FamilyTreeRepository? = null
        @VisibleForTesting set

    fun provideTasksRepository(context: Context): FamilyTreeRepository {
        synchronized(this) {
            return familyTreeRepository
                ?: this.familyTreeRepository!!
                ?: createStylishRepository(context)
        }
    }

    private fun createStylishRepository(context: Context): FamilyTreeRepository {
        return DefaultFamilyTreeRepository(
            FamilyTreeRemoteDataSource,
            createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): FamilyTreeDataSource {
        return FamilyTreeLocalDataSource(context)
    }
}