package com.koreatech.kotrip_android.di

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.koreatech.kotrip_android.api.RetrofitManager
import com.koreatech.kotrip_android.data.DataStoreImpl
import com.koreatech.kotrip_android.data.repository.TourRepositoryImpl
import com.koreatech.kotrip_android.kakao.KakaoService
import com.koreatech.kotrip_android.presentation.MainActivity
import com.koreatech.kotrip_android.presentation.views.entry.EntryViewModel
import com.koreatech.kotrip_android.presentation.views.history.HistoryViewModel
import com.koreatech.kotrip_android.presentation.views.home.HomeViewModel
import com.koreatech.kotrip_android.presentation.views.hotel.HotelViewModel
import com.koreatech.kotrip_android.presentation.views.login.LoginViewModel
import com.koreatech.kotrip_android.presentation.views.optimal.OptimalViewModel
import com.koreatech.kotrip_android.presentation.views.splash.SplashViewModel
import com.koreatech.kotrip_android.presentation.views.trip.TripViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.koin.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent

@Composable
fun getComposeViewModelOwner(): ViewModelOwner {
    return ViewModelOwner.from(
        LocalViewModelStoreOwner.current!!,
        LocalSavedStateRegistryOwner.current
    )
}

@Composable
fun getActivityComposeViewModelOwner(): ViewModelOwner {
    return ViewModelOwner.from(
        LocalContext.current as ComponentActivity
    )
}

@Composable
inline fun <reified T : ViewModel> getComposeViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T {
    val viewModelOwner = getComposeViewModelOwner()
    return KoinJavaComponent.getKoin().getViewModel(qualifier, { viewModelOwner }, parameters)
}

@Composable
inline fun <reified T : ViewModel> getActivityComposeViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T {
    val viewModelOwner = getActivityComposeViewModelOwner()
    return KoinJavaComponent.getKoin().getViewModel(qualifier, { viewModelOwner }, parameters)
}


val kotripModule = module {
    single {
        RetrofitManager.RetrofitServicePool.kotripApi
    }
    single {
        RetrofitManager.RetrofitServicePool.kotripAuthApi
    }
    single {
        RetrofitManager.RetrofitServicePool.kotripNaverApi
    }
    single {
        DataStoreImpl(androidContext())
    }
    single {
        TourRepositoryImpl(get())
    }
    viewModel {
        SplashViewModel(
            kotripApi = get(),
            dataStoreImpl = get()
        )
    }
    viewModel {
        LoginViewModel(
//            kakaoService = get(),
            dataStoreImpl = get(),
            kotripApi = get()
        )
    }
    viewModel {
        EntryViewModel(get(), get())
    }
    viewModel {
        TripViewModel(get())
    }
    viewModel {
        HomeViewModel(get(), get(), get(), get( ))
    }
    viewModel {
        OptimalViewModel(get())
    }
    viewModel {
        HotelViewModel(get(), get())
    }
    viewModel {
        HistoryViewModel(get(), get())
    }
}