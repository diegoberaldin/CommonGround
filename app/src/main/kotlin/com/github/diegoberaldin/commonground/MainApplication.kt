package com.github.diegoberaldin.commonground

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.github.diegoberaldin.commonground.core.appearance.CoreAppearanceModule
import com.github.diegoberaldin.commonground.core.commonui.CoreCommonUiModule
import com.github.diegoberaldin.commonground.core.persistence.CorePersistenceModule
import com.github.diegoberaldin.commonground.core.utils.CoreUtilsModule
import com.github.diegoberaldin.commonground.domain.favorites.DomainFavoritesModule
import com.github.diegoberaldin.commonground.domain.gallery.DomainGalleryModule
import com.github.diegoberaldin.commonground.domain.imagefetch.cache.DomainImageFetchCacheModule
import com.github.diegoberaldin.commonground.domain.imagefetch.fetcherimpl.DomainImageFetchFetcherImplModule
import com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.DomainImageFetchLemmyModule
import com.github.diegoberaldin.commonground.domain.imagesource.repository.DomainImageSourceRepositoryModule
import com.github.diegoberaldin.commonground.domain.imagesource.usecase.DomainImageSourceUseCaseModule
import com.github.diegoberaldin.commonground.domain.palette.DomainPaletteModule
import com.github.diegoberaldin.commonground.feature.drawer.FeatureDrawerModule
import com.github.diegoberaldin.commonground.feature.favorites.FeatureFavoritesModule
import com.github.diegoberaldin.commonground.feature.imagedetail.FeatureImageDetailModule
import com.github.diegoberaldin.commonground.feature.imagelist.FeatureImageListModule
import com.github.diegoberaldin.commonground.feature.settings.FeatureSettingsModule
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
                CoreCommonUiModule().module,
                CorePersistenceModule().module,
                CoreUtilsModule().module,

                DomainFavoritesModule().module,
                DomainGalleryModule().module,
                DomainImageFetchCacheModule().module,
                DomainImageFetchFetcherImplModule().module,
                DomainImageFetchLemmyModule().module,
                DomainImageSourceRepositoryModule().module,
                DomainImageSourceUseCaseModule().module,
                DomainPaletteModule().module,

                FeatureDrawerModule().module,
                FeatureFavoritesModule().module,
                FeatureImageListModule().module,
                FeatureImageDetailModule().module,
                FeatureSettingsModule().module,
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