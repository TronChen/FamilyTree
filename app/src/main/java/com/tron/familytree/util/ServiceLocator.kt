package com.tron.familytree.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import app.appworks.school.publisher.data.source.DefaultFamilyTreeRepository
import app.appworks.school.publisher.data.source.FamilyTreeDataSource
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import app.appworks.school.publisher.data.source.local.FamilyTreeLocalDataSource
import com.tron.familytree.data.source.remote.FamilyTreeRemoteDataSource

object ServiceLocator {

    @Volatile
    var repository: FamilyTreeRepository? = null
        @VisibleForTesting set

    fun provideRepository(context: Context): FamilyTreeRepository {
        synchronized(this) {
            return repository
                ?: repository
                ?: createFamilyTreeRepository(context)
        }
    }

    private fun createFamilyTreeRepository(context: Context): FamilyTreeRepository {
        return DefaultFamilyTreeRepository(
            FamilyTreeRemoteDataSource,
            createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): FamilyTreeDataSource {
        return FamilyTreeLocalDataSource(context)
    }
}