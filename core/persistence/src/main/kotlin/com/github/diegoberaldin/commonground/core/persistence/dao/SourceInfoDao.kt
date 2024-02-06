package com.github.diegoberaldin.commonground.core.persistence.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.github.diegoberaldin.commonground.core.persistence.entities.SourceInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SourceInfoDao {

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): Flow<List<SourceInfoEntity>>

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    suspend fun getById(id: Int): SourceInfoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg value: SourceInfoEntity)

    @Update
    suspend fun update(value: SourceInfoEntity)

    @Delete
    suspend fun delete(value: SourceInfoEntity)

    @Query("DELETE FROM $TABLE_NAME WHERE 1")
    suspend fun deleteAll()

    companion object {
        const val TABLE_NAME = "source_info"
    }
}