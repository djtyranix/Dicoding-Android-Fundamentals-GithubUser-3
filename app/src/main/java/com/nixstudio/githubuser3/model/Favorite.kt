package com.nixstudio.githubuser3.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite")
data class Favorite(
    @PrimaryKey val login: String,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String?
): Serializable
