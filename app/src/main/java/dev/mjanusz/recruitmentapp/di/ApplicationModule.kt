package dev.mjanusz.recruitmentapp.di

import android.app.UiModeManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideUiModeManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
}