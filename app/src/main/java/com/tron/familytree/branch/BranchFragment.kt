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


        viewModel.user.observe(viewLifecycleOwner, Observer {
            viewModel.getUserMate()
            viewModel.getUserChildren()
            viewModel.getUserFather()
        })

        viewModel.fatherId.observe(viewLifecycleOwner, Observer {
            viewModel.getUserMother()
        })

        viewModel.motherId.observe(viewLifecycleOwner, Observer {
            viewModel.getMateFather()
        })

        viewModel.mateFatherId.observe(viewLifecycleOwner, Observer {
            viewModel.getMateMother()
        })

        viewModel.mateMotherId.observe(viewLifecycleOwner, Observer {

        })

        viewModel.TreeList.observe(viewLifecycleOwner, Observer {
            val list = viewModel.getMockUsers()
            val layoutManager = GridLayoutManager(requireContext(),viewModel.getSpanCount(viewModel.children.size))
            binding.recyclerBranch.layoutManager = layoutManager
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {

                    val item = list[position]
                    when (item) {
                        is TreeItem.Parent -> {
                            return item.user.spanSize!!
                        }
                        is TreeItem.Mate -> {
                            return item.user.spanSize!!
                        }
                        is TreeItem.Children -> {
                            return item.user.spanSize!!
                        }
                        is TreeItem.ChildrenMid -> {
                            return item.user.spanSize!!
                        }
                        is TreeItem.Empty -> {
                            return 1
                        }
                        is TreeItem.EmptyLine -> {
                            return 1
                        }
                        is TreeItem.EmptyLineBot -> {
                            return 1
                        }
                    }
                }
            }
            adapter.submitList(list)
            adapter.notifyDataSetChanged()
        })

        viewModel.itemClick.observe(viewLifecycleOwner, Observer {
            Log.e("itemClick", it.toString())
            if (viewModel.itemClick.value == 100){
                findNavController().navigate(BranchFragmentDirections.actionGlobalBranchUserDetailDialog(
                    viewModel.itemSelected.value!!
                ))
            }
            if (viewModel.itemClick.value == 200){
                viewModel.parents.clear()
                viewModel.meAndMate.clear()
                viewModel.children.clear()
                viewModel.treeFinalList.clear()
                Log.e("treeFinalList", viewModel.treeFinalList.toString())
                viewModel.userId.value = viewModel.itemSelected.value!!.name
            }
        })







        return binding.root
    }
}