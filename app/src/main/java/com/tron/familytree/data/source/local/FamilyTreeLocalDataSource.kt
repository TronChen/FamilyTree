package app.appworks.school.publisher.data.source.local

import android.content.Context
import androidx.lifecycle.MutableLiveData
import app.appworks.school.publisher.data.source.FamilyTreeDataSource
import com.tron.familytree.branch.BranchViewModel
import com.tron.familytree.branch.TreeItem
import com.tron.familytree.data.*
import com.tron.familytree.data.Map
import com.tron.familytree.message.chatroom.MessageItem
import com.tron.familytree.profile.member.MemberItem

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Concrete implementation of a Publisher source as a db.
 */
class FamilyTreeLocalDataSource(val context: Context) : FamilyTreeDataSource {

    override suspend fun uploadImage(path: String): AppResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addMemberReturnUser(user: User): AppResult<User> {
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

    override suspend fun findUserByName(name: String): AppResult<User> {
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

    override suspend fun addChatroom(chatRoom: ChatRoom): AppResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getChatroom(): AppResult<List<ChatRoom>> {
        TODO("Not yet implemented")
    }

    override fun getLiveChatroom(): MutableLiveData<List<ChatRoom>> {
        TODO("Not yet implemented")
    }

    override suspend fun addMessage(chatRoom: ChatRoom, message: Message): AppResult<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getLiveMessage(chatRoom: ChatRoom): MutableLiveData<List<MessageItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMessage(chatRoom: ChatRoom): AppResult<List<MessageItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun addEvent(event: Event): AppResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getEvent(): AppResult<List<Event>> {
        TODO("Not yet implemented")
    }

    override fun getLiveEvent(): MutableLiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun addEventAttender(user: User, event: Event): AppResult<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getLiveAttender(event: Event): MutableLiveData<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAttender(event: Event): AppResult<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun findChatroom(member: String, userId: String): AppResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getEventByUserId(user: User): AppResult<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEventByFamilyId(id: String): AppResult<List<Event>> {
        TODO("Not yet implemented")
    }

    override fun getLiveEventByFamilyId(id: String): MutableLiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override fun getLiveEventByUserId(id: String): MutableLiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEventByTime(date: String): AppResult<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun addPhoto(event: Event, photo: Photo): AppResult<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getLiveAlbum(event: Event): MutableLiveData<List<Photo>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAlbum(event: Event): AppResult<List<Photo>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserLocation(): AppResult<List<Map>> {
        TODO("Not yet implemented")
    }

    override fun getLiveUserLocation(): MutableLiveData<List<Map>> {
        TODO("Not yet implemented")
    }

    override suspend fun addLocation(map: Map): AppResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllEpisode(): AppResult<List<Episode>> {
        TODO("Not yet implemented")
    }

    override suspend fun findEpisodeById(id: String): AppResult<Episode> {
        TODO("Not yet implemented")
    }

    override suspend fun addFamily(family : Family, user: User): AppResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getFamily(): AppResult<List<Family>> {
        TODO("Not yet implemented")
    }

    override fun getLiveFamily(): MutableLiveData<List<Family>> {
        TODO("Not yet implemented")
    }

    override suspend fun getFamilyMember(family: Family): AppResult<List<User>> {
        TODO("Not yet implemented")
    }

    override fun getLiveFamilyMember(family: Family): MutableLiveData<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateFamily(family: Family, user: User): AppResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun searchBranchUser(id: String,viewModel: BranchViewModel): AppResult<List<TreeItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun findFamilyById(id: String): AppResult<Family> {
        TODO("Not yet implemented")
    }

}
