package com.chocolatecake.marvel.ui.stories.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.chocolatecake.marvel.R
import com.chocolatecake.marvel.databinding.StoriesFragmentBinding
import com.chocolatecake.marvel.ui.base.BaseFragment
import com.chocolatecake.marvel.ui.stories.viewModel.StoriesViewModel
import com.chocolatecake.marvel.ui.stories.adapter.StoriesAdapter

class StoriesFragment : BaseFragment<StoriesFragmentBinding, StoriesViewModel>() {

    override val viewModel: StoriesViewModel by viewModels()
    override val layoutIdFragment: Int = R.layout.stories_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val storiesAdapter = StoriesAdapter(mutableListOf(),viewModel)
        binding.recyclerViewStories.adapter = storiesAdapter
    }
}