package com.mft100.gas.demo.hilt.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.qmuiteam.qmui.skin.QMUISkinManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ModuleApp {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideQmuiSkinManager(context: Context): QMUISkinManager = QMUISkinManager.defaultInstance(context)

}