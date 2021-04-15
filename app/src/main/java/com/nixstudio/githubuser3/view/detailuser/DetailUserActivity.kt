package com.nixstudio.githubuser3.view.detailuser

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.nixstudio.githubuser3.R
import com.nixstudio.githubuser3.model.UsersItem
import com.nixstudio.githubuser3.viewmodel.DetailUserViewModel

class DetailUserActivity : AppCompatActivity() {

    private val viewModel: DetailUserViewModel by viewModels()

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_user_activity)
        val user: UsersItem
        val args: DetailUserActivityArgs? by navArgs()

        user = args?.userData ?: intent.getParcelableExtra<UsersItem>(EXTRA_USER) as UsersItem

        val username = user.login

        viewModel.setFollowers(username)
        viewModel.setFollowing(username)
        viewModel.setUserDetail(username)

        if (savedInstanceState == null) {
            val mFragmentManager = supportFragmentManager
            val mDetailUserFragment = DetailUserFragment()
            val mTablayoutContainerFragment = TablayoutContainerFragment()

            mFragmentManager.beginTransaction()
                .replace(R.id.detail_user_fragment_container, mDetailUserFragment)
                .commit()

            mFragmentManager.beginTransaction()
                .replace(R.id.tablayout_fragment_container, mTablayoutContainerFragment)
                .commit()
        }
    }

    fun setActionBarTitle(title: String?) {
        supportActionBar?.title = title
    }
}