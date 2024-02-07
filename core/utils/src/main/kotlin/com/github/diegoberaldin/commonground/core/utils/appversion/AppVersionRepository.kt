package com.github.diegoberaldin.commonground.core.utils.appversion

interface AppVersionRepository {
    fun getAppVersion(): String
    fun setAppVersion(value: String)
}
