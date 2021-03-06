package com.tron.familytree.branch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.tron.familytree.R
import com.tron.familytree.databinding.FragmentBranchBinding
import com.tron.familytree.ext.getVmFactory

const val DETAIL = 100
const val QUERY = 200
const val ADD_PEOPLE = 300

class BranchFragment : Fragment() {

    private val viewModel by viewModels<BranchViewModel> {getVmFactory()}


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
            viewModel.searchBranchUser(it)
        })

        viewModel.children.observe(viewLifecycleOwner, Observer {
//            viewModel.searchBranchUser(viewModel.userId.value!!)
            Log.e("childrenSize", it.toString())
        })

        viewModel.TreeList.observe(viewLifecycleOwner, Observer {
//            Log.e("TreeList.size", it.size.toString())

            val layoutManager = GridLayoutManager(requireContext(),viewModel.getSpanCount(viewModel.children.value!!))
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
//            Log.e("itemClick", it.toString())
            if (viewModel.itemClick.value == DETAIL) {
                findNavController().navigate(
                    BranchFragmentDirections.actionGlobalBranchUserDetailDialog(
                        viewModel.itemSelected.value!!
                    )
                )
            }
            if (viewModel.itemClick.value == QUERY){
               viewModel.reQuery()
                viewModel.TreeList.value = null
                Log.e("treeFinalList", viewModel.treeFinalList.toString())
                viewModel.userId.value = viewModel.itemSelected.value!!.id
            }
            if (viewModel.itemClick.value == ADD_PEOPLE) {
                findNavController().navigate(
                    BranchFragmentDirections.actionGlobalAddPeopleDialog(
                        viewModel.itemSelected.value!!
                    )
                )
                viewModel.reQuery()

            }
        })

        viewModel.findUser.observe(viewLifecycleOwner, Observer {
            if (it.familyId == null || it.familyId == "" ||
                        it.gender == null || it.gender == "" ||
                        it.name == ""
            ){
                findNavController().navigate(R.id.action_global_instructionDialog)
            }
        })


        return binding.root
    }
}