package app.appworks.school.publisher.data.source

import androidx.lifecycle.MutableLiveData
import com.tron.familytree.data.*
import com.tron.familytree.data.Map
import com.tron.familytree.message.chatroom.MessageItem


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

    override suspend fun getEventByUserId(id: String): AppResult<List<Event>>{
        return remoteDataSource.getEventByUserId(id)
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

}
