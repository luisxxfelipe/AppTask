package com.conect.taskapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.conect.taskapp.R
import com.conect.taskapp.databinding.FragmentHomeBinding
import com.conect.taskapp.ui.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTabs()
    }

    private fun initTabs(){
        val pageAdapter = ViewPagerAdapter(requireActivity())
        binding.viewPager2.adapter = pageAdapter

        pageAdapter.addFragment(TodoFragment(),R.string.status_task_todo)
        pageAdapter.addFragment(DoingFragment(),R.string.status_task_doing)
        pageAdapter.addFragment(DoneFragment(),R.string.status_task_done)

        binding.viewPager2.offscreenPageLimit = pageAdapter.itemCount

        TabLayoutMediator(binding.tabLayout1, binding.viewPager2){tab, position->
            tab.text = getString(pageAdapter.getTitle(position))
        }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}