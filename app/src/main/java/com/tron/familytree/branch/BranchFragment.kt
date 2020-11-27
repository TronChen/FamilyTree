package com.tron.familytree.branch

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tron.familytree.R

class BranchFragment : Fragment() {

    companion object {
        fun newInstance() = BranchFragment()
    }

    private lateinit var viewModel: BranchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_branch, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BranchViewModel::class.java)
        // TODO: Use the ViewModel
    }

}