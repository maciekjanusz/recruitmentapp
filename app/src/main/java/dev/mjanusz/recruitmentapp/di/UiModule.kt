package dev.mjanusz.recruitmentapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mjanusz.recruitmentapp.ui.common.ChannelEventHandler
import dev.mjanusz.recruitmentapp.ui.common.Action
import dev.mjanusz.recruitmentapp.ui.common.UIEventHandler
import dev.mjanusz.recruitmentapp.ui.model.Repository
import dev.mjanusz.recruitmentapp.ui.model.RepositoryLanguage

@Module
@InstallIn(SingletonComponent::class)
interface UiModule {

    @Binds
    fun bindRepoClickHandler(handler: ChannelEventHandler<Repository>): UIEventHandler<Repository>

    @Binds
    fun bindLanguageClickHandler(handler: ChannelEventHandler<RepositoryLanguage>): UIEventHandler<RepositoryLanguage>

    @Binds
    fun bindActionEventHandler(handler: ChannelEventHandler<Action>): UIEventHandler<Action>

}