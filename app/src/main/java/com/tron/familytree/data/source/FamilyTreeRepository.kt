package app.appworks.school.publisher.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tron.familytree.data.AppResult
import com.tron.familytree.data.ChatRoom
import com.tron.familytree.data.Episode
import com.tron.familytree.data.User
import com.tron.familytree.profile.member.MemberItem

interface FamilyTreeRepository {

    suspend fun getArticles(): AppResult<List<User>>

    fun getLiveArticles(): MutableLiveData<List<User>>

    suspend fun uploadImage(path: String) : AppResult<String>

    suspend fun addMember(user: User) : AppResult<Boolean>

    suspend fun updateMemberMotherId(user: User, newMember : User): AppResult<Boolean>

    suspend fun updateMemberFatherId(user: User, newMember : User): AppResult<Boolean>

    suspend fun updateMemberMateId(user: User, newMember : User): AppResult<Boolean>

    suspend fun addUserToFirebase(user: User): AppResult<Boolean>

    suspend fun findUser(name: String): AppResult<User>

    suspend fun updateMember(user: User): AppResult<Boolean>

    suspend fun addUserEpisode(episode: Episode): AppResult<Boolean>

    fun getLiveEpisode(user: User): MutableLiveData<List<Episode>>

    suspend fun getEpisode(user: User): AppResult<List<Episode>>

    suspend fun findUserById(id: String): AppResult<User>

    suspend fun updateChild(user: User,newMember: User): AppResult<Boolean>

    suspend fun getAllFamily(): AppResult<List<User>>

    suspend fun getUserEpisode(): AppResult<List<Episode>>

    fun getUserLiveEpisode(): MutableLiveData<List<Episode>>

    suspend fun addChatroom(chatRoom: ChatRoom): AppResult<Boolean>

    suspend fun getChatroom(): AppResult<List<ChatRoom>>

    fun getLiveChatroom(): MutableLiveData<List<ChatRoom>>
}
