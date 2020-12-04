package app.appworks.school.publisher.data.source

import androidx.lifecycle.MutableLiveData
import com.tron.familytree.data.AppResult
import com.tron.familytree.data.User


class DefaultFamilyTreeRepository(private val remoteDataSource: FamilyTreeDataSource,
                                  private val localDataSource: FamilyTreeDataSource
) : FamilyTreeRepository {

//    override suspend fun loginMockData(id: String): AppResult<User> {
//        return localDataSource.login(id)
//    }

    override suspend fun getArticles(): AppResult<List<User>> {
        return remoteDataSource.getArticles()
    }

    override fun getLiveArticles(): MutableLiveData<List<User>> {
        return remoteDataSource.getLiveArticles()
    }
}
