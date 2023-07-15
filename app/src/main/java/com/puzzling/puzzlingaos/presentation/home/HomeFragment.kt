package com.puzzling.puzzlingaos.presentation.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.puzzling.puzzlingaos.R
import com.puzzling.puzzlingaos.base.BaseFragment
import com.puzzling.puzzlingaos.databinding.FragmentHomeBinding
import com.puzzling.puzzlingaos.presentation.home.personal.PersonalDashboardFragment
import com.puzzling.puzzlingaos.presentation.home.team.TeamDashboardFragment
import com.puzzling.puzzlingaos.presentation.main.HomeChooseProjectFragment

class HomeFragment :
    BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel by viewModels<HomeViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabItem()
        projectNameBtn()
        clickTabItem()
        clickNotiIcon()
    }

    private fun initTabItem() {
        parentFragmentManager.commit {
            replace(R.id.fcv_home_container, PersonalDashboardFragment())
        }
    }

    private fun clickTabItem() {
        binding.tlHomeDashboard.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val fragment: Fragment = when (tab.position) {
                    0 -> PersonalDashboardFragment()
                    1 -> TeamDashboardFragment()
                    else -> PersonalDashboardFragment()
                }
                parentFragmentManager.commit {
                    replace(R.id.fcv_home_container, fragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Not used
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Not used
            }
        })
    }

    private fun projectNameBtn() {
        binding.clHomeProjectMain.setOnClickListener {
            val chooseProjectFragment = HomeChooseProjectFragment()
            chooseProjectFragment.show(parentFragmentManager, "show")
        }
    }

    private fun clickNotiIcon() {
        binding.btnHomeNotification.setOnClickListener {
            val isCardVisible = binding.cvHomePopup.isVisible
            binding.cvHomePopup.isVisible = !isCardVisible
        }
    }
}
