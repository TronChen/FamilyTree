package com.tron.familytree.profile.edituser

import android.app.AlertDialog
import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tron.familytree.R
import com.tron.familytree.databinding.FragmentEditUserBinding
import com.tron.familytree.databinding.FragmentProfileBinding
import com.tron.familytree.ext.getVmFactory
import com.tron.familytree.profile.ProfileViewModel

class EditUserFragment : Fragment() {

    private val viewModel by viewModels<EditUserViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentEditUserBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.conAddEpisode.setOnClickListener {
            findNavController().navigate(R.id.action_global_editEpisodeDialog)
//            addToCart()
        }


        return binding.root
    }



    private fun addToCart() {
        val addToCart = LayoutInflater.from(context).inflate(R.layout.fragment_edit_episode, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(addToCart)
        val addToCartSuccess = mBuilder.show()
        addToCartSuccess.window?.setBackgroundDrawableResource(R.color.transparent)
    }
}

