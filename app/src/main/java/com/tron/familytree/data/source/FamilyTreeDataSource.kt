package app.appworks.school.publisher.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tron.familytree.branch.BranchViewModel
import com.tron.familytree.branch.TreeItem
import com.tron.familytree.data.*
import com.tron.familytree.data.Map
import com.tron.familytree.message.chatroom.MessageItem
import com.tron.familytree.profile.member.MemberItem
import kotlin.coroutines.Continuation


interface FamilyTreeDataSource {

    suspend fun uploadImage(path: String) : AppResult<String>

    suspend fun addMemberReturnUser(user: User) : AppResult<User>

    suspend fun addMember(user: User): AppResult<Boolean>

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

    suspend fun findUserByName(name: String): AppResult<User>

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

    suspend fun getEventByUserId(user: User): AppResult<List<Event>>

    suspend fun getEventByFamilyId(user: User): AppResult<List<Event>>

    fun getLiveEventByFamilyId(user: User): MutableLiveData<List<Event>>

    fun getLiveEventByUserId(id: String): MutableLiveData<List<Event>>

    suspend fun getEventByTime(date: String): AppResult<List<Event>>

    suspend fun addPhoto(event: Event,photo: Photo): AppResult<Boolean>

    fun getLiveAlbum(event: Event): MutableLiveData<List<Photo>>

    suspend fun getAlbum(event: Event): AppResult<List<Photo>>

    suspend fun getUserLocation(): AppResult<List<Map>>

    fun getLiveUserLocation(): MutableLiveData<List<Map>>

    suspend fun addLocation(map: Map): AppResult<Boolean>

    suspend fun getAllEpisode(): AppResult<List<Episode>>

    suspend fun findEpisodeById(id: String): AppResult<Episode>

    suspend fun addFamily(family : Family, user: User): AppResult<Boolean>

    suspend fun getFamily(): AppResult<List<Family>>

    fun getLiveFamily(): MutableLiveData<List<Family>>

    suspend fun getFamilyMember(family: Family): AppResult<List<User>>

    fun getLiveFamilyMember(family: Family): MutableLiveData<List<User>>

    suspend fun updateFamily(family : Family, user: User): AppResult<Boolean>

    suspend fun searchBranchUser(id: String,viewModel: BranchViewModel): AppResult<List<TreeItem>>

    suspend fun findFamilyById(id: String): AppResult<Family>

}
