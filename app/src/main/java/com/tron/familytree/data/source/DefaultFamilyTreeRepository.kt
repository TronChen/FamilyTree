package app.appworks.school.publisher.data.source

import androidx.lifecycle.MutableLiveData
import com.tron.familytree.data.AppResult
import com.tron.familytree.data.User


class DefaultFamilyTreeRepository(private val remoteDataSource: FamilyTreeDataSource,
                                  private val localDataSource: FamilyTreeDataSource
) : FamilyTreeRepository {

    override suspend fun getArticles(): AppResult<List<User>> {
        return remoteDataSource.getArticles()
    }

    override fun getLiveArticles(): MutableLiveData<List<User>> {
        return remoteDataSource.getLiveArticles()
    }

    override suspend fun uploadImage(path: String) : AppResult<String> {
        return remoteDataSource.uploadImage(path)
    }

    override suspend fun addMember(user: User): AppResult<Boolean>{
        return remoteDataSource.addMember(user)
    }

    override suspend fun updateMemberMotherId(user: User, newMember : User): AppResult<Boolean>{
        return remoteDataSource.updateMemberMotherId(user , newMember)
    }

    override suspend fun updateMemberFatherId(user: User, newMember : User): AppResult<Boolean>{
        return remoteDataSource.updateMemberFatherId(user , newMember)
    }

    override suspend fun addUserToFirebase(user: User): AppResult<Boolean>{
        return remoteDataSource.addUserToFirebase(user)
    }

    override suspend fun findUser(name: String): AppResult<User>{
        return remoteDataSource.findUser(name)
    }

    override suspend fun updateMember(user: User): AppResult<Boolean>{
        return remoteDataSource.updateMember(user)
    }
}
