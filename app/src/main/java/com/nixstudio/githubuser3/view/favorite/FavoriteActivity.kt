package com.nixstudio.githubuser3.view.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nixstudio.githubuser3.R
import com.nixstudio.githubuser3.db.AppDatabase

class FavoriteActivity : AppCompatActivity() {

    val db = AppDatabase(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorite_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FavoriteFragment.newInstance())
                .commitNow()
        }
    }
}