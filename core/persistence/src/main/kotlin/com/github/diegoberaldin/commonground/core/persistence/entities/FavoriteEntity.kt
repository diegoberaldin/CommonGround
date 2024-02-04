package com.github.diegoberaldin.commonground.core.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.diegoberaldin.commonground.core.persistence.dao.FavoriteDao

@Entity(tableName = FavoriteDao.TABLE_NAME)
data class FavoriteEntity(
    @PrimaryKey
    @ColumnInfo("url") val url: String = "",
    @ColumnInfo("title") val title: String = "",
)