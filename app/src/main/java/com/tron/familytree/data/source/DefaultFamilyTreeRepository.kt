package com.tron.familytree.data.source

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultFamilyTreeRepository (private val stylishRemoteDataSource: FamilyTreeDataSource,
private val stylishLocalDataSource: FamilyTreeDataSource,
private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FamilyTreeRepository {

}