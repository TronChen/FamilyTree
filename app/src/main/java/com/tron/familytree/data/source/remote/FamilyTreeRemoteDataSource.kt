package app.appworks.school.publisher.data.source.remote

import android.icu.util.Calendar
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
import com.tron.familytree.data.AppResult
import com.tron.familytree.data.User
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object FamilyTreeRemoteDataSource : FamilyTreeDataSource {

    private const val PATH_ARTICLES = "articles"
    private const val KEY_CREATED_TIME = "createdTime"
    private const val PATH_USER = "User"

//    override suspend fun login(id: String): AppResult<User> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }

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


    override suspend fun updateMemberFatherId(user: User, newMember : User): AppResult<Boolean> = suspendCoroutine { continuation ->
        val User = FirebaseFirestore.getInstance().collection(PATH_USER)
        var path: String = ""

        User.whereEqualTo("name",user.fatherId)
            .get()
            .addOnSuccessListener {
                for (index in it) {
                    path = index.id
                    Log.e("FindFather", index.id)
                    //找到原先user
                    val document = User.document(path)

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
                }
            }
    }

    override suspend fun updateMemberMotherId(user: User, newMember : User): AppResult<Boolean> = suspendCoroutine { continuation ->
        val User = FirebaseFirestore.getInstance().collection(PATH_USER)
        var path: String = ""

        User.whereEqualTo("name",user.motherId)
            .get()
            .addOnSuccessListener {
                for (index in it) {
                    path = index.id
                    Log.e("FindMother", index.id)
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
