package app.appworks.school.publisher.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tron.familytree.data.AppResult
import com.tron.familytree.data.User
import kotlin.coroutines.Continuation


interface FamilyTreeDataSource {

//    suspend fun login(id: String): AppResult<User>

    suspend fun getArticles(): AppResult<List<User>>

    fun getLiveArticles(): MutableLiveData<List<User>>
}
