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

    fun getMockUsers(): MutableList<TreeItem> {
        val parents = mutableListOf<TreeItem>()
        parents.add(
            TreeItem.Parent(
                User(name = "DadOfME"),
                true
            )
        )
        parents.add(
            TreeItem.Parent(
                User(name = "MomOfME"),
                true
            )
        )
        parents.add(
            TreeItem.Parent(
                User(name = "DadOfMATE"),
                false
            )
        )
        parents.add(
            TreeItem.Parent(
                User(name = "MomOfMATE"),
                false
            )
        )
        val meAndMate = mutableListOf<TreeItem>()
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

        val children = mutableListOf<TreeItem>()

        for (index in 0..7) {
            children.add(
                TreeItem.Children(
                    User(name = "C$index"),
                    index
                )
            )
        }

        val mockList = mutableListOf<TreeItem>()
        for (parent in parents) {
            if (children.size > 4) {
                (parent as TreeItem.Parent).user.spanSize = children.size / 2 / 2
                mockList.add(parent)
            }
        }

        for (self in meAndMate) {
            if (children.size > 4) {
                (self as TreeItem.Mate).user.spanSize = children.size / 2
                mockList.add(self)
            }
        }

        for (child in children) {
            mockList.add(child)
        }

        return  mockList
    }

    fun addAdapterList(){
        val x = 7
        val a = x * 2
        val b = x * 3
        val c = x * 4
        val d = x * 5
        val e = x * 6
        val f = x * 7
        val g = x * 8

        //User
        val lv1_1 = listOf(x + 3)
        val lv1_2 = listOf(x + 1, x + 5)
        val lv1_3 = listOf(x + 1, x + 3, x + 5)
        val lv1_4 = listOf(x, x + 2, x + 4, x + 6)
        val lv1_5 = listOf(x, x + 2, x + 3, x + 4, x + 6)
        val lv1_6 = listOf(x, x + 1, x + 2, x + 3, x + 4, x + 5)
        val lv1_7 = listOf(x, x + 1, x + 2, x + 3, x + 4, x + 5, x + 6)

        val lv4_1 = listOf(c + 3)
        val lv4_2 = listOf(c + 1, c + 5)
        val lv4_3 = listOf(c + 1, c + 3, c + 5)
        val lv4_4 = listOf(c, c + 2, c + 4, c + 6)
        val lv4_5 = listOf(c, c + 2, c + 3, c + 4, c + 6)
        val lv4_6 = listOf(c, c + 1, c + 2, c + 3, c + 4, c + 5)
        val lv4_7 = listOf(c, c + 1, c + 2, c + 3, c + 4, c + 5, c + 6)

        val lv7_1 = listOf(f + 3)
        val lv7_2 = listOf(f + 1, f + 5)
        val lv7_3 = listOf(f + 1, f + 3, f + 5)
        val lv7_4 = listOf(f, f + 2, f + 4, f + 6)
        val lv7_5 = listOf(f, f + 2, f + 3, f + 4, f + 6)
        val lv7_6 = listOf(f, f + 1, f + 2, f + 4, f + 5, f + 6)
        val lv7_7 = listOf(f, f + 1, f + 2, f + 3, f + 4, f + 5, f + 6)

        for (index in 0..62){

            //fatherId
            if(index == 7){
                adapterList.add(fatherId.value!!)
            }

            //motherId
            if(index == 8){
                adapterList.add(motherId.value!!)
            }

            //mateFatherId
            if(index == 9){
                adapterList.add(mateFatherId.value!!)
            }

            //mateMotherId
            if(index == 10){
                adapterList.add(mateMotherId.value!!)
            }

            //user
            if(index == 25){
                adapterList.add(user.value!!)
            }

            //mateId
            if(index == 28){
                adapterList.add(mateId.value!!)
            }
//            if(index == f+1){
//                adapterList.add(treeChildrenList)
//            }

             adapterList.add(emptyData)
            Log.e("adapterList", adapterList.size.toString())
        }
    }

}