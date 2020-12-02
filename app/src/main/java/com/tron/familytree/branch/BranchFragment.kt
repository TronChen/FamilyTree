package com.tron.familytree.branch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

//        val adapter = BranchRecyclerViewAdapter(BranchRecyclerViewAdapter.UserOnItemClickListener {
//            Log.e("Click", it.toString())
//        })
        val adapter = TestAdapter(TestAdapter.UserOnItemClickListener {
            Log.e("Click", it.toString())
        })

//        val adapter426 = BranchAdapter4_2_6(BranchAdapter4_2_6.UserOnItemClickListener {
//            Log.e("Click", it.toString())
//        })

//        binding.recyclerBranch.adapter = adapter426
        binding.recyclerBranch.adapter = adapter


        viewModel.user.observe(viewLifecycleOwner, Observer {
            viewModel.getUserMate()
            viewModel.getUserChildren()
            viewModel.getUserFather()
            Log.e("User", it.toString())
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
//            Log.e("Level1", it[0].toString())
//            viewModel.addAdapterList()

            viewModel.createMock()
            viewModel.getChildrenList(2)
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


        })








        return binding.root
    }
}