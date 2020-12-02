package com.tron.familytree.branch

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tron.familytree.data.User

class BranchViewModel : ViewModel() {

    val db = Firebase.firestore

    var user = MutableLiveData<com.tron.familytree.data.User>()
    var userMate = MutableLiveData<com.tron.familytree.data.User>()
    val userId = "TronChen"

    var mateId = MutableLiveData<com.tron.familytree.data.User>()
    var fatherId = MutableLiveData<com.tron.familytree.data.User>()
    var motherId = MutableLiveData<com.tron.familytree.data.User>()
    var mateFatherId = MutableLiveData<com.tron.familytree.data.User>()
    var mateMotherId = MutableLiveData<com.tron.familytree.data.User>()

    val treeParentsList = mutableListOf<com.tron.familytree.data.User>()
    val treeMeAndMateList = mutableListOf<com.tron.familytree.data.User>()
    val treeChildrenList = mutableListOf<com.tron.familytree.data.User>()
    val treeList = mutableListOf<MutableList<com.tron.familytree.data.User>>()

    val TreeList = MutableLiveData<List<List<com.tron.familytree.data.User>>>()

    val emptyData = User()
    val adapterList = mutableListOf<User>()

    init {
        getUser()
    }

    fun getUser(){
        db.collection("User").document("$userId")
            .get()
            .addOnSuccessListener {
                user.value = it.toObject(com.tron.familytree.data.User::class.java)
                treeMeAndMateList.add(user.value!!)
            }
    }

