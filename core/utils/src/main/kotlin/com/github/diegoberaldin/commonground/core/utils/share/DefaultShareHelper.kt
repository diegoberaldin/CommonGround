package com.github.diegoberaldin.commonground.core.utils.share

import android.content.Context
import android.content.Intent
import org.koin.core.annotation.Single

@Single
internal class DefaultShareHelper(
    private val context: Context,
) : ShareHelper {
    override fun share(url: String, mimeType: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = mimeType
        }

        val shareIntent = Intent.createChooser(sendIntent, null).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(shareIntent)
    }
}
