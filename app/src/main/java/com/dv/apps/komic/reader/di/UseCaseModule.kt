package com.dv.apps.komic.reader.di

import com.dv.apps.komic.reader.domain.usecase.GetKomicPreviewTree
import com.dv.apps.komic.reader.domain.usecase.ScanVirtualFilesystemRecursively
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::ScanVirtualFilesystemRecursively)
    singleOf(::GetKomicPreviewTree)
}