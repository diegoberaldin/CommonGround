package com.github.diegoberaldin.commonground.core.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.diegoberaldin.commonground.core.persistence.dao.FavoriteDao
import com.github.diegoberaldin.commonground.core.persistence.dao.SourceInfoDao
import com.github.diegoberaldin.commonground.core.persistence.entities.FavoriteEntity
import com.github.diegoberaldin.commonground.core.persistence.entities.SourceInfoEntity

@Database(
    entities = [
        SourceInfoEntity::class,
        FavoriteEntity::class,
    ],
    version = 1,
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun sourceInfoDao(): SourceInfoDao
    abstract fun favoriteDao(): FavoriteDao
}