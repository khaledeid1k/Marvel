package com.chocolatecake.marvel.ui.home.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.chocolatecake.marvel.R
import com.chocolatecake.marvel.databinding.FragmentHomeBinding
import com.chocolatecake.marvel.ui.base.BaseFragment
import com.chocolatecake.marvel.ui.home.adapter.HomeAdapter
import com.chocolatecake.marvel.ui.home.model.HomeItem
import com.chocolatecake.marvel.ui.home.view_model.HomeViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    lateinit var adapter: HomeAdapter
    override val viewModel: HomeViewModel by viewModels()

    override val layoutIdFragment: Int
        get() = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        updateItems()
        handelNavigation()
    }

    private fun setAdapter() {
        val layoutManager = GridLayoutManager(
            requireContext(),
            2
        )
        binding.recyclerViewHome.layoutManager = layoutManager
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0 || position == 1) {
                    2
                } else {
                    1
                }
            }
        }
        adapter = HomeAdapter(
            mutableListOf(
                HomeItem.EventsItem(emptyList()),
                HomeItem.SeriesItem(emptyList()),
            ), viewModel
        )
        binding.recyclerViewHome.adapter = adapter
    }

    private fun updateItems() {
        viewModel.events.observe(viewLifecycleOwner) { status ->
            status.toData()?.let {
                adapter.setItem(HomeItem.EventsItem(it))
            }
        }
    }

    private fun handelNavigation() {
        viewModel.eventId.observe(viewLifecycleOwner) {
            if (it != null) {
                //ToDo:Navigate To Event Details With It
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
            }
        }
    }

}