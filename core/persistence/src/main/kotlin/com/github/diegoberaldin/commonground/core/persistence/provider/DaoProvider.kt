package com.github.diegoberaldin.commonground.core.persistence.provider

import com.github.diegoberaldin.commonground.core.persistence.dao.FavoriteDao
import com.github.diegoberaldin.commonground.core.persistence.dao.SettingsDao
import com.github.diegoberaldin.commonground.core.persistence.dao.SourceInfoDao

interface DaoProvider {
    val sourceInfo: SourceInfoDao
    val favorite: FavoriteDao
    val settings: SettingsDao
}