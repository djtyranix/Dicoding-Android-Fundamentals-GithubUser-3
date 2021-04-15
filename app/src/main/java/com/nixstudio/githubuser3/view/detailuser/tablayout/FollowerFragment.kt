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
import com.nixstudio.githubuser3.adapter.UserListAdapter
import com.nixstudio.githubuser3.databinding.FragmentFollowerBinding
import com.nixstudio.githubuser3.model.UsersItem
import com.nixstudio.githubuser3.view.detailuser.DetailUserActivity
import com.nixstudio.githubuser3.viewmodel.DetailUserViewModel

class FollowerFragment : Fragment() {

    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailUserViewModel by activityViewModels()
    private lateinit var viewAdapter: FollowerListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)

        viewAdapter = FollowerListAdapter()
        viewAdapter.notifyDataSetChanged()

        binding.followersRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = viewAdapter
        }

        showLoading(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getFollowers().observe(viewLifecycleOwner, { usersItem ->
            if (usersItem != null) {
                viewAdapter.setData(usersItem)
                showLoading(false)
            }
        })

        viewAdapter.setOnItemClickCallback(object : FollowerListAdapter.OnItemClickCallback {
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