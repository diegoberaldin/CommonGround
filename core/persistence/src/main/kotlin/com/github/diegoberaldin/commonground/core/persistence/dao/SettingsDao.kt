package com.github.diegoberaldin.commonground.core.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.diegoberaldin.commonground.core.persistence.entities.SettingsEntity

@Dao
interface SettingsDao {

    @Query("SELECT * FROM $TABLE_NAME WHERE id = 1")
    suspend fun get(): SettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg value: SettingsEntity)

    companion object {
        const val TABLE_NAME = "settings"
    }
}