package app.appworks.school.publisher.data.source.local

import android.content.Context
import androidx.lifecycle.MutableLiveData
import app.appworks.school.publisher.data.source.FamilyTreeDataSource
import com.tron.familytree.data.AppResult
import com.tron.familytree.data.User

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Concrete implementation of a Publisher source as a db.
 */
class FamilyTreeLocalDataSource(val context: Context) : FamilyTreeDataSource {

    override suspend fun getArticles(): AppResult<List<User>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLiveArticles(): MutableLiveData<List<User>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun uploadImage(path: String): AppResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addMember(user: User): AppResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMemberMotherId(user: User, newMember: User): AppResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMemberFatherId(user: User, newMember: User): AppResult<Boolean> {
        TODO("Not yet implemented")
    }

}
