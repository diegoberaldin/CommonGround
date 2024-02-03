package com.github.diegoberaldin.commonground

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.github.diegoberaldin.commonground.core.appearance.CoreAppearanceModule
import com.github.diegoberaldin.commonground.core.cache.CoreCacheModule
import com.github.diegoberaldin.commonground.core.commonui.CoreCommonUiModule
import com.github.diegoberaldin.commonground.core.persistence.CorePersistenceModule
import com.github.diegoberaldin.commonground.core.utils.CoreUtilsModule
import com.github.diegoberaldin.commonground.domain.gallery.DomainGalleryModule
import com.github.diegoberaldin.commonground.domain.imagefetch.fetcherimpl.DomainImageFetchFetcherImplModule
import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.DomainImageFetchLemmyModule
import com.github.diegoberaldin.commonground.domain.imagefetch.repository.DomainImageFetchRepositoryModule
import com.github.diegoberaldin.commonground.domain.imagefetch.usecase.DomainImageFetchUseCaseModule
import com.github.diegoberaldin.commonground.feature.drawer.FeatureDrawerModule
import com.github.diegoberaldin.commonground.feature.imagedetail.FeatureImageDetailModule
import com.github.diegoberaldin.commonground.feature.imagelist.FeatureImageListModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.ksp.generated.module

class MainApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(
                CoreAppearanceModule().module,
                CoreCacheModule().module,
                CoreCommonUiModule().module,
                CorePersistenceModule().module,
                CoreUtilsModule().module,
                DomainGalleryModule().module,
                DomainImageFetchRepositoryModule().module,
                DomainImageFetchUseCaseModule().module,
                DomainImageFetchFetcherImplModule().module,
                DomainImageFetchLemmyModule().module,
                FeatureDrawerModule().module,
                FeatureImageListModule().module,
                FeatureImageDetailModule().module,
            )
        }
    }

    override fun newImageLoader(): ImageLoader =
        ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .crossfade(true)
            .build()
}