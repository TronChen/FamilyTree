package app.appworks.school.publisher.data.source.local

import android.content.Context
import androidx.lifecycle.MutableLiveData
import app.appworks.school.publisher.data.source.FamilyTreeDataSource
import com.tron.familytree.data.AppResult
import com.tron.familytree.data.Episode
import com.tron.familytree.data.User
import com.tron.familytree.profile.member.MemberItem

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

    override suspend fun updateMemberMateId(user: User, newMember: User): AppResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun addUserToFirebase(user: User): AppResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun findUser(name: String): AppResult<User> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMember(user: User): AppResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun addUserEpisode(episode: Episode): AppResult<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getLiveEpisode(user: User): MutableLiveData<List<Episode>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEpisode(user: User): AppResult<List<Episode>> {
        TODO("Not yet implemented")
    }

    override suspend fun findUserById(id: String): AppResult<User> {
        TODO("Not yet implemented")
    }

    override suspend fun updateChild(user: User, newMember: User): AppResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllFamily(): AppResult<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserEpisode(): AppResult<List<Episode>> {
        TODO("Not yet implemented")
    }

    override fun getUserLiveEpisode(): MutableLiveData<List<Episode>> {
        TODO("Not yet implemented")
    }
}
