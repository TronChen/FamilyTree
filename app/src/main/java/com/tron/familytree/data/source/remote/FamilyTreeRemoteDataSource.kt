package com.tron.familytree.data.source.remote

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import app.appworks.school.publisher.data.source.FamilyTreeDataSource
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.tron.familytree.FamilyTreeApplication
import com.tron.familytree.R
import com.tron.familytree.data.*
import com.tron.familytree.message.chatroom.MessageItem
import com.tron.familytree.util.UserManager
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object FamilyTreeRemoteDataSource : FamilyTreeDataSource {

    private const val PATH_ARTICLES = "articles"
    private const val KEY_CREATED_TIME = "createdTime"
    private const val PATH_USER = "User"
    private const val EPISODE = "Episode"
    private const val CHATROOM = "Chatroom"
    private const val MESSAGE = "Message"
    private const val FAMILY = "Family"
    private const val EVENT = "Event"
    private const val ATTENDER = "Attender"


    override suspend fun getAttender(event: Event): AppResult<List<User>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(EVENT)
            .document(event.id)
            .collection(ATTENDER)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<User>()
                    for (document in task.result!!) {
                        Log.d("Tron",document.id + " => " + document.data)

                        val user = document.toObject(User::class.java)
                        list.add(user)
                    }
                    continuation.resume(AppResult.Success(list))
                } else {
                    task.exception?.let {

                        Log.w("Tron","[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(AppResult.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(AppResult.Fail(FamilyTreeApplication.INSTANCE.getString(R.string.you_know_nothing)))
                }
            }
    }

    override fun getLiveAttender(event: Event): MutableLiveData<List<User>> {
        val userCollection = FirebaseFirestore.getInstance().collection(EVENT)
            .document(event.id)
            .collection(ATTENDER)
        val liveData = MutableLiveData<List<User>>()

        userCollection
            .addSnapshotListener { snapshot, exception ->

                Log.i("Tron","addSnapshotListener detect")

                exception?.let {
                    Log.w("Tron","[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<User>()
                for (document in snapshot!!) {
                    Log.d("Tron",document.id + " => " + document.data)

                    val user = document.toObject(User::class.java)
                    list.add(user)
                }
                liveData.value = list
            }
        return liveData
    }


    override suspend fun addEventAttender(user: User, event: Event): AppResult<Boolean> = suspendCoroutine { continuation ->
        val eventCollection = FirebaseFirestore.getInstance().collection(EVENT)
        val userCollection = FirebaseFirestore.getInstance().collection(PATH_USER)

        eventCollection.document(event.id).collection(ATTENDER).document(user.id).set(user)
            .addOnSuccessListener {
                continuation.resume(AppResult.Success(true))
            }
            .addOnFailureListener {
                continuation.resume(AppResult.Error(it))
            }
        userCollection.document(user.id).collection(EVENT).document(event.id).set(event)
            .addOnSuccessListener {

//                continuation.resume(AppResult.Success(true))
            }
            .addOnFailureListener {
//                continuation.resume(AppResult.Error(it))
            }
    }


    override fun getLiveEvent(): MutableLiveData<List<Event>> {
        val userCollection = FirebaseFirestore.getInstance().collection(EVENT)
        val liveData = MutableLiveData<List<Event>>()

        userCollection
            .orderBy("eventTime",Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->

                Log.i("Tron","addSnapshotListener detect")

                exception?.let {
                    Log.w("Tron","[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Event>()
                for (document in snapshot!!) {
                    Log.d("Tron",document.id + " => " + document.data)

                    val event = document.toObject(Event::class.java)
                    list.add(event)
                }
                liveData.value = list
            }
        return liveData
    }

    override suspend fun getEvent(): AppResult<List<Event>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(EVENT)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Event>()
                    for (document in task.result!!) {
                        Log.d("Tron",document.id + " => " + document.data)

                        val event = document.toObject(Event::class.java)
                        list.add(event)
                    }
                    continuation.resume(AppResult.Success(list))
                } else {
                    task.exception?.let {

                        Log.w("Tron","[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(AppResult.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(AppResult.Fail(FamilyTreeApplication.INSTANCE.getString(R.string.you_know_nothing)))
                }
            }
    }

    override suspend fun addEvent(event: Event): AppResult<Boolean> = suspendCoroutine { continuation ->
        val userCollection = FirebaseFirestore.getInstance().collection(EVENT)

        event.id = userCollection.document().id
                    userCollection.document(event.id)
                        .set(event).addOnSuccessListener {
                            continuation.resume(AppResult.Success(true))
                        }
                        .addOnFailureListener {
                            continuation.resume(AppResult.Error(it))
                        }
    }

    override suspend fun getMessage(chatRoom: ChatRoom): AppResult<List<MessageItem>> = suspendCoroutine { continuation ->
        val userCollection = FirebaseFirestore.getInstance().collection(CHATROOM)
        userCollection
            .whereEqualTo("id", chatRoom.id)
            .get()
            .addOnSuccessListener {
                for (index in it) {

                    userCollection.document(index.id).collection(MESSAGE)
                        .orderBy("time",Query.Direction.ASCENDING)
                        .get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val list = mutableListOf<MessageItem>()
                                for (document in task.result!!) {
                                    Log.d("Tron", document.id + " => " + document.data)

                                    val message = document.toObject(Message::class.java)
                                    if (message.user == UserManager.email){
                                        list.add(MessageItem.Sender(message))
                                    }else{
                                        list.add(MessageItem.Receiver(message))
                                    }
                                }
                                continuation.resume(AppResult.Success(list))
                            } else {
                                task.exception?.let {

                                    Log.w(
                                        "Tron",
                                        "[${this::class.simpleName}] Error getting documents. ${it.message}"
                                    )
                                    continuation.resume(AppResult.Error(it))
                                    return@addOnCompleteListener
                                }
                                continuation.resume(
                                    AppResult.Fail(
                                        FamilyTreeApplication.INSTANCE.getString(
                                            R.string.you_know_nothing
                                        )
                                    )
                                )
                            }
                        }

                }
            }
    }

    override fun getLiveMessage(chatRoom: ChatRoom): MutableLiveData<List<MessageItem>> {
        val userCollection = FirebaseFirestore.getInstance().collection(CHATROOM)
        val liveData = MutableLiveData<List<MessageItem>>()
        userCollection
            .whereEqualTo("id",chatRoom.id)
            .get()
            .addOnSuccessListener {
                for (index in it){
                    userCollection.document(index.id).collection(MESSAGE)
                        .orderBy("time",Query.Direction.ASCENDING)
                        .addSnapshotListener { snapshot, exception ->

                            Log.i("Tron","addSnapshotListener detect")

                            exception?.let {
                                Log.w("Tron","[${this::class.simpleName}] Error getting documents. ${it.message}")
                            }

                            val list = mutableListOf<MessageItem>()
                            for (document in snapshot!!) {
                                Log.d("Tron",document.id + " => " + document.data)

                                val message = document.toObject(Message::class.java)
                                if (message.user == UserManager.email){
                                    list.add(MessageItem.Sender(message))
                                }else{
                                    list.add(MessageItem.Receiver(message))
                                }
                            }
                            liveData.value = list
                        }
                }
            }
        return liveData
    }

    override suspend fun addMessage(chatRoom: ChatRoom,message: Message): AppResult<Boolean> = suspendCoroutine { continuation ->
        val userCollection = FirebaseFirestore.getInstance().collection(CHATROOM)

        userCollection.whereEqualTo("id",chatRoom.id)
            .get()
            .addOnSuccessListener {
                for (index in it){
                    userCollection.document(index.id).collection(MESSAGE)
                        .add(message).addOnSuccessListener {
                            continuation.resume(AppResult.Success(true))
                        }
                        .addOnFailureListener {
                            continuation.resume(AppResult.Error(it))
                        }

                    userCollection.document(index.id)
                        .update("latestMessage",message).addOnSuccessListener {
//                            continuation.resume(AppResult.Success(true))
                        }
                        .addOnFailureListener {
//                            continuation.resume(AppResult.Error(it))
                        }
                }
            }
    }

    override suspend fun getChatroom(): AppResult<List<ChatRoom>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(CHATROOM)
            .whereArrayContains("attenderId",UserManager.email.toString())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<ChatRoom>()
                    for (document in task.result!!) {
                        Log.d("Tron", document.id + " => " + document.data)

                        val chatroom1 = ChatRoom()
                        val chatroom = document.toObject(ChatRoom::class.java)
                        chatroom1.id = chatroom.id
                        chatroom1.userImage = chatroom.userImage.filter { it != UserManager.photo}
                        chatroom1.attenderId = chatroom.attenderId.filter { it != UserManager.email }
                        chatroom1.attenderName = chatroom.attenderName.filter { it != UserManager.name }
                        list.add(chatroom1)
                    }
                    continuation.resume(AppResult.Success(list))
                } else {
                    task.exception?.let {

                        Log.w(
                            "Tron",
                            "[${this::class.simpleName}] Error getting documents. ${it.message}"
                        )
                        continuation.resume(AppResult.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(
                        AppResult.Fail(
                            FamilyTreeApplication.INSTANCE.getString(
                                R.string.you_know_nothing
                            )
                        )
                    )
                }
            }
    }

    override suspend fun findChatroom(member: String, userId : String): AppResult<Boolean> = suspendCoroutine { continuation ->
        val memberlist= listOf(member,userId)
        FirebaseFirestore.getInstance()
            .collection(CHATROOM)
            .whereIn("attenderId", listOf(memberlist,memberlist.reversed()))
            .get()
            .addOnSuccessListener {result ->
                if (result.isEmpty){
                    continuation.resume(AppResult.Success(true))
                }
            }
    }


