package com.nixstudio.consumerapp

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.nixstudio.githubuser3"
    const val SCHEME = "content"

    internal class FavoriteUserColumns: BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite"
            const val USERNAME = "login"
            const val URL = "url"
            const val AVATAR_URL = "avatar_url"

            val CONTENT_URI = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}