package com.tron.familytree.data.source.remote

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import app.appworks.school.publisher.data.source.FamilyTreeDataSource
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.tron.familytree.FamilyTreeApplication
import com.tron.familytree.R
import com.tron.familytree.branch.BranchViewModel
import com.tron.familytree.branch.TreeItem
import com.tron.familytree.data.*
import com.tron.familytree.data.Map
import com.tron.familytree.message.chatroom.MessageItem
import com.tron.familytree.util.UserManager
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object FamilyTreeRemoteDataSource : FamilyTreeDataSource {

    private const val PATH_USER = "User"
    private const val EPISODE = "Episode"
    private const val CHATROOM = "Chatroom"
    private const val MESSAGE = "Message"
    private const val FAMILY = "Family"
    private const val EVENT = "Event"
    private const val ATTENDER = "Attender"
    private const val ALBUM = "Album"
    private const val MAP = "Map"


    override suspend fun searchBranchUser(id: String,viewModel: BranchViewModel): AppResult<List<TreeItem>> = suspendCoroutine{ continuation ->
        val db = Firebase.firestore

        val user = MutableLiveData<User>()

        val mateId = MutableLiveData<User>()
        val fatherId = MutableLiveData<User>()
        val motherId = MutableLiveData<User>()
        val mateFatherId = MutableLiveData<User>()
        val mateMotherId = MutableLiveData<User>()

        val TreeList = MutableLiveData<List<TreeItem>>()

        val children = mutableListOf<TreeItem>()
        val parents = mutableListOf<TreeItem>()
        val meAndMate = mutableListOf<TreeItem>()
        val onlyMate = mutableListOf<TreeItem>()

        val treeFinalList = mutableListOf<TreeItem>()

        fun getBranchView() {
            viewModel.children.value = children.size
            //Parent
            for ((index, parent) in parents.withIndex()) {

                if (parents.size == 2){
//                    (parent as TreeItem.Parent).user.spanSize = 1
                    treeFinalList.add(parent)
                }

                if (parents.size == 4) {

                    if (children.size < 4 || children.size == 4) {
//                        (parent as TreeItem.Parent).user.spanSize = 1
                        if (index == 2) {
                            treeFinalList.add(
                                TreeItem.Empty(-1)
                            )
                        }
                        treeFinalList.add(parent)
                    }

                    if (children.size > 4 ) {
                        when(index){
                            0,1 -> (parent as TreeItem.Parent).user.spanSize = (children.size+1) / 2 / 2
                            2 -> {
                                if (mateId.value?.fatherId != null) {
                                    (parent as TreeItem.Parent).user.spanSize =
                                        (children.size+1) / 2 / 2
                                }else{
                                    (parent as TreeItem.ParentAdd).user.spanSize =
                                        (children.size+1) / 2 / 2
                                }
                            }
                            3 ->{
                                if (mateId.value?.motherId != null) {
                                    (parent as TreeItem.Parent).user.spanSize =
                                        (children.size+1) / 2 / 2
                                }else{
                                    (parent as TreeItem.ParentAdd).user.spanSize =
                                        (children.size+1) / 2 / 2
                                }
                            }
                        }

                        if ((children.size+1) % 2 == 1 && parents.size > 2) {
                            if (parents.size % (index + 1) == 1) {
                                treeFinalList.add(
                                    TreeItem.Empty(-1)
                                )
                            }
                        }

                        if (index % 2 == 1) {
                            if ((children.size+1) % 4 == 2) {
                                treeFinalList.add(
                                    TreeItem.EmptyLineBot(-1)
                                )
                            }
                            if ((children.size+1) % 4 == 3) {
                                treeFinalList.add(
                                    TreeItem.EmptyLineBot(-1)
                                )
                            }
                        }

                        treeFinalList.add(parent)

                    }
                }
            }
            if (parents.size == 2){
                treeFinalList.add(TreeItem.Empty(-1))
                treeFinalList.add(TreeItem.Empty(-1))
            }


            //MeAndMate
            // 有配偶
            if (mateId.value != null) {
                for ((index, self) in meAndMate.withIndex()) {
                    if (children.size < 4 || children.size == 4) {
                        (self as TreeItem.Mate).user.spanSize = 2
                        if (index == 1) {
                            treeFinalList.add(
                                TreeItem.Empty(-1)
                            )
                        }
                        treeFinalList.add(self)
                    }

                    if (children.size > 4 ) {
                        (self as TreeItem.Mate).user.spanSize = (children.size+1) / 2

                        if ((children.size+1) % 2 == 1 && meAndMate.size > 2) {
                            if (children.size % (index + 1) == 1) {
                                treeFinalList.add(
                                    TreeItem.Empty(-1)
                                )
                            }
                        }
                        if ((children.size+1) % 2 == 1 && meAndMate.size == 2) {
                            if (index == 1) {
                                treeFinalList.add(
                                    TreeItem.Empty(-1)
                                )
                            }
                        }

                        treeFinalList.add(self)
                    }
                }
            }
            //沒配偶
            if (mateId.value == null){
                for (index in meAndMate){
                    (index as TreeItem.Mate).user.spanSize = 2
                    treeFinalList.add(index)
                }
                treeFinalList.add(
                    TreeItem.Empty(-1)
                )
                for (index in onlyMate){
                    (index as TreeItem.MateAdd).user.spanSize = 2
                    treeFinalList.add(index)
                }
                treeFinalList.add(
                    TreeItem.Empty(-1)
                )
                treeFinalList.add(
                    TreeItem.EmptyLine(-1)
                )
                treeFinalList.add(
                    TreeItem.EmptyLine(-1)
                )
                treeFinalList.add(
                    TreeItem.EmptyLine(-1)
                )
                treeFinalList.add(
                    TreeItem.Empty(-1)
                )
            }


            //Children
            Log.e("children",children.size.toString())
            if (user.value?.mateId != null) {
                if (children.isEmpty()) {
                    treeFinalList.add(
                        TreeItem.Empty(-1)
                    )
                    treeFinalList.add(
                        TreeItem.EmptyLine(-1)
                    )
                    if (user.value?.gender == "male") {
                        treeFinalList.add(
                            TreeItem.ChildrenAdd(
                                User(
                                    name = "No child",
                                    fatherId = user.value?.id,
                                    motherId = mateId.value?.id,
                                    gender = user.value?.gender
                                ), 1
                            )
                        )
                    }
                    if (user.value?.gender == "female") {
                        treeFinalList.add(
                            TreeItem.ChildrenAdd(
                                User(
                                    name = "No child",
                                    motherId = user.value?.id,
                                    fatherId = mateId.value?.id,
                                    gender = user.value?.gender
                                ), 1
                            )
                        )
                    }
                    treeFinalList.add(
                        TreeItem.EmptyLine(-1)
                    )
                }
            }
            for ((index, child) in children.withIndex()) {

                if (children.size == 1) {
                    when (index) {
                        0 -> treeFinalList.add(
                            TreeItem.Empty(-1)
                        )
                    }
                    treeFinalList.add(child)
                    treeFinalList.add(
                        TreeItem.EmptyLine(-1)
                    )
                    if (user.value?.gender == "male"){
                        treeFinalList.add(
                            TreeItem.ChildrenAdd(User(name = "No child", fatherId = user.value?.id , motherId = mateId.value?.id , gender = user.value?.gender), 1)
                        )
                    }
                    if (user.value?.gender == "female"){
                        treeFinalList.add(
                            TreeItem.ChildrenAdd(User(name = "No child", motherId = user.value?.id , fatherId = mateId.value?.id , gender = user.value?.gender), 1)
                        )
                    }
                }
            }
            if (children.size == 2){
                treeFinalList.add(TreeItem.Empty(-1))
                for (child in children){
                    treeFinalList.add(child)
                }
                if (user.value?.gender == "male"){
                    treeFinalList.add(
                        TreeItem.ChildrenAdd(User(name = "No child", fatherId = user.value?.id , motherId = mateId.value?.id , gender = user.value?.gender), 1)
                    )
                }
                if (user.value?.gender == "female"){
                    treeFinalList.add(
                        TreeItem.ChildrenAdd(User(name = "No child", motherId = user.value?.id , fatherId = mateId.value?.id , gender = user.value?.gender), 1)
                    )
                }

            }

            if (children.size == 3) {
                for (child in children) {
                    treeFinalList.add(child)
                }
                if (user.value?.gender == "male"){
                    treeFinalList.add(
                        TreeItem.ChildrenAdd(User(name = "No child", fatherId = user.value?.id , motherId = mateId.value?.id , gender = user.value?.gender), 3)
                    )
                }
                if (user.value?.gender == "female"){
                    treeFinalList.add(
                        TreeItem.ChildrenAdd(User(name = "No child", motherId = user.value?.id , fatherId = mateId.value?.id , gender = user.value?.gender), 3)
                    )
                }
            }

            if (children.size > 4 || children.size == 4) {
                for (child in children) {
                    treeFinalList.add(child)
                }
                if (user.value?.gender == "male"){
                    treeFinalList.add(
                        TreeItem.ChildrenAdd(User(name = "No child", fatherId = user.value?.id , motherId = mateId.value?.id , gender = user.value?.gender), 10000)
                    )
                }
                if (user.value?.gender == "female"){
                    treeFinalList.add(
                        TreeItem.ChildrenAdd(User(name = "No child", motherId = user.value?.id , fatherId = mateId.value?.id , gender = user.value?.gender), 10000)
                    )
                }
            }

            Log.e("treeFinalList", treeFinalList.toString())
            TreeList.value = treeFinalList

            continuation.resume(AppResult.Success(TreeList.value!!))
        }


        fun getUserChildren(){
                if (user.value?.gender == "male") {
                    db.collection("User")
                        .whereEqualTo("familyId",user.value?.familyId)
                        .whereEqualTo("fatherId", user.value?.id)
                        .get()
                        .addOnSuccessListener { result ->
                            for ((index, document) in result.withIndex()) {
                                val child = document.toObject(User::class.java)
                                if (result.size() > 2) {
                                    children.add(
                                        TreeItem.Children(child, index)
                                    )
                                } else {
                                    children.add(
                                        TreeItem.ChildrenMid(child, index)
                                    )
                                }
                            }
                            if (result.isEmpty) {
//                                children.add(
//                                    TreeItem.ChildrenAdd(
//                                        User(
//                                            name = "No child",
//                                            fatherId = user.value?.id, motherId = mateId.value?.id , gender = user.value?.gender
//                                        ), 0
//                                    )
//                                )
                            }
                            Log.e("treeChildrenList", children.toString())
                        }
                }

                if (user.value?.gender == "female") {
                    db.collection("User")
                        .whereEqualTo("familyId",user.value?.familyId)
                        .whereEqualTo("motherId", user.value?.id)
                        .get()
                        .addOnSuccessListener { result ->
                            for ((index, document) in result.withIndex()) {
                                val child = document.toObject(User::class.java)
                                if (result.size() > 2) {
                                    children.add(
                                        TreeItem.Children(child, index)
                                    )
                                } else {
                                    children.add(
                                        TreeItem.ChildrenMid(child, index)
                                    )
                                }
                            }
                            if (result.isEmpty) {
//                                children.add(
//                                    TreeItem.ChildrenAdd(
//                                        User(
//                                            name = "No child",
//                                            motherId = user.value?.id, fatherId = mateId.value?.id , gender = user.value?.gender
//                                        ), 0
//                                    )
//                                )
                            }
                            Log.e("treeChildrenList", children.toString())
                        }
                }
            Log.e("treeChildrenList", children.toString())
        }

        fun getMateMother(){
                db.collection("User")
                    .whereEqualTo("familyId",user.value?.familyId)
                    .whereEqualTo("id", mateId.value?.motherId)
                    .whereEqualTo("gender", "female")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {

                            mateMotherId.value = document.toObject(User::class.java)
                            parents.add(
                                TreeItem.Parent(mateMotherId.value!!, false, 3)
                            )
                            Log.e("MateMother", parents.toString())
                            Log.e("MateMother", mateMotherId.value.toString())
                            getBranchView()
                        }
                        if (result.isEmpty) {
                            if (mateId.value != null) {
                                parents.add(
                                    TreeItem.ParentAdd(
                                        User(
                                            name = "No mateMother",
                                            motherId = mateId.value?.id
                                        ), false, 5
                                    )
                                )
                            }
                            getBranchView()
                        }
                    }
        }

        fun getMateFather(){
            mateId.value?.let { Log.e("MateID", it.toString()) }
            db.collection("User")
                .whereEqualTo("familyId",user.value?.familyId)
                .whereEqualTo("id", mateId.value?.fatherId)
                .whereEqualTo("gender", "male")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {

                        mateFatherId.value = document.toObject(User::class.java)
                        parents.add(
                            TreeItem.Parent(mateFatherId.value!!, false, 2)
                        )
                        getMateMother()

                    }
                    if (result.isEmpty) {
                        if (mateId.value != null) {
                            parents.add(
                                TreeItem.ParentAdd(
                                    User(
                                        name = "No mateFather",
                                        fatherId = mateId.value?.id
                                    ), false, 4
                                )
                            )
                        }
                        getMateMother()
                    }
                }
        }

        fun getUserMother(){
            db.collection("User")
                .whereEqualTo("familyId",user.value?.familyId)
                .whereEqualTo("id", user.value?.motherId)
                .whereEqualTo("gender", "female")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        //有媽媽 有配偶
                        if (user.value?.mateId != null) {
                            motherId.value = document.toObject(User::class.java)
                            parents.add(
                                TreeItem.Parent(motherId.value!!, true, 1)
                            )
                            getMateFather()
                            Log.e("Mother", parents.toString())
                            //有媽媽 沒配偶
                        } else {
                            motherId.value = document.toObject(User::class.java)
                            parents.add(
                                TreeItem.Parent(motherId.value!!, true, 1)
                            )
                            onlyMate.add(
                                TreeItem.MateAdd(
                                    User(name = "No mate", mateId = user.value?.id),
                                    false
                                )
                            )
                            getBranchView()
                        }
                    }
                    //沒媽媽 有配偶
                    if (result.isEmpty && user.value?.mateId != null) {
                        parents.add(
                            TreeItem.ParentAdd(
                                User(
                                    name = "No mother",
                                    motherId = user.value?.id
                                ), true, 1
                            )
                        )
                        getMateFather()
                    }
                    //有媽媽 沒配偶
                    if (result.isEmpty && user.value?.mateId == null) {
                        parents.add(
                            TreeItem.ParentAdd(
                                User(
                                    name = "No mother",
                                    motherId = user.value?.id
                                ), true, 1
                            )
                        )

                        onlyMate.add(
                            TreeItem.MateAdd(
                                User(name = "No mate", mateId = user.value?.id),
                                false
                            )
                        )
                        getBranchView()
                    }
                }
        }

        fun getUserFather(){
            db.collection("User")
                .whereEqualTo("familyId",user.value?.familyId)
                .whereEqualTo("id", user.value?.fatherId)
                .whereEqualTo("gender", "male")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        fatherId.value = document.toObject(User::class.java)
                        parents.add(
                            TreeItem.Parent(fatherId.value!!, true, 0)
                        )
                        Log.e("Father", parents.toString())
                        getUserMother()

                    }
                    if (result.isEmpty) {
                        parents.add(
                            TreeItem.ParentAdd(
                                User(
                                    name = "No father",
                                    fatherId = user.value?.id
                                ), true, 0
                            )
                        )
                        getUserMother()
                    }
                }
        }

        fun getUserMate(){
            Log.e("user.value?.mateId", user.value?.mateId.toString())
            db.collection("User")
                .whereEqualTo("familyId",user.value?.familyId)
                .whereEqualTo("id", user.value?.mateId)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        mateId.value = document.toObject(User::class.java)
                        meAndMate.add(
                            TreeItem.Mate((mateId.value!!), false)
                        )
                        Log.e("MMMMMMate", meAndMate.toString())
                        getUserChildren()
                    }
                }
                .addOnFailureListener {
                    Log.e("MateFail", it.toString())
                }
        }

        fun getUser(id: String) {
            db.collection("User")
                .whereEqualTo("id", id)
                .get()
                .addOnSuccessListener {
                    for (index in it) {
                        user.value = index.toObject(User::class.java)
                        if (user.value!!.mateId != null) {
                            getUserFather()
                            getUserMate()
                            meAndMate.add(
                                TreeItem.Mate(user.value!!, true)
                            )
                            Log.e("UUUUUser",user.value!!.toString())
                        } else {
                            getUserFather()
                            meAndMate.add(
                                TreeItem.Mate(user.value!!, true)
                            )
                        }
                    }
                }
                .addOnFailureListener {
                    continuation.resume(AppResult.Error(it))
                }
        }

        getUser(id)

    }

    override suspend fun updateFamily(family : Family, user: User): AppResult<Boolean> = suspendCoroutine { continuation ->
        val familyCollection = FirebaseFirestore.getInstance().collection(FAMILY)
        val userCollection = FirebaseFirestore.getInstance().collection(PATH_USER)

        familyCollection
            .document(family.id)
            .collection(PATH_USER)
            .document(user.id)
            .set(user).addOnSuccessListener {
                continuation.resume(AppResult.Success(true))
            }
            .addOnFailureListener {
                continuation.resume(AppResult.Error(it))
            }

        user.familyId = family.id

        userCollection.document(user.id)
            .update("familyId",family.id)
            .addOnSuccessListener {  }
            .addOnFailureListener {  }

    }

    override suspend fun getFamilyMember(family: Family): AppResult<List<User>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(FAMILY)
            .document(family.id)
            .collection(PATH_USER)
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

    override fun getLiveFamilyMember(family: Family): MutableLiveData<List<User>> {
        val liveData = MutableLiveData<List<User>>()

        FirebaseFirestore.getInstance()
            .collection(FAMILY)
            .document(family.id)
            .collection(PATH_USER)
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

    override suspend fun getFamily(): AppResult<List<Family>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(FAMILY)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Family>()
                    for (document in task.result!!) {
                        Log.d("Tron",document.id + " => " + document.data)

                        val family = document.toObject(Family::class.java)
                        list.add(family)
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

    override fun getLiveFamily(): MutableLiveData<List<Family>> {

        val liveData = MutableLiveData<List<Family>>()

        FirebaseFirestore.getInstance()
            .collection(FAMILY)
            .addSnapshotListener { snapshot, exception ->

                Log.i("Tron","addSnapshotListener detect")

                exception?.let {
                    Log.w("Tron","[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Family>()
                for (document in snapshot!!) {
                    Log.d("Tron",document.id + " => " + document.data)

                    val family = document.toObject(Family::class.java)
                    list.add(family)
                }
                liveData.value = list
            }
        return liveData
    }

    override suspend fun addFamily(family : Family, user: User): AppResult<Boolean> = suspendCoroutine { continuation ->
        val familyCollection = FirebaseFirestore.getInstance().collection(FAMILY)
        val userCollection = FirebaseFirestore.getInstance().collection(PATH_USER)

        family.id = familyCollection.document().id
            familyCollection
                .document(family.id)
            .set(family).addOnSuccessListener {
                continuation.resume(AppResult.Success(true))
            }
            .addOnFailureListener {
                continuation.resume(AppResult.Error(it))
            }

        user.familyId = family.id
        familyCollection
            .document(family.id)
            .collection(PATH_USER).document(UserManager.email.toString())
            .set(user).addOnSuccessListener {
//                continuation.resume(AppResult.Success(true))
            }
            .addOnFailureListener {
//                continuation.resume(AppResult.Error(it))
            }

        userCollection.document(user.id)
            .update("familyId",family.id)
            .addOnSuccessListener {  }
            .addOnFailureListener {  }

    }

    override suspend fun findEpisodeById(id: String): AppResult<Episode> = suspendCoroutine { continuation ->
        val userCollection = FirebaseFirestore.getInstance().collection(EPISODE)
        userCollection
            .document(id)
            .get()
            .addOnSuccessListener {
                if (it != null){
                        val episode = it.toObject(Episode::class.java)
                        continuation.resume(AppResult.Success(episode!!))
                    }
                }
            .addOnFailureListener {
                continuation.resume(AppResult.Error(it))
            }
    }

    override suspend fun getAllEpisode(): AppResult<List<Episode>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
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

    override suspend fun updateMapFamilyId(user: User): AppResult<Boolean> = suspendCoroutine { continuation ->
        val mapCollection = FirebaseFirestore.getInstance().collection(MAP).document(UserManager.email.toString())

        mapCollection
            .update("familyId",user.familyId)
            .addOnSuccessListener {
                continuation.resume(AppResult.Success(true))
            }
            .addOnFailureListener {
                continuation.resume(AppResult.Error(it))
            }
    }

    override suspend fun addLocation(map: Map): AppResult<Boolean> = suspendCoroutine { continuation ->
        val mapCollection = FirebaseFirestore.getInstance().collection(MAP).document(UserManager.email.toString())

        mapCollection
            .set(map).addOnSuccessListener {
                continuation.resume(AppResult.Success(true))
            }
            .addOnFailureListener {
                continuation.resume(AppResult.Error(it))
            }
    }

    override suspend fun getUserLocation(): AppResult<List<Map>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(MAP)
            .whereNotEqualTo("userId",UserManager.email.toString())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Map>()
                    for (document in task.result!!) {
                        Log.d("Tron",document.id + " => " + document.data)

                        val map = document.toObject(Map::class.java)
                        list.add(map)
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

    override fun getLiveUserLocation(): MutableLiveData<List<Map>> {
        val userCollection = FirebaseFirestore.getInstance()
            .collection(MAP)
            .whereNotEqualTo("userId",UserManager.email.toString())
        val liveData = MutableLiveData<List<Map>>()

        userCollection
            .addSnapshotListener { snapshot, exception ->

                Log.i("Tron","addSnapshotListener detect")

                exception?.let {
                    Log.w("Tron","[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Map>()
                for (document in snapshot!!) {
                    Log.d("Tron",document.id + " => " + document.data)

                    val map = document.toObject(Map::class.java)
                    list.add(map)
                }
                liveData.value = list
            }
        return liveData
    }

    override suspend fun getAlbum(event: Event): AppResult<List<Photo>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(EVENT)
            .document(event.id)
            .collection(ALBUM)
            .orderBy("createTime",Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Photo>()
                    for (document in task.result!!) {
                        Log.d("Tron",document.id + " => " + document.data)

                        val photo = document.toObject(Photo::class.java)
                        list.add(photo)
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

    override fun getLiveAlbum(event: Event): MutableLiveData<List<Photo>> {
        val userCollection = FirebaseFirestore.getInstance()
            .collection(EVENT)
            .document(event.id)
            .collection(ALBUM)
            .orderBy("createTime",Query.Direction.DESCENDING)
        val liveData = MutableLiveData<List<Photo>>()

        userCollection
            .addSnapshotListener { snapshot, exception ->

                Log.i("Tron","addSnapshotListener detect")

                exception?.let {
                    Log.w("Tron","[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Photo>()
                for (document in snapshot!!) {
                    Log.d("Tron",document.id + " => " + document.data)

                    val photo = document.toObject(Photo::class.java)
                    list.add(photo)
                }
                liveData.value = list
            }
        return liveData
    }

    override suspend fun addPhoto(event: Event,photo: Photo): AppResult<Boolean> = suspendCoroutine { continuation ->
        val userCollection = FirebaseFirestore.getInstance().collection(EVENT).document(event.id).collection(ALBUM)
        photo.id = userCollection.document().id
        userCollection.document(photo.id!!)
            .set(photo).addOnSuccessListener {
                continuation.resume(AppResult.Success(true))
            }
            .addOnFailureListener {
                continuation.resume(AppResult.Error(it))
            }
    }

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

    override suspend fun getEventByTime(date: String): AppResult<List<Event>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_USER)
            .document(UserManager.email.toString())
            .collection(EVENT)
            .whereEqualTo("date",date)
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


    override fun getLiveEventByUserId(id: String): MutableLiveData<List<Event>> {
        val userCollection = FirebaseFirestore.getInstance().collection(PATH_USER)
        val liveData = MutableLiveData<List<Event>>()

        userCollection
            .document(id)
            .collection(EVENT)
            .orderBy("eventTime",Query.Direction.DESCENDING)
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

    override suspend fun getEventByUserId(user: User): AppResult<List<Event>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_USER)
            .document(user.id)
            .collection(EVENT)
            .whereEqualTo("familyId",user.familyId)
            .orderBy("eventTime",Query.Direction.DESCENDING)
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

    override suspend fun getEventByFamilyId(id: String): AppResult<List<Event>> = suspendCoroutine { continuation ->

        val userCollection = FirebaseFirestore.getInstance().collection(PATH_USER)

        fun getEventByUserFamilyId(id: String){

            FirebaseFirestore.getInstance()
                .collection(EVENT)
                .whereEqualTo("publisherFamilyId", id)
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

        userCollection
            .whereEqualTo("id", id)
            .get()
            .addOnSuccessListener {
                if (it != null){
                    for (index in it) {
                        val user = index.toObject(User::class.java)
                        user.familyId?.let { it1 -> getEventByUserFamilyId(it1) }
                    }
                }
            }
            .addOnFailureListener {
                continuation.resume(AppResult.Error(it))
            }


    }

    override fun getLiveEventByFamilyId(id: String): MutableLiveData<List<Event>> {
        val liveData = MutableLiveData<List<Event>>()
        val userCollection = FirebaseFirestore.getInstance().collection(PATH_USER)

        fun getLiveEventByUserFamilyId(familyId : String) {
            FirebaseFirestore.getInstance()
                .collection(EVENT)
                .whereEqualTo("publisherFamilyId", familyId)
                .addSnapshotListener { snapshot, exception ->

                    Log.i("Tron", "addSnapshotListener detect")

                    exception?.let {
                        Log.w(
                            "Tron",
                            "[${this::class.simpleName}] Error getting documents. ${it.message}"
                        )
                    }

                    val list = mutableListOf<Event>()
                    for (document in snapshot!!) {
                        Log.d("Tron", document.id + " => " + document.data)

                        val event = document.toObject(Event::class.java)
                        list.add(event)
                    }
                    liveData.value = list
                }
        }

        userCollection
            .whereEqualTo("id", id)
            .get()
            .addOnSuccessListener {
                if (it != null){
                    for (index in it) {
                        val user = index.toObject(User::class.java)
                        user.familyId?.let { it1 -> getLiveEventByUserFamilyId(it1) }
                    }
                }
            }
            .addOnFailureListener {}

        return liveData
    }


    override fun getLiveEvent(): MutableLiveData<List<Event>> {
        val userCollection = FirebaseFirestore.getInstance().collection(EVENT)
        val liveData = MutableLiveData<List<Event>>()

        userCollection
            .orderBy("eventTime",Query.Direction.DESCENDING)
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
            .orderBy("eventTime",Query.Direction.DESCENDING)
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

    override suspend fun getEpisodeByFamilyId(familyId: String): AppResult<List<Episode>> = suspendCoroutine { continuation ->

        FirebaseFirestore.getInstance()
            .collection(EPISODE)
            .whereEqualTo("familyId", familyId)
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
        val episodeCollection = FirebaseFirestore.getInstance().collection(EPISODE)

        episode.id = episodeCollection.document().id

        userCollection.whereEqualTo("id",UserManager.email)
            .get()
            .addOnSuccessListener {
                for (index in it){
                    userCollection.document(index.id).collection(EPISODE).document(episode.id!!)
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


        episodeCollection.document(episode.id!!)
            .set(episode)
            .addOnSuccessListener {
                if (it != null) {
//                    continuation.resume(AppResult.Success(true))
                }
            }
            .addOnFailureListener {
//                continuation.resume(AppResult.Error(it))
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

    override suspend fun findFamilyById(id: String): AppResult<Family> = suspendCoroutine { continuation ->
        val userCollection = FirebaseFirestore.getInstance().collection(FAMILY)
        userCollection
            .document(id)
            .get()
            .addOnSuccessListener {
                        val user = it.toObject(Family::class.java)
                        continuation.resume(AppResult.Success(user!!))
                Log.e("findFamilyById", user.toString())
            }
            .addOnFailureListener {
                continuation.resume(AppResult.Error(it))
            }
    }

    override suspend fun findUserByName(name: String): AppResult<User> = suspendCoroutine { continuation ->
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

    override suspend fun findUser(id: String): AppResult<User> = suspendCoroutine { continuation ->
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


    override suspend fun addMemberReturnUser(user: User): AppResult<User> = suspendCoroutine { continuation ->

        val userPath = FirebaseFirestore.getInstance().collection(PATH_USER)
        val document = userPath.document()
        user.id = document.id

        fun getUser(){
            userPath.document(user.id)
                .get()
                .addOnSuccessListener {result ->
                    val newMember : User
                    if (result != null) {
                        newMember = result.toObject(User::class.java)!!
                        continuation.resume(AppResult.Success(newMember))
                    }
                }
        }

        document
            .set(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getUser()
                } else {
                    task.exception?.let {
                        continuation.resume(AppResult.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(AppResult.Fail(FamilyTreeApplication.INSTANCE.getString(R.string.you_know_nothing)))
                }
            }
    }

    override suspend fun addMember(user: User): AppResult<Boolean> = suspendCoroutine { continuation ->

        val userPath = FirebaseFirestore.getInstance().collection(PATH_USER)
        val document = userPath.document()
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

        User.document(user.id)
            .update("mateId",newMember.id)
            .addOnSuccessListener {
                continuation.resume(AppResult.Success(true))
            }
            .addOnFailureListener {
                continuation.resume(AppResult.Error(it))
            }
    }



    override suspend fun updateMemberFatherId(user: User, newMember : User): AppResult<Boolean> = suspendCoroutine { continuation ->
        val User = FirebaseFirestore.getInstance().collection(PATH_USER)
        val userFather = MutableLiveData<User>()

        fun updateFather(){
            User.document(user.id)
                .update("fatherId",newMember.id)
                .addOnSuccessListener {

                }
        }

        fun updateFatherMate(){
            User.document(newMember.id)
                .update("mateId",user.motherId)
                .addOnSuccessListener {

                }
        }

        fun updateMotherMate(){
            User.document(user.motherId!!)
                .update("mateId",newMember.id)
                .addOnSuccessListener {
                    continuation.resume(AppResult.Success(true))
                }
        }

        fun findFatherById(){
            User.whereEqualTo("id",newMember.id)
                .get()
                .addOnSuccessListener {
                    for (result in it){
                        userFather.value = result.toObject(com.tron.familytree.data.User::class.java)
                        if (user.motherId != null){
                            updateFatherMate()
                            updateMotherMate()
                        }

                    }
                }
        }


        updateFather()
        findFatherById()
    }

    override suspend fun updateMemberMotherId(user: User, newMember : User): AppResult<Boolean> = suspendCoroutine { continuation ->
        val User = FirebaseFirestore.getInstance().collection(PATH_USER)
        val userMother = MutableLiveData<User>()

        fun updateMother(){
            User.document(user.id)
                .update("motherId",newMember.id)
                .addOnSuccessListener {

                }
        }

        fun updateMotherMate(){
            User.document(newMember.id)
                .update("mateId",user.fatherId)
                .addOnSuccessListener {

                }
        }

        fun updateFatherMate(){
            User.document(user.fatherId!!)
                .update("mateId",newMember.id)
                .addOnSuccessListener {
                    continuation.resume(AppResult.Success(true))
                }
        }

        fun findMotherById(){
            User.whereEqualTo("id",newMember.id)
                .get()
                .addOnSuccessListener {
                    for (result in it){
                        userMother.value = result.toObject(com.tron.familytree.data.User::class.java)
                        if (user.fatherId != null){
                            updateMotherMate()
                            updateFatherMate()
                        }
                    }
                }
        }


        updateMother()
        findMotherById()
    }
}
