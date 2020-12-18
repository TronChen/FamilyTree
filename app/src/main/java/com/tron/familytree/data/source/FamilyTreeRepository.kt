package app.appworks.school.publisher.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tron.familytree.data.*
import com.tron.familytree.message.chatroom.MessageItem
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

    suspend fun addMessage(chatRoom: ChatRoom,message: Message): AppResult<Boolean>

    fun getLiveMessage(chatRoom: ChatRoom): MutableLiveData<List<MessageItem>>

    suspend fun getMessage(chatRoom: ChatRoom): AppResult<List<MessageItem>>

    suspend fun addEvent(event: Event): AppResult<Boolean>

    suspend fun getEvent(): AppResult<List<Event>>

    fun getLiveEvent(): MutableLiveData<List<Event>>

    suspend fun addEventAttender(user: User, event: Event): AppResult<Boolean>

    fun getLiveAttender(event: Event): MutableLiveData<List<User>>

    suspend fun getAttender(event: Event): AppResult<List<User>>

    suspend fun findChatroom(member: String, userId : String): AppResult<Boolean>
}
