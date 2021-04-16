package com.nixstudio.consumerapp

import android.database.Cursor

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<UsersItem> {
        val list = ArrayList<UsersItem>()

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val login = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.USERNAME))
                val url = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.URL))
                val avatarUrl = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.AVATAR_URL))

                val entry = UsersItem(login, url, avatarUrl)
                list.add(entry)
            }
        }

        return list
    }
}