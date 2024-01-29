package com.github.diegoberaldin.commonground.core.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.diegoberaldin.commonground.core.persistence.dao.SourceInfoDao

@Entity(tableName = SourceInfoDao.TABLE_NAME)
data class SourceInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo("name") val name: String = "",
    @ColumnInfo("type") val type: Int = 0,
    @ColumnInfo("config") val config: String = "",
)