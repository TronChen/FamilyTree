package com.tron.familytree.branch

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tron.familytree.data.User

class BranchViewModel : ViewModel() {

    val db = Firebase.firestore

    var user = MutableLiveData<User>()
    val userId = MutableLiveData<String>()

    var mateId = MutableLiveData<User>()
    var fatherId = MutableLiveData<User>()
    var motherId = MutableLiveData<User>()
    var mateFatherId = MutableLiveData<User>()
    var mateMotherId = MutableLiveData<User>()

    var itemClick = MutableLiveData<Int>()
    var itemSelected = MutableLiveData<User>()

    val TreeList = MutableLiveData<List<TreeItem>>()

    val children = mutableListOf<TreeItem>()
    val parents = mutableListOf<TreeItem>()
    val meAndMate = mutableListOf<TreeItem>()
    val onlyMate = mutableListOf<TreeItem>()

    var treeFinalList = mutableListOf<TreeItem>()

    init {
        userId.value = "TronTron"
    }

    fun getUser(){
        db.collection("User").document("${userId.value}")
            .get()
            .addOnSuccessListener {
                user.value = it.toObject(User::class.java)
                if (user.value!!.mateId != null){
                getUserFather()
                getUserMate()
                meAndMate.add(
                    TreeItem.Mate( user.value!!,true)
                )
                }else{
                    getUserFather()
                    meAndMate.add(
                        TreeItem.Mate( user.value!!,true)
                    )
                }
            }
    }

    fun getUserMate(){
        Log.e("user.value?.mateId", user.value?.mateId.toString())
        db.collection("User").whereEqualTo("name", user.value?.mateId)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    mateId.value = document.toObject(User::class.java)
                    meAndMate.add(
                        TreeItem.Mate((mateId.value!!),false)
                    )
                    Log.e("Mate", meAndMate.toString())
                    getUserChildren()
                }
            }
    }

    fun getUserChildren(){
        if (user.value?.gender == "male") {
            db.collection("User").whereEqualTo("fatherId", user.value?.name)
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
                    if (result.isEmpty){
                        children.add(
                            TreeItem.ChildrenAdd(User(name = "No child"), 0)
                        )
                    }
                }
        }

        if (user.value?.gender == "female") {
            db.collection("User").whereEqualTo("motherId", user.value?.name)
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
                    if (result.isEmpty){
                        children.add(
                            TreeItem.ChildrenAdd(User(name = "No child"), 0)
                        )
                    }
                    Log.e("treeChildrenList", children.toString())
                }
        }
    }

    fun getUserFather(){
        db.collection("User").whereEqualTo("name", user.value?.fatherId).whereEqualTo("gender","male")
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
                    if (result.isEmpty){
                        parents.add(
                        TreeItem.ParentAdd(User(name = "No father"),true,0)
                        )
                        getUserMother()
                    }
            }
    }

    fun getUserMother(){
        db.collection("User").whereEqualTo("name", user.value?.motherId).whereEqualTo("gender","female")
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
                            TreeItem.MateAdd(User(name = "No mate"), false)
                        )
                        getMockUsers()
                    }
                }
                    //沒媽媽 有配偶
                    if (result.isEmpty && user.value?.mateId != null){
                        parents.add(
                            TreeItem.ParentAdd(User(name = "No mother"), true, 1)
                        )
                        getMateFather()
                    }
                    //有媽媽 沒配偶
                    if (result.isEmpty && user.value?.mateId == null){
                        parents.add(
                            TreeItem.ParentAdd(User(name = "No mother"), true, 1)
                        )

                        onlyMate.add(
                            TreeItem.MateAdd(User(name = "No mate"), false)
                        )
                        getMockUsers()
                    }
            }
    }

    fun getMateFather(){
        mateId.value?.let { Log.e("MateID", it.toString()) }
        db.collection("User").whereEqualTo("name", mateId.value?.fatherId).whereEqualTo("gender","male")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                        mateFatherId.value = document.toObject(User::class.java)
                        parents.add(
                            TreeItem.Parent(mateFatherId.value!!, false, 2)
                        )
                        getMateMother()

                }
                    if (result.isEmpty){
                        if (mateId.value != null){
                            parents.add(
                                TreeItem.ParentAdd(User(name = "No mateFather"),false,4)
                            )
                        }
                        getMateMother()
                    }
            }
    }

        fun getMateMother(){
        db.collection("User").whereEqualTo("name", mateId.value?.motherId).whereEqualTo("gender","female")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                        mateMotherId.value = document.toObject(User::class.java)
                        parents.add(
                            TreeItem.Parent(mateMotherId.value!!, false, 3)
                        )
                        Log.e("MateMother", parents.toString())
                        Log.e("MateMother", mateMotherId.value.toString())
                        getMockUsers()

                }
                    if (result.isEmpty){
                        if (mateId.value != null) {
                            parents.add(
                                TreeItem.ParentAdd(User(name = "No mateMother"), false, 5)
                            )
                        }
                        getMockUsers()
                    }
            }
    }

    fun getSpanCount(size: Int): Int{
        return if (size > 4){
            size + 1
        } else{
            5
        }
    }


    fun getMockUsers(): MutableList<TreeItem> {

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
                    treeFinalList.add(
                        TreeItem.ChildrenAdd(User(name = "No child"), 1)
                    )
                }

//                if (children.size == 3) {
//                    if (index > 0) {
//                        treeFinalList.add(
//                            TreeItem.EmptyLine(-1)
//                        )
//                    }
//                    treeFinalList.add(child)
//                }
//
//                if (children.size > 4 || children.size == 4) {
//                    treeFinalList.add(child)
//                }
            }
        if (children.size == 2){
            treeFinalList.add(TreeItem.Empty(-1))
            for (child in children){
                treeFinalList.add(child)
            }
            treeFinalList.add(
                TreeItem.ChildrenAdd(User(name = "No child"), 1)
            )
        }

        if (children.size == 3) {
            for (child in children) {
                treeFinalList.add(child)
            }
            treeFinalList.add(
                TreeItem.ChildrenAdd(User(name = "No child"), 3)
            )
        }

        if (children.size > 4 || children.size == 4) {
            for (child in children) {
                treeFinalList.add(child)
            }
            treeFinalList.add(
                TreeItem.ChildrenAdd(User(name = "No child"), 10000)
            )
        }


//        else {
//            for ((index, parent) in parents.withIndex()) {
//                (parent as TreeItem.Parent).user.spanSize = 1
//                if (index == 2) {
//                    treeFinalList.add(
//                        TreeItem.Empty(-1)
//                    )
//                }
//                treeFinalList.add(parent)
//            }
//        }

        Log.e("treeFinalList", treeFinalList.toString())
       TreeList.value = treeFinalList
        return  treeFinalList

    }


    fun reQuery(){
        parents.clear()
        meAndMate.clear()
        children.clear()
        treeFinalList.clear()
        onlyMate.clear()
        mateId.value = null
        fatherId.value = null
        motherId.value = null
        mateFatherId.value = null
        mateMotherId.value = null
    }

}