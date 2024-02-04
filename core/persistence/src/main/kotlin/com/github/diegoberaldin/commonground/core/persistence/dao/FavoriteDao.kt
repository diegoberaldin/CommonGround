package com.github.diegoberaldin.commonground.core.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.diegoberaldin.commonground.core.persistence.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM $TABLE_NAME where url = :url")
    suspend fun getByUrl(url: String): FavoriteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg value: FavoriteEntity)

    @Query("DELETE FROM $TABLE_NAME where url = :url")
    suspend fun delete(url: String)

    companion object {
        const val TABLE_NAME = "favorite"
    }
}