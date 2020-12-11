package com.tron.familytree.branch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.tron.familytree.databinding.FragmentBranchBinding


class BranchFragment : Fragment() {

    private val viewModel: BranchViewModel by lazy {
        ViewModelProvider(this).get(BranchViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentBranchBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel




        val adapter = BranchViewAdapter(BranchViewAdapter.UserOnItemClickListener {
            Log.e("Click", it.toString())
            viewModel.itemSelected.value = it
        }
        ,viewModel
        )

        binding.recyclerBranch.adapter = adapter



        viewModel.userId.observe(viewLifecycleOwner, Observer {
            viewModel.getUser()
        })


        viewModel.TreeList.observe(viewLifecycleOwner, Observer {
            val layoutManager = GridLayoutManager(requireContext(),viewModel.getSpanCount(viewModel.children.size))
            binding.recyclerBranch.layoutManager = layoutManager
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {

                    val item = it[position]
                    return when (item) {
                        is TreeItem.Parent -> {
                            item.user.spanSize!!
                        }
                        is TreeItem.Mate -> {
                            item.user.spanSize!!
                        }
                        is TreeItem.Children -> {
                            item.user.spanSize!!
                        }
                        is TreeItem.ChildrenMid -> {
                            item.user.spanSize!!
                        }
                        is TreeItem.ParentAdd -> {
                            item.user.spanSize!!
                        }
                        is TreeItem.MateAdd -> {
                            item.user.spanSize!!
                        }
                        is TreeItem.ChildrenAdd -> {
                            item.user.spanSize!!
                        }
                        is TreeItem.Empty -> {
                            1
                        }
                        is TreeItem.EmptyLine -> {
                            1
                        }
                        is TreeItem.EmptyLineBot -> {
                            1
                        }
                    }
                }
            }
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.itemClick.observe(viewLifecycleOwner, Observer {
            Log.e("itemClick", it.toString())
            if (viewModel.itemClick.value == 100) {
                findNavController().navigate(
                    BranchFragmentDirections.actionGlobalBranchUserDetailDialog(
                        viewModel.itemSelected.value!!
                    )
                )
            }
            if (viewModel.itemClick.value == 200){
               viewModel.reQuery()
                Log.e("treeFinalList", viewModel.treeFinalList.toString())
                viewModel.userId.value = viewModel.itemSelected.value!!.name
            }
            if (viewModel.itemClick.value == 300) {
                findNavController().navigate(
                    BranchFragmentDirections.actionGlobalAddPeopleDialog(
                        viewModel.itemSelected.value!!
                    )
                )
            }
        })







        return binding.root
    }
}