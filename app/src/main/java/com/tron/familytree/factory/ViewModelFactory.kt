//package com.tron.familytree.factory
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.tron.familytree.MainActivityViewModel
//import com.tron.familytree.branch.BranchViewModel
//import com.tron.familytree.data.source.FamilyTreeRepository
//import com.tron.familytree.family.FamilyViewModel
//import com.tron.familytree.map.MapViewModel
//import com.tron.familytree.message.MessageViewModel
//import com.tron.familytree.profile.ProfileViewModel
//
//@Suppress(names = ["UNCHECKED_CAST"])
//class ViewModelFactory constructor(
//    private val familyTreeRepository: FamilyTreeRepository
//) : ViewModelProvider.NewInstanceFactory() {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>) =
//        with(modelClass) {
//            when {
//                isAssignableFrom(MainActivityViewModel::class.java) ->
//                    MainActivityViewModel(familyTreeRepository)
//
//                isAssignableFrom(BranchViewModel::class.java) ->
//                    BranchViewModel(familyTreeRepository)
//
//                isAssignableFrom(FamilyViewModel::class.java) ->
//                    FamilyViewModel(familyTreeRepository)
//
//                isAssignableFrom(MapViewModel::class.java) ->
//                    MapViewModel(familyTreeRepository)
//
//                isAssignableFrom(MessageViewModel::class.java) ->
//                    MessageViewModel(familyTreeRepository)
//
//                isAssignableFrom(ProfileViewModel::class.java) ->
//                    ProfileViewModel(familyTreeRepository)
//                else ->
//                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
//            }
//        } as T
//}
