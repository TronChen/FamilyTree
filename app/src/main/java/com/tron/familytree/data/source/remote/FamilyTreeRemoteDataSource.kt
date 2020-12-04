package app.appworks.school.publisher.data.source.remote

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import app.appworks.school.publisher.data.source.FamilyTreeDataSource
import com.google.firebase.firestore.Query
import com.tron.familytree.FamilyTreeApplication
import com.tron.familytree.R
import com.tron.familytree.data.AppResult
import com.tron.familytree.data.User
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object FamilyTreeRemoteDataSource : FamilyTreeDataSource {

    private const val PATH_ARTICLES = "articles"
    private const val KEY_CREATED_TIME = "createdTime"

//    override suspend fun login(id: String): AppResult<User> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }

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
