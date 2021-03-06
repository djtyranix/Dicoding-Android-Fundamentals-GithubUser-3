package com.nixstudio.githubuser3.view.main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.nixstudio.githubuser3.R

class MainActivity : AppCompatActivity() {

    private var mMainFragment = MainFragment()
    var doubleBackToExitOnce: Boolean = false
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        mMainFragment = getForegroundFragment() as MainFragment
    }

    fun getForegroundFragment(): Fragment? {
        navHostFragment = supportFragmentManager.findFragmentByTag("container_fragment") as NavHostFragment
        return when (navHostFragment) {
            else -> navHostFragment.childFragmentManager.fragments.get(0)
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitOnce = true

        Toast.makeText(this, resources.getString(R.string.exit_confirmation), Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            kotlin.run { doubleBackToExitOnce = false }
        }, 2000)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                mMainFragment.showLoading(false)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                mMainFragment.searchUser(query)
                return true
            }
        })

        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                Log.d("Closed", "Closed")
                return true
            }
        })

        menu.findItem(R.id.search)
            .setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    mMainFragment.binding.userRecyclerView.visibility = View.GONE
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    if (mMainFragment.viewAdapter.itemCount == 0) {
                        mMainFragment.binding.noUser.text =
                            resources.getString(R.string.welcome_message)
                        mMainFragment.binding.noUser.visibility = View.VISIBLE
                    } else {
                        mMainFragment.binding.userRecyclerView.visibility = View.VISIBLE
                        mMainFragment.binding.noUser.visibility = View.GONE
                    }

                    return true
                }

            })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting_menu -> {
                mMainFragment.view?.findNavController()
                    ?.navigate(R.id.action_mainFragment_to_settingsActivity)
                return true
            }
            else -> return true
        }
    }
}