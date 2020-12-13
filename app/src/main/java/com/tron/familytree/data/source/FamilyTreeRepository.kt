package app.appworks.school.publisher.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tron.familytree.data.AppResult
import com.tron.familytree.data.User

interface FamilyTreeRepository {

    suspend fun getArticles(): AppResult<List<User>>

    fun getLiveArticles(): MutableLiveData<List<User>>

    suspend fun uploadImage(path: String) : AppResult<String>

    suspend fun addMember(user: User) : AppResult<Boolean>

    suspend fun updateMemberMotherId(user: User, newMember : User): AppResult<Boolean>

    suspend fun updateMemberFatherId(user: User, newMember : User): AppResult<Boolean>

    suspend fun addUserToFirebase(user: User): AppResult<Boolean>
}
