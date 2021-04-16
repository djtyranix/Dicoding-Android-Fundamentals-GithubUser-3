package com.nixstudio.githubuser3.db

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.nixstudio.githubuser3.model.Favorite

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite")
    fun getAll(): List<Favorite>

    @Query("SELECT * FROM favorite WHERE login = :login")
    fun findByLogin(login: String): List<Favorite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg favorite: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query("SELECT COUNT(login) FROM favorite WHERE login = :login")
    fun checkIfExist(login: String): Int

    @Query("SELECT * FROM favorite")
    fun getAllToCursor(): Cursor
}