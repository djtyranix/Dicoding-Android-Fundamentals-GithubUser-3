package com.nixstudio.githubuser3.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.nixstudio.githubuser3.db.AppDatabase

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val AUTHORITY = "com.nixstudio.githubuser3"
        private const val TABLE_NAME = "favorite"
        private const val ID_FAV_USER_DATA = 1
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(AUTHORITY, TABLE_NAME, ID_FAV_USER_DATA)
        }
    }

    private lateinit var db: AppDatabase

    override fun onCreate(): Boolean {
        db = context?.let { AppDatabase(it) }!!

        return false
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        when (uriMatcher.match(uri)) {
            ID_FAV_USER_DATA -> {
                cursor = db.favoriteDao().getAllToCursor()

                if (context != null) {
                    cursor.setNotificationUri(context?.contentResolver, uri)
                }
            }

            else -> {
                cursor = null
            }
        }

        return cursor
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}