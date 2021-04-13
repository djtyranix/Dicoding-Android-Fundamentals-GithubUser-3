package com.nixstudio.githubuser3.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsersItem(
        var login: String = "",
        var followersUrl: String? = null,
        var type: String? = null,
        var url: String? = null,
        var avatarUrl: String? = null,
        var id: Int? = null,
        var gravatarId: String? = null,
) : Parcelable
