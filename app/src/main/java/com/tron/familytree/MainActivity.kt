package com.tron.familytree

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tron.familytree.databinding.ActivityMainBinding
import com.tron.familytree.databinding.NavHeaderDrawerBinding
import java.util.Observer


class MainActivity : AppCompatActivity() {

    var imgPath = MutableLiveData<String>()

    private lateinit var binding: ActivityMainBinding
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {

            R.id.navigation_branch -> {
                binding.fab.visibility = View.VISIBLE
                binding.imageSearch.visibility = View.VISIBLE
                binding.imageCalendar.visibility = View.INVISIBLE
                binding.imageScan.visibility = View.INVISIBLE
                binding.imageEdit.visibility = View.INVISIBLE
                findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_branchFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map-> {
                binding.fab.visibility = View.VISIBLE
                binding.imageSearch.visibility = View.INVISIBLE
                binding.imageCalendar.visibility = View.INVISIBLE
                binding.imageScan.visibility = View.INVISIBLE
                binding.imageEdit.visibility = View.INVISIBLE
                findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_mapsFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_message -> {
                binding.fab.visibility = View.VISIBLE
                binding.imageSearch.visibility = View.INVISIBLE
                binding.imageCalendar.visibility = View.INVISIBLE
                binding.imageScan.visibility = View.INVISIBLE
                binding.imageEdit.visibility = View.INVISIBLE
                findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_messageFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                binding.fab.visibility = View.VISIBLE
                binding.imageSearch.visibility = View.INVISIBLE
                binding.imageCalendar.visibility = View.INVISIBLE
                binding.imageScan.visibility = View.VISIBLE
                binding.imageEdit.visibility = View.VISIBLE
                findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_profileFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        val navController = this.findNavController(R.id.myNavHostFragment)
        binding.fab.setOnClickListener {
            binding.fab.visibility = View.INVISIBLE
            binding.imageSearch.visibility = View.INVISIBLE
            binding.imageCalendar.visibility = View.VISIBLE
            binding.imageScan.visibility = View.INVISIBLE
            binding.imageEdit.visibility = View.INVISIBLE
            navController.navigate(R.id.action_global_familyFragment)
        }

        binding.menuEvent.setOnClickListener {
            binding.imageSearch.visibility = View.INVISIBLE
            binding.imageCalendar.visibility = View.VISIBLE
            binding.imageScan.visibility = View.INVISIBLE
            binding.imageEdit.visibility = View.INVISIBLE
            navController.navigate(R.id.action_global_createEventDialog)
        }

        binding.menuAlbum.setOnClickListener {
            binding.imageSearch.visibility = View.INVISIBLE
            binding.imageCalendar.visibility = View.VISIBLE
            binding.imageScan.visibility = View.INVISIBLE
            binding.imageEdit.visibility = View.INVISIBLE
            navController.navigate(R.id.action_global_createAlbumDialog)
        }

        binding.imageCalendar.setOnClickListener {
            navController.navigate(R.id.action_global_calendarDialog)
        }

        binding.imageScan.setOnClickListener {
            navController.navigate(R.id.action_global_qrCodeReaderFragment)
        }

        binding.imageEdit.setOnClickListener {
            navController.navigate(R.id.action_global_editUserFragment)
        }

        setupBottomNav()
        setupDrawer()
    }



    private fun setupBottomNav() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }


    private fun setupDrawer() {

        // set up toolbar
        val navController = this.findNavController(R.id.myNavHostFragment)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null

        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.drawerNavView, navController)


        binding.drawerNavView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_branch -> {
//                    viewModel.navigate.value = 1
                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_branchFragment)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.navigation_map->{
//                    val bundle = bundleOf("type" to 0)
                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_mapFragment)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
//                    viewModel.currentFragmentType.value = CurrentFragmentType.READY_TO_SHIP
                    true
                }
                R.id.navigation_message->{
//                    val bundle = bundleOf("type" to 1)
                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_messageFragment)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
//                    viewModel.currentFragmentType.value = CurrentFragmentType.SHIPPING
                    true
                }
                R.id.navigation_profile->{
                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_profileFragment)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
//                    viewModel.currentFragmentType.value = CurrentFragmentType.SHIPPED
                    true
                }
                else -> false
            }
        }

        binding.drawerLayout.fitsSystemWindows = true
        binding.drawerLayout.clipToPadding = false

        actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)

//                when (UserManager.isLoggedIn) { // check user login status when open drawer
//                    true -> {
//                        viewModel.checkUser()
//                    }
//                    else -> {
//                        findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToLoginDialog())
//                        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                            binding.drawerLayout.closeDrawer(GravityCompat.START)
//                        }
//                    }
//                }

            }
        }.apply {
            binding.drawerLayout.addDrawerListener(this)
            syncState()
        }

        // Set up header of drawer ui using data binding
        val bindingNavHeader = NavHeaderDrawerBinding.inflate(
            LayoutInflater.from(this), binding.drawerNavView, false)

        bindingNavHeader.lifecycleOwner = this
//        bindingNavHeader.viewModel = viewModel
        binding.drawerNavView.addHeaderView(bindingNavHeader.root)

//        viewModel.navigate.observe(this, Observer {
//            //navigate到指定頁面並變動bottomNavigation
//            binding.bottomNavView.selectedItemId = R.id.navigation_cart
//        })

        // Observe current drawer toggle to set the navigation icon and behavior
//        viewModel.currentDrawerToggleType.observe(this, Observer { type ->
//
//            actionBarDrawerToggle?.isDrawerIndicatorEnabled = type.indicatorEnabled
//            supportActionBar?.setDisplayHomeAsUpEnabled(!type.indicatorEnabled)
//            binding.toolbar.setNavigationIcon(
//                when (type) {
//                    DrawerToggleType.BACK -> R.drawable.toolbar_back
//                    else -> R.drawable.toolbar_menu
//                }
//            )
//            actionBarDrawerToggle?.setToolbarNavigationClickListener {
//                when (type) {
//                    DrawerToggleType.BACK -> onBackPressed()
//                    else -> {}
//                }
//            }
//        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val filePath: String = ImagePicker.getFilePath(data) ?: ""
                    if (data != null) {
                        if (filePath.isNotEmpty()) {
                            imgPath.value = filePath
                            Log.e("data", data.toString())
                            Log.e("rqeCode", requestCode.toString())
                            Toast.makeText(this@MainActivity, imgPath.value, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "load_img_fail", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                ImagePicker.RESULT_ERROR -> Toast.makeText(
                    this,
                    ImagePicker.getError(data),
                    Toast.LENGTH_SHORT
                ).show()
                else -> Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

}

