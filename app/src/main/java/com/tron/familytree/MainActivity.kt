package com.tron.familytree

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.tron.familytree.data.User
import com.tron.familytree.databinding.ActivityMainBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.message.MessageFragmentDirections
import com.tron.familytree.util.CurrentFragmentType
import com.tron.familytree.util.UserManager


const val ADD_USER = 111
const val EDIT_USER = 222
const val ADD_PHOTO = 333

class MainActivity : AppCompatActivity() {

    val viewModel by viewModels<MainActivityViewModel> { getVmFactory() }

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var binding: ActivityMainBinding

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {

            R.id.navigation_branch -> {
                findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_branchFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map-> {
                findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_mapsFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_message -> {
                findNavController(R.id.myNavHostFragment).navigate(MessageFragmentDirections.actionGlobalMessageFragment(
                    User(id = UserManager.email.toString(),
                    name = UserManager.name.toString())
                ))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_profileFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_family -> {
                findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_familyFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        firebaseAnalytics = Firebase.analytics

        val navController = this.findNavController(R.id.myNavHostFragment)

        binding.imageCalendar.setOnClickListener {
            navController.navigate(R.id.action_global_calendarDialog)
        }

        binding.imageScan.setOnClickListener {
            navController.navigate(R.id.action_global_qrCodeFragment)
        }

        binding.imageBack.setOnClickListener {
            navController.navigateUp()
        }

        // observe current fragment change, only for show info
        viewModel.currentFragmentType.observe(this, androidx.lifecycle.Observer {
            Log.i("Tron","~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
            Log.i("Tron","[${viewModel.currentFragmentType.value}]")
            Log.i("Tron","~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
        })

        setupBottomNav()
        setupNavController()
    }

    private fun setupNavController(){
        findNavController(R.id.myNavHostFragment).addOnDestinationChangedListener{ navController: NavController, _: NavDestination, _: Bundle? ->
            viewModel.currentFragmentType.value = when(navController.currentDestination?.id) {
                R.id.branchFragment -> CurrentFragmentType.BRANCH
                R.id.familyFragment -> CurrentFragmentType.FAMILY
                R.id.mapsFragment -> CurrentFragmentType.MAPS
                R.id.messageFragment -> CurrentFragmentType.MESSAGE
                R.id.profileFragment -> CurrentFragmentType.PROFILE
                R.id.editUserFragment -> CurrentFragmentType.PROFILE_USER_EDIT
                R.id.calendarDialog -> CurrentFragmentType.CALENDAR
                R.id.eventDialog -> CurrentFragmentType.FAMILY_EVENT
                R.id.albumDetailFragment -> CurrentFragmentType.FAMILY_ALBUM
                R.id.qrCodeFragment -> CurrentFragmentType.QR_CODE
                R.id.qrCodeReaderFragment -> CurrentFragmentType.QR_CODE_SCAN
                R.id.chatRoomFragment -> CurrentFragmentType.CHAT_ROOM
                R.id.logInFragment -> CurrentFragmentType.LOGIN
                R.id.albumFragment -> CurrentFragmentType.FAMILY_ALBUM

                else -> viewModel.currentFragmentType.value
            }
        }
    }



    private fun setupBottomNav() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            when (resultCode) {
                Activity.RESULT_OK -> {

                    val filePath: String = ImagePicker.getFilePath(data) ?: ""
                    when(requestCode and 0x0000ffff) {
                        ADD_USER -> {
                            if (data != null) {
                                if (filePath.isNotEmpty()) {
                                    viewModel.addUserImgPath.value = filePath
                                    Log.e("data", data.toString())
                                    Log.e("reqCode", requestCode.toString())
                                    Log.e("rstCode", resultCode.toString())
                                    Toast.makeText(
                                        this@MainActivity,
                                        viewModel.addUserImgPath.value,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                        }
                        EDIT_USER -> {
                            if (data != null) {
                                if (filePath.isNotEmpty()) {
                                    viewModel.editUserImgPath.value = filePath
                                    Log.e("data", data.toString())
                                    Log.e("reqCode", requestCode.toString())
                                    Log.e("rstCode", resultCode.toString())
                                    Toast.makeText(
                                        this@MainActivity,
                                        viewModel.addUserImgPath.value,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }

                        }
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

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        Log.e("onSaveInstanceState",viewModel.addUserImgPath.value.toString())
    }
}