//    override fun getLiveLatestMessage(): MutableLiveData<List<Message>> {
//
//        val liveData = MutableLiveData<List<Message>>()
//
//        FirebaseFirestore.getInstance()
//            .collection(CHATROOM)
//            .whereArrayContains("attenderId",UserManager.email.toString())
//            .addSnapshotListener { snapshot, exception ->
//
//                Log.i("Tron", "addSnapshotListener detect")
//
//                exception?.let {
//                    Log.w(
//                        "Tron",
//                        "[${this::class.simpleName}] Error getting documents. ${it.message}"
//                    )
//                }
//
//                val list = mutableListOf<Message>()
//                for (document in snapshot!!) {
//                    FirebaseFirestore.getInstance()
//                        .collection(CHATROOM)
//                        .document(document.id)
//                        .collection(MESSAGE)
//                        .orderBy("time", Query.Direction.DESCENDING)
//                        .limit(1)
//                        .addSnapshotListener {  snapshot, exception ->
//                            for (document in snapshot!!) {
//                                val message = document.toObject(Message::class.java)
//                                list.add(message)
//                            }
//                        }
//                }
//                liveData.value = list
//
//            }
//        return liveData
//    }


    override fun getLiveChatroom(): MutableLiveData<List<ChatRoom>> {

        val liveData = MutableLiveData<List<ChatRoom>>()

        FirebaseFirestore.getInstance()
            .collection(CHATROOM)
            .whereArrayContains("attenderId",UserManager.email.toString())
            .addSnapshotListener { snapshot, exception ->

                Log.i("Tron", "addSnapshotListener detect")

                exception?.let {
                    Log.w(
                        "Tron",
                        "[${this::class.simpleName}] Error getting documents. ${it.message}"
                    )
                }

                val list = mutableListOf<ChatRoom>()
                for (document in snapshot!!) {
                    Log.d("Tron", document.id + " => " + document.data)

                    val chatroom1 = ChatRoom()
                    val chatroom = document.toObject(ChatRoom::class.java)
                    chatroom1.id = chatroom.id
                    chatroom1.userImage = chatroom.userImage.filter { it != UserManager.photo}
                    chatroom1.attenderId = chatroom.attenderId.filter { it != UserManager.email }
                    chatroom1.attenderName = chatroom.attenderName.filter { it != UserManager.name }
                    chatroom1.latestMessage = chatroom.latestMessage
                    Log.e("chatroom1", chatroom1.toString())
                    list.add(chatroom1)
                }

                liveData.value = list

            }
        return liveData
    }


    override suspend fun addChatroom(chatRoom: ChatRoom): AppResult<Boolean> = suspendCoroutine { continuation ->
        val userCollection = FirebaseFirestore.getInstance().collection(CHATROOM)
        val document = userCollection.document()

        userCollection.whereEqualTo("attenderId",chatRoom.attenderId)
            .get()
            .addOnSuccessListener {
                for (index in it){
                    continuation.resume(AppResult.Success(true))
                }
                if (it.isEmpty){
                    chatRoom.id = document.id
                    userCollection.document(chatRoom.id)
                        .set(chatRoom)
                        .addOnSuccessListener {
                            if (it != null) {
                                continuation.resume(AppResult.Success(true))
                            }
                        }
                        .addOnFailureListener {
                            continuation.resume(AppResult.Error(it))
                        }
                }
            }
    }



    override suspend fun getAllFamily(): AppResult<List<User>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_USER)
            .whereEqualTo("familyId","Chen")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<User>()
                    for (document in task.result!!) {
                        Log.d("Tron",document.id + " => " + document.data)

                        val user = document.toObject(User::class.java)
                        list.add(user)
                    }
                    continuation.resume(AppResult.Success(list))
                } else {
                    task.exception?.let {

                        Log.w("Tron","[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(AppResult.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(AppResult.Fail(FamilyTreeApplication.INSTANCE.getString(R.string.you_know_nothing)))
                }
            }
    }

    override suspend fun getEpisode(user: User): AppResult<List<Episode>> = suspendCoroutine { continuation ->

            FirebaseFirestore.getInstance()
                .collection(PATH_USER)
                .document(user.id)
                .collection(EPISODE)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Episode>()
                        for (document in task.result!!) {
                            Log.d("Tron",document.id + " => " + document.data)

                            val episode = document.toObject(Episode::class.java)
                            list.add(episode)
                        }
                        continuation.resume(AppResult.Success(list))
                    } else {
                        task.exception?.let {

                            Log.w("Tron","[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(AppResult.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(AppResult.Fail(FamilyTreeApplication.INSTANCE.getString(R.string.you_know_nothing)))
                    }
                }

    }

    override suspend fun getUserEpisode(): AppResult<List<Episode>> = suspendCoroutine { continuation ->

        FirebaseFirestore.getInstance()
            .collection(PATH_USER)
            .document(UserManager.email.toString())
            .collection(EPISODE)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Episode>()
                    for (document in task.result!!) {
                        Log.d("Tron",document.id + " => " + document.data)

                        val episode = document.toObject(Episode::class.java)
                        list.add(episode)
                    }
                    continuation.resume(AppResult.Success(list))
                } else {
                    task.exception?.let {

                        Log.w("Tron","[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(AppResult.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(AppResult.Fail(FamilyTreeApplication.INSTANCE.getString(R.string.you_know_nothing)))
                }
            }

    }

    override fun getLiveEpisode(user: User): MutableLiveData<List<Episode>> {

        val liveData = MutableLiveData<List<Episode>>()
            FirebaseFirestore.getInstance()
                .collection(PATH_USER)
                .document(user.id)
                .collection(EPISODE)
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, exception ->

                    Log.i("Tron","addSnapshotListener detect")

                    exception?.let {
                        Log.w("Tron","[${this::class.simpleName}] Error getting documents. ${it.message}")
                    }

                    val list = mutableListOf<Episode>()
                    for (document in snapshot!!) {
                        Log.d("Tron",document.id + " => " + document.data)

                        val episode = document.toObject(Episode::class.java)
                        list.add(episode)
                    }

                    liveData.value = list

        }
        return liveData
    }

    override fun getUserLiveEpisode(): MutableLiveData<List<Episode>> {

        val liveData = MutableLiveData<List<Episode>>()
        FirebaseFirestore.getInstance()
            .collection(PATH_USER)
            .document(UserManager.email.toString())
            .collection(EPISODE)
            .orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

                Log.i("Tron","addSnapshotListener detect")

                exception?.let {
                    Log.w("Tron","[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Episode>()
                for (document in snapshot!!) {
                    Log.d("Tron",document.id + " => " + document.data)

                    val episode = document.toObject(Episode::class.java)
                    list.add(episode)
                }

                liveData.value = list

            }
        return liveData
    }



    override suspend fun addUserEpisode(episode: Episode): AppResult<Boolean> = suspendCoroutine { continuation ->
        val userCollection = FirebaseFirestore.getInstance().collection(PATH_USER)

        userCollection.whereEqualTo("id",UserManager.email)
            .get()
            .addOnSuccessListener {
                for (index in it){
                    userCollection.document(index.id).collection(EPISODE).document(episode.title)
                        .set(episode)
                        .addOnSuccessListener {
                            if (it != null) {
                                continuation.resume(AppResult.Success(true))
                            }
                        }
                        .addOnFailureListener {
                            continuation.resume(AppResult.Error(it))
                        }
                }
            }
    }

    override suspend fun findUserById(id: String): AppResult<User> = suspendCoroutine { continuation ->
        val userCollection = FirebaseFirestore.getInstance().collection(PATH_USER)
        userCollection
            .whereEqualTo("id", id)
            .get()
            .addOnSuccessListener {
                if (it != null){
                    for (index in it) {
                        val user = index.toObject(User::class.java)
                        continuation.resume(AppResult.Success(user))
                    }
                }
            }
            .addOnFailureListener {
                continuation.resume(AppResult.Error(it))
            }
    }

    override suspend fun findUser(name: String): AppResult<User> = suspendCoroutine { continuation ->
        val userCollection = FirebaseFirestore.getInstance().collection(PATH_USER)
        userCollection
            .whereEqualTo("name", name)
            .get()
            .addOnSuccessListener {
                if (it != null){
                    for (index in it) {
                        val user = index.toObject(User::class.java)
                        continuation.resume(AppResult.Success(user))
                    }
                }
            }
            .addOnFailureListener {
                continuation.resume(AppResult.Error(it))
            }
    }

    override suspend fun addUserToFirebase(user: User): AppResult<Boolean> = suspendCoroutine { continuation ->
            val userCollection = FirebaseFirestore.getInstance().collection(PATH_USER)
            val document = userCollection.document(user.id)
        userCollection.whereEqualTo("id",user.id)
            .get()
            .addOnSuccessListener {
                for (index in it){
                    continuation.resume(AppResult.Success(true))
                }
                if (it.isEmpty){
            document
                .set(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("addUserToFirebase", "addUserToFirebase: $user")
                        continuation.resume(AppResult.Success(true))
                    } else {
                        task.exception?.let {
                            Log.d(
                                "add_user_exception",
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(AppResult.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(AppResult.Fail(FamilyTreeApplication.INSTANCE.getString(R.string.you_know_nothing)))
                    }
                }
                }
            }

        }


    override suspend fun uploadImage(path: String): AppResult<String> = suspendCoroutine { continuation ->
        val file = Uri.fromFile(File(path))
        val fileName = file.lastPathSegment
        var userImage = MutableLiveData<String>()
        val mStorageRef = FirebaseStorage.getInstance().reference
        val metadata = StorageMetadata.Builder()
            .setContentDisposition("userImage")
            .setContentType("image/jpg")
            .build()
        val riversRef = mStorageRef.child(file.lastPathSegment ?: "")
        val uploadTask = riversRef.putFile(file, metadata)
        uploadTask.addOnSuccessListener {
            if (fileName != null) {
                FirebaseStorage.getInstance().reference.child(fileName)
                    .downloadUrl
                    .addOnSuccessListener {
                                userImage.value = it.toString()
                                Log.e("URL", it.toString())
                continuation.resume(AppResult.Success(it.toString()))
                    }
            }
        }
            .addOnFailureListener{
                    continuation.resume(AppResult.Error(it))
                }
    }


    override suspend fun addMember(user: User): AppResult<Boolean> = suspendCoroutine { continuation ->
        val User = FirebaseFirestore.getInstance().collection(PATH_USER)
        val document = User.document()
        user.id = document.id
        document
            .set(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    continuation.resume(AppResult.Success(true))
                } else {
                    task.exception?.let {
                        continuation.resume(AppResult.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(AppResult.Fail(FamilyTreeApplication.INSTANCE.getString(R.string.you_know_nothing)))
                }
            }
    }

    override suspend fun updateChild(user: User,newMember: User): AppResult<Boolean> = suspendCoroutine { continuation ->
        val User = FirebaseFirestore.getInstance().collection(PATH_USER)
        var path: String = ""

        User.whereEqualTo("name", newMember.name)
            .get()
            .addOnSuccessListener {
                for (index in it) {
                    path = index.id
                    Log.e("FindUser", index.id)
                    //找到原先user
                    val document = User.document(path)

                    document
                        .update("fatherId", user.fatherId,
                            "motherId",user.motherId
                        )
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                continuation.resume(AppResult.Success(true))
                            } else {
                                task.exception?.let {
                                    continuation.resume(AppResult.Error(it))
                                    return@addOnCompleteListener
                                }
                                continuation.resume(AppResult.Fail(FamilyTreeApplication.INSTANCE.getString(R.string.you_know_nothing)))
                            }
                        }
                }
            }
    }

    override suspend fun updateMember(user: User): AppResult<Boolean> = suspendCoroutine { continuation ->
        val User = FirebaseFirestore.getInstance().collection(PATH_USER)
        var path: String = ""

        User.whereEqualTo("id", user.id)
            .get()
            .addOnSuccessListener {
                for (index in it) {
                    path = index.id
                    Log.e("FindUser", index.id)
                    //找到原先user
                    val document = User.document(path)

                    document
                        .update("name", user.name,
                            "birth",user.birth,
                            "gender",user.gender,
                            "userImage", user.userImage,
                            "birthLocation", user.birthLocation,
                            "deathDate", user.deathDate
                        )
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                continuation.resume(AppResult.Success(true))
                            } else {
                                task.exception?.let {
                                    continuation.resume(AppResult.Error(it))
                                    return@addOnCompleteListener
                                }
                                continuation.resume(AppResult.Fail(FamilyTreeApplication.INSTANCE.getString(R.string.you_know_nothing)))
                            }
                        }
                }
            }
    }


    override suspend fun updateMemberMateId(user: User, newMember : User): AppResult<Boolean> = suspendCoroutine { continuation ->
        val User = FirebaseFirestore.getInstance().collection(PATH_USER)
        var path: String = ""

        User.whereEqualTo("name",user.mateId)
            .get()
            .addOnSuccessListener {
                for (index in it) {
                    path = index.id
                    Log.e("FindMate", index.id)
                    //找到原先user
                    val document = User.document(path)

                    document
                        .update("mateId",newMember.name)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                continuation.resume(AppResult.Success(true))
                            } else {
                                task.exception?.let {
                                    continuation.resume(AppResult.Error(it))
                                    return@addOnCompleteListener
                                }
                                continuation.resume(AppResult.Fail(FamilyTreeApplication.INSTANCE.getString(R.string.you_know_nothing)))
                            }
                        }
                }
            }
    }


    override suspend fun updateMemberFatherId(user: User, newMember : User): AppResult<Boolean> = suspendCoroutine { continuation ->
        val collectionReference = FirebaseFirestore.getInstance().collection(PATH_USER)
        var path: String = ""
        val originUser = MutableLiveData<User>()
        collectionReference.whereEqualTo("name",user.fatherId)
            .get()
            .addOnSuccessListener {
                for (index in it) {
                    //找到原先user
                    //確認原先的user有沒有媽媽
                    originUser.value = index.toObject(User::class.java)

                    path = index.id
                    Log.e("FindFather", index.id)
                    val document = collectionReference.document(path)
                        //update找到的user
                    document
                        .update("fatherId",newMember.name)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                continuation.resume(AppResult.Success(true))
                            } else {
                                task.exception?.let {
                                    continuation.resume(AppResult.Error(it))
                                    return@addOnCompleteListener
                                }
                                continuation.resume(AppResult.Fail(FamilyTreeApplication.INSTANCE.getString(R.string.you_know_nothing)))
                            }
                        }
                    if (originUser.value!!.motherId != null){
                        collectionReference.whereEqualTo("name",newMember.name)
                            .get().addOnSuccessListener {task ->
                                for (fatherIndex in task){
                                    val fatherPath = fatherIndex.id
                                     collectionReference.document(fatherPath)
                                         .update("mateId",originUser.value!!.motherId)
                                         .addOnSuccessListener {
                                             Log.e("updateComplete", it.toString())
                                         }

                                }
                            }
                        collectionReference.whereEqualTo("name",originUser.value!!.motherId)
                            .get().addOnSuccessListener {task ->
                                for (fatherIndex in task){
                                    val fatherPath = fatherIndex.id
                                    collectionReference.document(fatherPath)
                                        .update("mateId",originUser.value!!.motherId)
                                        .addOnSuccessListener {
                                            Log.e("updateComplete", it.toString())
                                        }

                                }
                            }
                    }

                     }
            }
    }

    override suspend fun updateMemberMotherId(user: User, newMember : User): AppResult<Boolean> = suspendCoroutine { continuation ->
        val User = FirebaseFirestore.getInstance().collection(PATH_USER)
        var path: String = ""
        val originUser = MutableLiveData<User>()
        User.whereEqualTo("name",user.motherId)
            .get()
            .addOnSuccessListener {
                for (index in it) {
                    path = index.id
                    Log.e("FindMother", index.id)
                    originUser.value = index.toObject(com.tron.familytree.data.User::class.java)
                    //找到原先user
                    val document = User.document(path)

                    document
                        .update("motherId",newMember.name)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                continuation.resume(AppResult.Success(true))
                            } else {
                                task.exception?.let {
                                    continuation.resume(AppResult.Error(it))
                                    return@addOnCompleteListener
                                }
                                continuation.resume(AppResult.Fail(FamilyTreeApplication.INSTANCE.getString(R.string.you_know_nothing)))
                            }
                        }
                    if (originUser.value!!.fatherId != null){
                        User.whereEqualTo("name",newMember.name)
                            .get().addOnSuccessListener {task ->
                                for (fatherIndex in task){
                                    val fatherPath = fatherIndex.id
                                    User.document(fatherPath)
                                        .update("mateId",originUser.value!!.fatherId)
                                        .addOnSuccessListener {
//                                            Log.e("updateComplete", it.toString())
                                        }

                                }
                            }
                        User.whereEqualTo("name",originUser.value!!.motherId)
                            .get().addOnSuccessListener {task ->
                                for (fatherIndex in task){
                                    val fatherPath = fatherIndex.id
                                    User.document(fatherPath)
                                        .update("mateId",originUser.value!!.motherId)
                                        .addOnSuccessListener {
                                            Log.e("updateComplete", it.toString())
                                        }

                                }
                            }
                    }
                }
            }
    }


    override suspend fun getArticles(): AppResult<List<User>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_ARTICLES)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<User>()
                    for (document in task.result!!) {
//                        Logger.d(document.id + " => " + document.data)

                        val article = document.toObject(User::class.java)
                        list.add(article)
                    }
                    continuation.resume(AppResult.Success(list))
                } else {
                    task.exception?.let {

//                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(AppResult.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(AppResult.Fail(FamilyTreeApplication.INSTANCE.getString(R.string.you_know_nothing)))
                }
            }
    }

    override fun getLiveArticles(): MutableLiveData<List<User>> {

        val liveData = MutableLiveData<List<User>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_ARTICLES)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

//                Logger.i("addSnapshotListener detect")

                exception?.let {
//                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<User>()
                for (document in snapshot!!) {
//                    Logger.d(document.id + " => " + document.data)

                    val article = document.toObject(User::class.java)
                    list.add(article)
                }

                liveData.value = list
            }
        return liveData
    }

}
