package dev.mjanusz.recruitmentapp.ui.repos

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryDetailsArgModule {

    @Named("owner")
    @Provides
    @ViewModelScoped
    fun provideOwner(savedStateHandle: SavedStateHandle): String =
        checkNotNull(savedStateHandle["owner"])

    @Named("repo")
    @Provides
    @ViewModelScoped
    fun provideRepoName(savedStateHandle: SavedStateHandle): String =
        checkNotNull(savedStateHandle["repo"])
}