    fun getUserMate(){
        db.collection("User").whereEqualTo("name", user.value?.mateId)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    mateId.value = document.toObject(com.tron.familytree.data.User::class.java)
                    treeMeAndMateList.add(mateId.value!!)
                    Log.e("Mate", mateId.value.toString())
                    Log.e("treeMeAndMateList", treeMeAndMateList.toString())
//                    Log.e("userMate.value", userMate.value!!.toString())
                }
            }
    }

    fun getUserChildren(){
        db.collection("User").whereEqualTo("fatherId", user.value?.name)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    treeChildrenList.add(document.toObject(com.tron.familytree.data.User::class.java))
                    Log.e("Children", treeChildrenList.toString())
                }
            }
    }

    fun getUserFather(){
        db.collection("User").whereEqualTo("childId", user.value?.name).whereEqualTo("gender","male")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    fatherId.value = document.toObject(com.tron.familytree.data.User::class.java)
                    treeParentsList.add(fatherId.value!!)
                    Log.e("Father", treeParentsList.toString())
                }
            }
    }

    fun getUserMother(){
        db.collection("User").whereEqualTo("childId", user.value?.name).whereEqualTo("gender","female")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    motherId.value = document.toObject(com.tron.familytree.data.User::class.java)
                    treeParentsList.add(motherId.value!!)
                    Log.e("Mother", motherId.value!!.toString())
                }
            }
    }

    fun getMateFather(){
        db.collection("User").whereEqualTo("childId", user.value?.mateId).whereEqualTo("gender","male")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    mateFatherId.value = document.toObject(com.tron.familytree.data.User::class.java)
                    treeParentsList.add(mateFatherId.value!!)
                    Log.e("mateFather", mateFatherId.value!!.toString())
                }
            }
    }

        fun getMateMother(){
        db.collection("User").whereEqualTo("childId", user.value?.mateId).whereEqualTo("gender","female")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    mateMotherId.value = document.toObject(com.tron.familytree.data.User::class.java)
                    treeParentsList.add(mateMotherId.value!!)

                    treeList.add(treeParentsList)
                    treeList.add(treeMeAndMateList)
                    treeList.add(treeChildrenList)
                    TreeList.value = treeList
                    Log.e("treeList", treeList.toString())
                    Log.e("mateMother", mateMotherId.value!!.toString())
                    Log.e("treeParentsList", treeParentsList.toString())
                }
            }
    }


    val children = mutableListOf<TreeItem>()
    val parents = mutableListOf<TreeItem>()
    val meAndMate = mutableListOf<TreeItem>()

    fun getSpanCount(size: Int): Int{
        return if (size > 4 || size == 4){
            size
        } else{
            5
        }
    }

    fun createMock() {
        parents.add(
            TreeItem.Parent(
                User(name = "DadOfME", gender = "male"),
                true,
                0
            )
        )
        parents.add(
            TreeItem.Parent(
                User(name = "MomOfME", gender = "female"),
                true,
                1
            )
        )
        parents.add(
            TreeItem.Parent(
                User(name = "DadOfMATE", gender = "male"),
                false,
                2
            )
        )
        parents.add(
            TreeItem.Parent(
                User(name = "MomOfMATE", gender = "female"),
                false,
                3
            )
        )

        meAndMate.add(
            TreeItem.Mate(
                User(name = "ME"),
                true
            )
        )
        meAndMate.add(
            TreeItem.Mate(
                User(name = "MATE"),
                false
            )
        )

//        for (index in 0..1) {
//            children.add(
//                TreeItem.Children(
//                    User(name = "C$index"),
//                    index
//                )
//            )
//        }
    }

    fun getChildrenList(item: Int){
        if (item < 2){
            for (index in 0..item) {
                children.add(
                    TreeItem.ChildrenMid(
                        User(name = "C$index"),
                        index
                    )
                )
            }
        }
        else{
            for (index in 0..item) {
                children.add(
                    TreeItem.Children(
                        User(name = "C$index"),
                        index
                    )
                )
            }
        }
    }


        val mockList = mutableListOf<TreeItem>()
    fun getMockUsers(): MutableList<TreeItem> {
        //Parent
        for ((index, parent) in parents.withIndex()) {

            if (children.size < 4) {
                (parent as TreeItem.Parent).user.spanSize = 1
                if (index == 2) {
                        mockList.add(
                            TreeItem.Empty(-1)
                        )
                }
                mockList.add(parent)
            }

            if (children.size > 4 || children.size == 4) {
                (parent as TreeItem.Parent).user.spanSize = children.size / 2 / 2

                if (children.size %2 == 1 && parents.size > 2){
                    if(parents.size % (index + 1) == 1){
                        mockList.add(
                            TreeItem.Empty(-1)
                        )
                    }
                }

                if (index % 2 == 1) {
                    if (children.size % 4  == 2) {
                        mockList.add(
                            TreeItem.EmptyLineBot(-1)
                        )
                    }
                    if (children.size % 4  == 3) {
                        mockList.add(
                            TreeItem.EmptyLineBot(-1)
                        )
                    }
                }

                mockList.add(parent)
            }
        }


        //MeAndMate
        for ((index,self) in meAndMate.withIndex()) {

            if (children.size < 4) {
                (self as TreeItem.Mate).user.spanSize = 2
                if(index == 1){
                    mockList.add(
                        TreeItem.Empty(-1)
                    )
                }
                mockList.add(self)
            }

            if (children.size > 4 || children.size == 4) {
                (self as TreeItem.Mate).user.spanSize = children.size / 2

                if (children.size %2 == 1 && meAndMate.size > 2){
                    if(children.size % (index + 1) == 1){
                        mockList.add(
                            TreeItem.Empty(-1)
                        )
                    }
                }
                if (children.size %2 == 1 && meAndMate.size == 2) {
                    if (index == 1) {
                        mockList.add(
                            TreeItem.Empty(-1)
                        )
                    }
                }

                mockList.add(self)
            }
        }


        //Children
        for ((index,child) in children.withIndex()) {
            if (children.size == 1){
                if (index == 0){
                    mockList.add(
                        TreeItem.Empty(-1)
                    )
                    mockList.add(
                        TreeItem.EmptyLine(-1)
                    )
                }

                mockList.add(child)

                    mockList.add(
                        TreeItem.EmptyLine(-1)
                    )
                    mockList.add(
                        TreeItem.Empty(-1)
                    )
            }

            if (children.size == 2){
                if (index == 0){
                    mockList.add(
                        TreeItem.Empty(-1)
                    )
                }
                if (index == 1){
                    mockList.add(
                        TreeItem.EmptyLine(-1)
                    )
                }
                mockList.add(child)
            }

            if (children.size == 3){
                if (index > 0){
                    mockList.add(
                        TreeItem.EmptyLine(-1)
                    )
                }
                mockList.add(child)
            }

            if (children.size > 4 || children.size == 4){
            mockList.add(child)
          }
        }

        return  mockList
    }

}