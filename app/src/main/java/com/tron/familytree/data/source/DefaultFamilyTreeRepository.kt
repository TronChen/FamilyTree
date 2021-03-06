package app.appworks.school.publisher.data.source

import androidx.lifecycle.MutableLiveData
import com.tron.familytree.branch.BranchViewModel
import com.tron.familytree.branch.TreeItem
import com.tron.familytree.data.*
import com.tron.familytree.data.Map
import com.tron.familytree.message.chatroom.MessageItem
import kotlin.coroutines.suspendCoroutine


class DefaultFamilyTreeRepository(private val remoteDataSource: FamilyTreeDataSource,
                                  private val localDataSource: FamilyTreeDataSource
) : FamilyTreeRepository {

    override suspend fun uploadImage(path: String) : AppResult<String> {
        return remoteDataSource.uploadImage(path)
    }

    override suspend fun addMemberReturnUser(user: User): AppResult<User>{
        return remoteDataSource.addMemberReturnUser(user)
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

    override suspend fun findUserByName(name: String): AppResult<User>{
        return remoteDataSource.findUserByName(name)
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

    override suspend fun findChatroom(member: String, userId : String): AppResult<Boolean>{
        return remoteDataSource.findChatroom(member,userId)
    }

    override suspend fun addMessage(chatRoom: ChatRoom,message: Message): AppResult<Boolean>{
        return remoteDataSource.addMessage(chatRoom,message)
    }

    override fun getLiveMessage(chatRoom: ChatRoom): MutableLiveData<List<MessageItem>>{
        return remoteDataSource.getLiveMessage(chatRoom)
    }

    override suspend fun getMessage(chatRoom: ChatRoom): AppResult<List<MessageItem>>{
        return remoteDataSource.getMessage(chatRoom)
    }

    override suspend fun addEvent(event: Event): AppResult<Boolean>{
        return remoteDataSource.addEvent(event)
    }

    override suspend fun getEvent(): AppResult<List<Event>>{
        return remoteDataSource.getEvent()
    }

    override fun getLiveEvent(): MutableLiveData<List<Event>>{
        return remoteDataSource.getLiveEvent()
    }

    override suspend fun addEventAttender(user: User, event: Event): AppResult<Boolean>{
        return remoteDataSource.addEventAttender(user,event)
    }

    override fun getLiveAttender(event: Event): MutableLiveData<List<User>>{
        return remoteDataSource.getLiveAttender(event)
    }

    override suspend fun getAttender(event: Event): AppResult<List<User>>{
        return remoteDataSource.getAttender(event)
    }

    override suspend fun getEventByUserId(user: User): AppResult<List<Event>>{
        return remoteDataSource.getEventByUserId(user)
    }

    override suspend fun getEventByFamilyId(id: String): AppResult<List<Event>>{
        return remoteDataSource.getEventByFamilyId(id)
    }

    override fun getLiveEventByFamilyId(id: String): MutableLiveData<List<Event>>{
        return remoteDataSource.getLiveEventByFamilyId(id)
    }

    override fun getLiveEventByUserId(id: String): MutableLiveData<List<Event>>{
        return remoteDataSource.getLiveEventByUserId(id)
    }

    override suspend fun getEventByTime(date: String): AppResult<List<Event>>{
        return remoteDataSource.getEventByTime(date)
    }

    override suspend fun addPhoto(event: Event,photo: Photo): AppResult<Boolean>{
        return remoteDataSource.addPhoto(event,photo)
    }

    override fun getLiveAlbum(event: Event): MutableLiveData<List<Photo>>{
        return remoteDataSource.getLiveAlbum(event)
    }

    override suspend fun getAlbum(event: Event): AppResult<List<Photo>>{
        return remoteDataSource.getAlbum(event)
    }

    override suspend fun getUserLocation(): AppResult<List<Map>>{
        return remoteDataSource.getUserLocation()
    }

    override fun getLiveUserLocation(): MutableLiveData<List<Map>>{
        return remoteDataSource.getLiveUserLocation()
    }

    override suspend fun addLocation(map: Map): AppResult<Boolean>{
        return remoteDataSource.addLocation(map)
    }

    override suspend fun getAllEpisode(): AppResult<List<Episode>>{
        return remoteDataSource.getAllEpisode()
    }

    override suspend fun findEpisodeById(id: String): AppResult<Episode>{
        return remoteDataSource.findEpisodeById(id)
    }

    override suspend fun addFamily(family : Family, user: User): AppResult<Boolean>{
        return remoteDataSource.addFamily(family,user)
    }

    override suspend fun getFamily(): AppResult<List<Family>>{
        return remoteDataSource.getFamily()
    }

    override fun getLiveFamily(): MutableLiveData<List<Family>>{
        return remoteDataSource.getLiveFamily()
    }

    override suspend fun getFamilyMember(family: Family): AppResult<List<User>>{
        return remoteDataSource.getFamilyMember(family)
    }

    override fun getLiveFamilyMember(family: Family): MutableLiveData<List<User>>{
        return remoteDataSource.getLiveFamilyMember(family)
    }

    override suspend fun updateFamily(family : Family, user: User): AppResult<Boolean>{
        return remoteDataSource.updateFamily(family,user)
    }

    override suspend fun searchBranchUser(id: String,viewModel: BranchViewModel): AppResult<List<TreeItem>>{
        return remoteDataSource.searchBranchUser(id,viewModel)
    }

    override suspend fun findFamilyById(id: String): AppResult<Family>{
        return remoteDataSource.findFamilyById(id)
    }

    override suspend fun updateMapFamilyId(user: User): AppResult<Boolean>{
        return remoteDataSource.updateMapFamilyId(user)
    }

    override suspend fun getEpisodeByFamilyId(familyId: String): AppResult<List<Episode>>{
        return remoteDataSource.getEpisodeByFamilyId(familyId)
    }

}
