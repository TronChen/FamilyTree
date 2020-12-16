package app.appworks.school.publisher.data.source

import androidx.lifecycle.MutableLiveData
import com.tron.familytree.data.AppResult
import com.tron.familytree.data.ChatRoom
import com.tron.familytree.data.Episode
import com.tron.familytree.data.User
import com.tron.familytree.profile.member.MemberItem


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

    override suspend fun updateMemberMateId(user: User, newMember : User): AppResult<Boolean>{
        return remoteDataSource.updateMemberMateId(user , newMember)
    }

    override suspend fun findUserById(id: String): AppResult<User>{
        return remoteDataSource.findUserById(id)
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

    override suspend fun addUserEpisode(episode: Episode): AppResult<Boolean>{
        return remoteDataSource.addUserEpisode(episode)
    }

    override fun getLiveEpisode(user: User): MutableLiveData<List<Episode>>{
        return remoteDataSource.getLiveEpisode(user)
    }

    override suspend fun getEpisode(user: User): AppResult<List<Episode>>{
        return remoteDataSource.getEpisode(user)
    }

    override fun getUserLiveEpisode(): MutableLiveData<List<Episode>>{
        return remoteDataSource.getUserLiveEpisode()
    }

    override suspend fun getUserEpisode(): AppResult<List<Episode>>{
        return remoteDataSource.getUserEpisode()
    }

    override suspend fun updateChild(user: User,newMember: User): AppResult<Boolean>{
        return remoteDataSource.updateChild(user,newMember)
    }

    override suspend fun getAllFamily(): AppResult<List<User>>{
        return remoteDataSource.getAllFamily()
    }

    override suspend fun addChatroom(chatRoom: ChatRoom): AppResult<Boolean>{
        return remoteDataSource.addChatroom(chatRoom)
    }

    override suspend fun getChatroom(): AppResult<List<ChatRoom>>{
        return remoteDataSource.getChatroom()
    }

    override fun getLiveChatroom(): MutableLiveData<List<ChatRoom>>{
        return remoteDataSource.getLiveChatroom()
    }

}
