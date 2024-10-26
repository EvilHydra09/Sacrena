package com.example.chatapplication.di

import android.content.Context
import com.example.chatapplication.R
import com.example.chatapplication.data.repository.AuthRepoImpl
import com.example.chatapplication.domain.helper.ChatHelper
import com.example.chatapplication.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        client: ChatClient
    ): AuthRepository {
        return AuthRepoImpl(firebaseAuth,client)
    }

    @Provides
    fun provideOfflinePluginFactory(@ApplicationContext context: Context) =
        StreamOfflinePluginFactory(
            appContext = context
        )

    @Provides
    fun provideStatePluginFactory(@ApplicationContext context: Context) = StreamStatePluginFactory(
        appContext = context,
        config = StatePluginConfig()
    )

    @Provides
    fun provideChatHelper(client: ChatClient,firebaseAuth: FirebaseAuth) = ChatHelper(client,firebaseAuth)




    @Singleton
    @Provides
    fun provideChatClient(
        @ApplicationContext context: Context,
        offlinePluginFactory: StreamOfflinePluginFactory,
        statePluginFactory: StreamStatePluginFactory
    ): ChatClient =
        ChatClient.Builder(context.getString(R.string.apikey), context)
            .withPlugins(offlinePluginFactory, statePluginFactory)
            .logLevel(ChatLogLevel.ALL)
            .build()
}


