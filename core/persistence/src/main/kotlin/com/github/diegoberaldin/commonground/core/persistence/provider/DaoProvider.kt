package com.github.diegoberaldin.commonground.core.persistence.provider

import com.github.diegoberaldin.commonground.core.persistence.dao.SourceInfoDao

interface DaoProvider {

    val sourceInfo: SourceInfoDao
}