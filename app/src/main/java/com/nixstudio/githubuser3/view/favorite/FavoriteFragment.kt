package com.nixstudio.githubuser3.view.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nixstudio.githubuser3.R
import com.nixstudio.githubuser3.adapter.FavoriteListAdapter
import com.nixstudio.githubuser3.adapter.UserListAdapter
import com.nixstudio.githubuser3.databinding.FavoriteFragmentBinding
import com.nixstudio.githubuser3.model.Favorite
import com.nixstudio.githubuser3.model.UsersItem
import com.nixstudio.githubuser3.view.detailuser.DetailUserActivity
import com.nixstudio.githubuser3.view.main.MainFragmentDirections
import com.nixstudio.githubuser3.viewmodel.FavoriteViewModel

class FavoriteFragment : Fragment() {

    val viewModel: FavoriteViewModel by activityViewModels()
    private var _binding: FavoriteFragmentBinding? = null
    val binding get() = _binding!!
    private lateinit var viewAdapter: FavoriteListAdapter

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteFragmentBinding.inflate(inflater, container, false)

        viewAdapter = FavoriteListAdapter()
        viewAdapter.notifyDataSetChanged()

        binding.favRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = viewAdapter
        }

        binding.favRecyclerView.visibility = View.GONE
        showLoading(true)
        viewModel.setFavList()

        return binding.root
    }

    fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
            binding.noFav.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getFavList().observe(viewLifecycleOwner, { favItem ->
            if (favItem != null) {
                if (favItem.isNotEmpty()) {
                    binding.noFav.visibility = View.GONE
                    binding.favRecyclerView.visibility = View.VISIBLE
                } else {
                    binding.noFav.text = resources.getString(R.string.no_favorite)
                    binding.noFav.visibility = View.VISIBLE
                    binding.favRecyclerView.visibility = View.GONE
                }

                viewAdapter.setData(favItem as ArrayList<Favorite>)
                showLoading(false)
            }
        })

        viewAdapter.setOnItemClickCallback(object : FavoriteListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Favorite) {
                showItemDetail(data)
            }
        })
    }

    private fun showItemDetail(user: Favorite) {
        val data = UsersItem(user.login)
        val renewUserDetailIntent = Intent(activity, DetailUserActivity::class.java)
        renewUserDetailIntent.putExtra(DetailUserActivity.EXTRA_USER, data)
        startActivity(renewUserDetailIntent)
    }
}