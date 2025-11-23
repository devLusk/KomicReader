package com.dv.apps.komic.reader.di

import com.dv.apps.komic.reader.feature.settings.folder.FolderSourceSettingsSectionViewModel
import com.dv.apps.komic.reader.feature.shelf.ShelfScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::ShelfScreenViewModel)
    viewModelOf(::FolderSourceSettingsSectionViewModel)
}