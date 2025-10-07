package com.dv.apps.komic.reader.di

import com.dv.apps.komic.reader.feature.folder.FolderManagementViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::FolderManagementViewModel)
}