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

    val TreeList = MutableLiveData<Int>()

    val children = mutableListOf<TreeItem>()
    val parents = mutableListOf<TreeItem>()
    val meAndMate = mutableListOf<TreeItem>()

    var treeFinalList = mutableListOf<TreeItem>()

    init {
        userId.value = "Paul"
    }

    fun getUser(){
        db.collection("User").document("${userId.value}")
            .get()
            .addOnSuccessListener {
                user.value = it.toObject(User::class.java)
                meAndMate.add(
                    TreeItem.Mate( user.value!!,true)
                )
                Log.e("Me", meAndMate.toString())
            }
    }

    fun getUserMate(){
        db.collection("User").whereEqualTo("name", user.value?.mateId)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    mateId.value = document.toObject(User::class.java)
                    meAndMate.add(
                        TreeItem.Mate((mateId.value!!),false)
                    )
                    Log.e("Mate", meAndMate.toString())
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

                    for (child in children) {
                        Log.d("treeChildrenList", "child=$child")
                    }

                    Log.e("treeChildrenList", children.toString())
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

                    for (child in children) {
                        Log.d("treeChildrenList", "child=$child")
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
                        TreeItem.Parent(fatherId.value!!,true,0))
                    Log.e("Father", parents.toString())
                }
            }
    }

    fun getUserMother(){
        db.collection("User").whereEqualTo("name", user.value?.motherId).whereEqualTo("gender","female")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                   motherId.value = document.toObject(User::class.java)
                    parents.add(
                        TreeItem.Parent(motherId.value!!,true,1))
                    Log.e("Mother", parents.toString())
                }
            }
    }

    fun getMateFather(){
        db.collection("User").whereEqualTo("name", mateId.value?.fatherId).whereEqualTo("gender","male")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    mateFatherId.value = document.toObject(User::class.java)
                    parents.add(
                        TreeItem.Parent(mateFatherId.value!!,false,2))
                    Log.e("mateFather", parents.toString())
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
                        TreeItem.Parent(mateMotherId.value!!,false,3))
                    Log.e("MateMother", parents.toString())
                    TreeList.value = 100
                }
            }
    }

    fun getSpanCount(size: Int): Int{
        return if (size > 4 || size == 4){
            size
        } else{
            5
        }
    }


    fun getMockUsers(): MutableList<TreeItem> {
        //Parent
        for ((index, parent) in parents.withIndex()) {

            if (children.size < 4) {
                (parent as TreeItem.Parent).user.spanSize = 1
                if (index == 2) {
                        treeFinalList.add(
                            TreeItem.Empty(-1)
                        )
                }
                treeFinalList.add(parent)
            }

            if (children.size > 4 || children.size == 4) {
                (parent as TreeItem.Parent).user.spanSize = children.size / 2 / 2

                if (children.size %2 == 1 && parents.size > 2){
                    if(parents.size % (index + 1) == 1){
                        treeFinalList.add(
                            TreeItem.Empty(-1)
                        )
                    }
                }

                if (index % 2 == 1) {
                    if (children.size % 4  == 2) {
                        treeFinalList.add(
                            TreeItem.EmptyLineBot(-1)
                        )
                    }
                    if (children.size % 4  == 3) {
                        treeFinalList.add(
                            TreeItem.EmptyLineBot(-1)
                        )
                    }
                }

                treeFinalList.add(parent)
            }
        }


        //MeAndMate
        for ((index,self) in meAndMate.withIndex()) {

            if (children.size < 4) {
                (self as TreeItem.Mate).user.spanSize = 2
                if(index == 1){
                    treeFinalList.add(
                        TreeItem.Empty(-1)
                    )
                }
                treeFinalList.add(self)
            }

            if (children.size > 4 || children.size == 4) {
                (self as TreeItem.Mate).user.spanSize = children.size / 2

                if (children.size %2 == 1 && meAndMate.size > 2){
                    if(children.size % (index + 1) == 1){
                        treeFinalList.add(
                            TreeItem.Empty(-1)
                        )
                    }
                }
                if (children.size %2 == 1 && meAndMate.size == 2) {
                    if (index == 1) {
                        treeFinalList.add(
                            TreeItem.Empty(-1)
                        )
                    }
                }

                treeFinalList.add(self)
            }
        }


        //Children
        for ((index,child) in children.withIndex()) {
            if (children.size == 1){
                if (index == 0){
                    treeFinalList.add(
                        TreeItem.Empty(-1)
                    )
                    treeFinalList.add(
                        TreeItem.EmptyLine(-1)
                    )
                }

                treeFinalList.add(child)

                    treeFinalList.add(
                        TreeItem.EmptyLine(-1)
                    )
                    treeFinalList.add(
                        TreeItem.Empty(-1)
                    )
            }

            if (children.size == 2){
                when(index){
                    0 -> treeFinalList.add(
                        TreeItem.Empty(-1)
                    )

                    1 ->  treeFinalList.add(
                        TreeItem.EmptyLine(-1)
                    )
                }
//                if (index == 0){
//                    treeFinalList.add(
//                        TreeItem.Empty(-1)
//                    )
//                }
//                if (index == 1){
//                    treeFinalList.add(
//                        TreeItem.EmptyLine(-1)
//                    )
//                }
                treeFinalList.add(child)
            }

            if (children.size == 3){
                if (index > 0){
                    treeFinalList.add(
                        TreeItem.EmptyLine(-1)
                    )
                }
                treeFinalList.add(child)
            }

            if (children.size > 4 || children.size == 4){
            treeFinalList.add(child)
          }
        }

        return  treeFinalList
    }

}