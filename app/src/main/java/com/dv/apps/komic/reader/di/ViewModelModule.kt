package com.dv.apps.komic.reader.di

import com.dv.apps.komic.reader.feature.settings.folder.FolderSourceSettingsSectionViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::FolderSourceSettingsSectionViewModel)
}