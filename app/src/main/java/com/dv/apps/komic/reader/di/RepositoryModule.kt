package com.dv.apps.komic.reader.di

import com.dv.apps.komic.reader.data.repository.SettingsManagerImpl
import com.dv.apps.komic.reader.data.repository.FileSystemManagerImpl
import com.dv.apps.komic.reader.domain.repository.SettingsManager
import com.dv.apps.komic.reader.domain.repository.filesystem.FileSystemManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::SettingsManagerImpl) bind SettingsManager::class
    singleOf(::FileSystemManagerImpl) bind FileSystemManager::class
}