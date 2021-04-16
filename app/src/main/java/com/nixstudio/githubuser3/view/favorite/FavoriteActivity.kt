package com.nixstudio.githubuser3.view.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.nixstudio.githubuser3.R
import com.nixstudio.githubuser3.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorite_activity)

        viewModel.createDatabase(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FavoriteFragment.newInstance())
                .commitNow()
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.setFavList()
    }
}