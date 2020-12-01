//package com.tron.familytree.ext
//
//import androidx.fragment.app.Fragment
//import com.tron.familytree.FamilyTreeApplication
//import com.tron.familytree.factory.ViewModelFactory
//
//fun Fragment.getVmFactory(): ViewModelFactory {
//    val repository = (requireContext().applicationContext as FamilyTreeApplication).familyTreeRepository
//    return ViewModelFactory(repository)
//}