package com.nixstudio.githubuser3.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nixstudio.githubuser3.view.detailuser.TablayoutContainerFragment
import com.nixstudio.githubuser3.view.detailuser.tablayout.FollowerFragment
import com.nixstudio.githubuser3.view.detailuser.tablayout.FollowingFragment

class SectionsPagerAdapter(fragment: TablayoutContainerFragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null

        when (position) {
            0 -> fragment = FollowerFragment()
            1 -> fragment = FollowingFragment()
        }

        return fragment as Fragment
    }

}