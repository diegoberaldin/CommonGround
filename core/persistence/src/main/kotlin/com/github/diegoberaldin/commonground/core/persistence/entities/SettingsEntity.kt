package com.github.diegoberaldin.commonground.core.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.diegoberaldin.commonground.core.persistence.dao.SettingsDao

@Entity(tableName = SettingsDao.TABLE_NAME)
data class SettingsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id: Int,
    @ColumnInfo("theme") val theme: Int?,
    @ColumnInfo("resizeMode") val resizeMode: Int?,
)