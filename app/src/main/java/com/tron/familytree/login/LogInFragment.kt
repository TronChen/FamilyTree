package com.tron.familytree.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.tron.familytree.R
import com.tron.familytree.data.User
import com.tron.familytree.databinding.FragmentLogInBinding
import com.tron.familytree.ext.getVmFactory


class LogInFragment : Fragment() {

    private val viewModel by viewModels<LogInViewModel> { getVmFactory() }

    private var googleSignInClient: GoogleSignInClient? = null
    private var GOOGLE_LOGIN_CODE = 1993


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentLogInBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        binding.conLogin.setOnClickListener {
            googleLogin()

        }


        viewModel.userAuth.observe(viewLifecycleOwner, Observer {
            if (it == true){
                findNavController().navigate(R.id.action_global_branchFragment)
            }
        })



        return binding.root
    }

    //    啟動activity 進行google信任登入 打開選擇google帳號的畫面
    private fun googleLogin() {
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
    }

    //    取得登入完後的結果 判斷是否是從google回來
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//    如果是的話 取得帳號資訊
        if (requestCode == GOOGLE_LOGIN_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            result?.isSuccess.let {
                val account = result?.signInAccount
                Log.d("google_account", "${account?.id} ")
                account?.let {
                    getLoginAuth(it)
                }
            }
        }
    }

    private fun getLoginAuth(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
//        將credential傳到firebase並建立帳號
        val auth = FirebaseAuth.getInstance()
        auth?.signInWithCredential(credential)?.addOnCompleteListener {
            if (it.isSuccessful) {
                val user = auth.currentUser
                user?.let {
                    val currentUser = User(
                        name = user.displayName!!,
                        id = user.email!!,
                        userImage = user.photoUrl.toString()
                    )
                    com.tron.familytree.util.UserManager.name = user.displayName
                    viewModel.addUserToFirebase(currentUser)
                }
                Log.d("google", "${user?.email}, ${user?.displayName}, ${user?.photoUrl} ")
            } else {
                it.exception?.let {
                    Log.d(
                        "getLoginAuth_exception",
                        "[${this::class.simpleName}] Error getting documents. ${it.message}"
                    )
                }
            }
        }
    }
}