package com.github.diegoberaldin.commonground.core.persistence.provider

import android.content.Context
import androidx.room.Room
import com.github.diegoberaldin.commonground.core.persistence.AppDatabase
import org.koin.core.annotation.Single

@Single
internal class DefaultDaoProvider(
    context: Context,
) : DaoProvider {

    private val appDatabase = Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = "app-db"
    ).build()

    override val sourceInfo = appDatabase.sourceInfoDao()
    override val favorite = appDatabase.favoriteDao()
    override val settings = appDatabase.settingsDao()
}