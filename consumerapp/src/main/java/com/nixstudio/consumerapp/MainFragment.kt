package com.nixstudio.consumerapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nixstudio.consumerapp.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    val viewModel: FavoriteViewModel by activityViewModels()
    private var _binding: FragmentMainBinding? = null
    val binding get() = _binding!!
    private lateinit var viewAdapter: UserListAdapter

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        viewAdapter = UserListAdapter()
        viewAdapter.notifyDataSetChanged()

        binding.favRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = viewAdapter
        }

        binding.favRecyclerView.visibility = View.GONE
        showLoading(true)
        activity?.let { viewModel.setFavList(it) }

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

                viewAdapter.setData(favItem)
                showLoading(false)
            }
        })
    }
}