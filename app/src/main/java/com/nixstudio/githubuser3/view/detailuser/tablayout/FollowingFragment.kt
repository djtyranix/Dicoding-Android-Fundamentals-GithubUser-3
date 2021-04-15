package com.nixstudio.githubuser3.view.detailuser.tablayout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nixstudio.githubuser3.adapter.FollowerListAdapter
import com.nixstudio.githubuser3.adapter.FollowingListAdapter
import com.nixstudio.githubuser3.databinding.FragmentFollowingBinding
import com.nixstudio.githubuser3.model.UsersItem
import com.nixstudio.githubuser3.view.detailuser.DetailUserActivity
import com.nixstudio.githubuser3.viewmodel.DetailUserViewModel

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailUserViewModel by activityViewModels()
    private lateinit var viewAdapter: FollowingListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)

        viewAdapter = FollowingListAdapter()
        viewAdapter.notifyDataSetChanged()

        binding.followingRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = viewAdapter
        }

        showLoading(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getFollowing().observe(viewLifecycleOwner, { usersItem ->
            if (usersItem != null) {
                viewAdapter.setData(usersItem)
                showLoading(false)
            }
        })

        viewAdapter.setOnItemClickCallback(object : FollowingListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UsersItem) {
                showItemDetail(data)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showItemDetail(data: UsersItem) {
        val renewUserDetailIntent = Intent(activity, DetailUserActivity::class.java)
        renewUserDetailIntent.putExtra(DetailUserActivity.EXTRA_USER, data)
        startActivity(renewUserDetailIntent)
    }
}