package com.nixstudio.githubuser3.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.hadilq.liveevent.LiveEvent
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.nixstudio.githubuser3.BuildConfig
import com.nixstudio.githubuser3.R
import com.nixstudio.githubuser3.db.AppDatabase
import com.nixstudio.githubuser3.model.Favorite
import com.nixstudio.githubuser3.model.UserDetail
import com.nixstudio.githubuser3.model.UsersItem
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.log

class DetailUserViewModel : ViewModel() {

    val detailUser = MutableLiveData<UserDetail>()
    val listFollowers = MutableLiveData<ArrayList<UsersItem>>()
    val listFollowing = MutableLiveData<ArrayList<UsersItem>>()
    val apiKey = BuildConfig.API_KEY
    var db: AppDatabase? = null
    var isUserExist = LiveEvent<Boolean>()

    fun createDatabase(context: Context) {
        if (db == null) {
            db = AppDatabase(context)
        }
    }

    fun setUserDetail(login: String) {
        val url = "https://api.github.com/users/${login}"

        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token $apiKey")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                try {
                    val user = UserDetail()
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)

                    user.gistsUrl = responseObject.getString("gists_url")
                    user.reposUrl = responseObject.getString("repos_url")
                    user.followingUrl = responseObject.getString("following_url")
                    user.bio = responseObject.getString("bio")
                    user.login = responseObject.getString("login")
                    user.company = responseObject.getString("company")
                    user.id = responseObject.getInt("id")
                    user.publicRepos = responseObject.getInt("public_repos")
                    user.gravatarId = responseObject.getString("gravatar_id")
                    user.followersUrl = responseObject.getString("followers_url")
                    user.publicGists = responseObject.getInt("public_gists")
                    user.url = responseObject.getString("url")
                    user.receivedEventsUrl = responseObject.getString("received_events_url")
                    user.followers = responseObject.getInt("followers")
                    user.avatarUrl = responseObject.getString("avatar_url")
                    user.eventsUrl = responseObject.getString("events_url")
                    user.htmlUrl = responseObject.getString("html_url")
                    user.following = responseObject.getInt("following")
                    user.name = responseObject.getString("name")
                    user.location = responseObject.getString("location")
                    user.nodeId = responseObject.getString("node_id")

                    detailUser.postValue(user)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    fun setFollowers(login: String) {
        val listItems = ArrayList<UsersItem>()

        val url = "https://api.github.com/users/${login}/followers"
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token $apiKey")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val items = JSONArray(result)

                    for (i in 0 until items.length()) {
                        val currentUser = items.getJSONObject(i)
                        val usersItem = UsersItem()

                        //Assigning JSON Object
                        usersItem.login = currentUser.getString("login")
                        usersItem.followersUrl = currentUser.getString("followers_url")
                        usersItem.type = currentUser.getString("type")
                        usersItem.url = currentUser.getString("url")
                        usersItem.avatarUrl = currentUser.getString("avatar_url")
                        usersItem.id = currentUser.getInt("id")
                        usersItem.gravatarId = currentUser.getString("gravatar_id")

                        listItems.add(usersItem)
                    }

                    listFollowers.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    fun setFollowing(login: String) {
        val listItems = ArrayList<UsersItem>()

        val url = "https://api.github.com/users/${login}/following"

        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token $apiKey")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val items = JSONArray(result)

                    for (i in 0 until items.length()) {
                        val currentUser = items.getJSONObject(i)
                        val usersItem = UsersItem()

                        //Assigning JSON Object
                        usersItem.login = currentUser.getString("login")
                        usersItem.followersUrl = currentUser.getString("followers_url")
                        usersItem.type = currentUser.getString("type")
                        usersItem.url = currentUser.getString("url")
                        usersItem.avatarUrl = currentUser.getString("avatar_url")
                        usersItem.id = currentUser.getInt("id")
                        usersItem.gravatarId = currentUser.getString("gravatar_id")

                        listItems.add(usersItem)
                    }

                    listFollowing.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    fun getUserDetail(): LiveData<UserDetail> = detailUser

    fun getFollowers(): LiveData<ArrayList<UsersItem>> = listFollowers

    fun getFollowing(): LiveData<ArrayList<UsersItem>> = listFollowing

    fun insertFavorite(user: UsersItem) {
        viewModelScope.launch(Dispatchers.Main){
            withContext(Dispatchers.Default) {
                try {
                    val newFavorite = Favorite(
                        user.login,
                        user.url,
                        user.avatarUrl
                    )

                    db?.favoriteDao()?.insertAll(newFavorite)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun deleteFavorite(user: UsersItem) {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Default) {
                try {
                    val deletedFavorite = Favorite(
                        user.login,
                        user.url,
                        user.avatarUrl
                    )

                    db?.favoriteDao()?.delete(deletedFavorite)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun checkIsFavoriteExist(login: String) {
        isUserExist.postValue(false)

        viewModelScope.launch(Dispatchers.Main) {
            val isExist = withContext(Dispatchers.Default) {
                try {
                    val count = db?.favoriteDao()?.checkIfExist(login)

                    Log.d("count", count.toString())

                    if (count != null) {
                        if (count > 0) {
                            Log.d("return", true.toString())
                            return@withContext(true)
                        } else {
                            Log.d("return", false.toString())
                            return@withContext(false)
                        }
                    } else {
                        Log.d("else", false.toString())
                        return@withContext(false)
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            } as Boolean

            isUserExist.postValue(isExist)
        }
    }

    fun checkFavorite(): LiveData<Boolean> = isUserExist
}