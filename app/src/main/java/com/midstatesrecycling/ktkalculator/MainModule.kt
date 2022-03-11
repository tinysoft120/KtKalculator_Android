package com.midstatesrecycling.ktkalculator

import com.midstatesrecycling.ktkalculator.network.provideDefaultCache
import com.midstatesrecycling.ktkalculator.network.provideOkHttp
import com.midstatesrecycling.ktkalculator.network.provideRestApi
import com.midstatesrecycling.ktkalculator.network.provideApiRetrofit
import com.midstatesrecycling.ktkalculator.repository.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {

    factory {
        provideDefaultCache()
    }
    factory {
        provideOkHttp(get(), get())
    }
    single {
        provideApiRetrofit(get())
    }
    single {
        provideRestApi(get())
    }
}

private val mainModule = module {
    single {
        androidContext().contentResolver
    }
}

private val dataModule = module {
    single {
        RealRepository(
            get(),
            get(),
            get()
        )
    }
}

private val viewModules = module {

    viewModel {
        MainViewModel(
            get(),
            get()
        )
    }

//    viewModel { (albumId: Long) ->
//        AlbumDetailsViewModel(
//            get(),
//            albumId
//        )
//    }
//
//    viewModel { (artistId: Long?, artistName: String?) ->
//        ArtistDetailsViewModel(
//            get(),
//            artistId,
//            artistName
//        )
//    }
//
//    viewModel { (playlist: PlaylistWithSongs) ->
//        PlaylistDetailsViewModel(
//            get(),
//            playlist
//        )
//    }
//
//    viewModel { (genre: Genre) ->
//        GenreDetailsViewModel(
//            get(),
//            genre
//        )
//    }
}

val appModules = listOf(mainModule, dataModule, viewModules, networkModule)