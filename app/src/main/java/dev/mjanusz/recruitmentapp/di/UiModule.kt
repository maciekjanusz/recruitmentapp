package dev.mjanusz.recruitmentapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mjanusz.recruitmentapp.ui.common.ChannelEventHandler
import dev.mjanusz.recruitmentapp.ui.common.UIEventHandler
import dev.mjanusz.recruitmentapp.ui.model.User

@Module
@InstallIn(SingletonComponent::class)
interface UiModule {

    @Binds
    fun bindUserClickHandler(handler: ChannelEventHandler<User>): UIEventHandler<User>
}