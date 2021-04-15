package com.nixstudio.githubuser3.view.detailuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import com.nixstudio.githubuser3.R
import com.nixstudio.githubuser3.db.AppDatabase
import com.nixstudio.githubuser3.model.Favorite
import com.nixstudio.githubuser3.model.UsersItem
import com.nixstudio.githubuser3.viewmodel.DetailUserViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.lang.Exception
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DetailUserActivity : AppCompatActivity() {

    private val viewModel: DetailUserViewModel by viewModels()
    private lateinit var user: UsersItem

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_user_activity)
        val args: DetailUserActivityArgs? by navArgs()

        user = args?.userData ?: intent.getParcelableExtra<UsersItem>(EXTRA_USER) as UsersItem

        val username = user.login

        viewModel.setFollowers(username)
        viewModel.setFollowing(username)
        viewModel.setUserDetail(username)
        viewModel.createDatabase(this)

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.user_detail_menu, menu)

        if (viewModel.isExistTemp) {
            menu.findItem(R.id.favorite).setIcon(R.drawable.ic_baseline_favorite_24_red)
        } else {
            menu.findItem(R.id.favorite).setIcon(R.drawable.ic_baseline_favorite_24)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                viewModel.checkIsFavoriteExist(user.login)

                viewModel.checkFavorite().observe(this, { isExist ->
                        if (isExist) { //User already exist, delete fav
                            viewModel.deleteFavorite(user)
                            item.setIcon(R.drawable.ic_baseline_favorite_24)
                            Toast.makeText(this, resources.getString(R.string.fav_del_success), Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.insertFavorite(user)
                            item.setIcon(R.drawable.ic_baseline_favorite_24_red)
                            Toast.makeText(this, resources.getString(R.string.fav_add_success), Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                return true
            }

            R.id.share -> {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Github User - ${user.login}")
                shareIntent.putExtra(Intent.EXTRA_TEXT, "https://github.com/${user.login}")
                shareIntent.setType("text/plain")
                startActivity(shareIntent)
                return true
            }

            else -> return true
        }
    }
}