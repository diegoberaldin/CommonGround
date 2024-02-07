package com.github.diegoberaldin.commonground.core.utils.appversion

import org.koin.core.annotation.Single

@Single
internal class DefaultAppVersionRepository : AppVersionRepository {

    private var version: String = ""
    override fun getAppVersion(): String = version

    override fun setAppVersion(value: String) {
        version = value
    }
}
