package com.tron.familytree

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import app.appworks.school.publisher.data.source.FamilyTreeRepository
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tron.familytree.databinding.ActivityMainBinding
import com.tron.familytree.databinding.NavHeaderDrawerBinding
import java.util.Observer


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {

            R.id.navigation_branch -> {
                binding.fab.visibility = View.VISIBLE
                findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_branchFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map-> {
                binding.fab.visibility = View.VISIBLE
                findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_mapsFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_message -> {
                binding.fab.visibility = View.VISIBLE
                findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_messageFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                binding.fab.visibility = View.VISIBLE
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
            navController.navigate(R.id.action_global_familyFragment)
        }

        binding.menuEvent.setOnClickListener {
            navController.navigate(R.id.action_global_createEventDialog)
        }

        binding.menuAlbum.setOnClickListener {
            navController.navigate(R.id.action_global_createAlbumDialog)
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
}

